package tdanford.maps;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static tdanford.maps.Triangle.area2;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrahamScan implements Paintable {

    public static Logger LOG = LoggerFactory.getLogger(GrahamScan.class);

    private final Sites sites;
    private final ArrayList<Point> hull;

    public GrahamScan(final Sites sites) {
        this.sites = sites;
        this.hull = new ArrayList<>();
    }

    @Override
    public void regenerate() {
        //sites.regenerate();
        hull.clear();

        Point[] siteArray = sites.get().toArray(new Point[sites.size()]);
        if(siteArray.length >= 3) {
            findLowest(siteArray);
            Point p0 = siteArray[0];
            Arrays.sort(siteArray, 1, siteArray.length, new GrahamPointComparator(p0));

            List<Integer> hullIndices = graham(siteArray);
            for (Integer i : hullIndices) {
                hull.add(siteArray[i]);
            }
        }
    }

    private List<Integer> graham(Point[] P) {
        LOG.info("Sorted Array: \n{}",
            IntStream.range(0, P.length).mapToObj(i -> String.format("%d: %s", i, P[i])).collect(joining("\n")));

        LinkedList<Integer> stack = new LinkedList<>();
        int N = P.length;
        stack.addFirst(N - 1);
        stack.addFirst(0);

        int i = 1;
        while (i < N) {

            LOG.info("a_idx={}, b_idx={}, c_idx={}", stack.get(1), stack.get(0), i);
            Point a = P[stack.get(1)], b = P[stack.get(0)], c = P[i];
            LOG.info("graham iteration {}, stack {}", i, stack);
            LOG.info("a={}, b={}, c={}", a, b, c);

            LOG.info("area2({}, {}, {}) = {}", a, b, c, area2(a, b, c));

            if (Edge.left(a, b, c)) {
                LOG.info("graham : push {} onto stack {}", i, stack);
                stack.addFirst(i);
                i++;
            } else {
                int removed = stack.removeFirst();
                LOG.info("graham : removed {} from stack {}", removed, stack);
            }
        }

        stack.removeFirst();
        return stack;
    }

    private void findLowest(Point[] siteArray) {
        int lowest = 0;
        for (int i = 1; i < siteArray.length; i++) {
            if (siteArray[i].y < siteArray[lowest].y ||
                (siteArray[i].y == siteArray[lowest].y && siteArray[i].x > siteArray[lowest].x)) {
                lowest = i;
            }
        }

        LOG.info("Lowest: i={}, point={}", lowest, siteArray[lowest]);

        Point tmp = siteArray[lowest];
        siteArray[lowest] = siteArray[0];
        siteArray[0] = tmp;
    }

    @Override
    public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
        final int N = hull.size();
        new Aggregate(IntStream.range(0, N).mapToObj(i -> new Edge(hull.get(i), hull.get((i + 1) % N))).collect(toList()))
            .paint(g, logical, physical);
    }
}
