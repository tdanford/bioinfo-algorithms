package tdanford.maps;

import static java.util.stream.Collectors.joining;
import static tdanford.maps.Triangle.area2;
import java.util.*;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrahamScan {

    private static final Logger LOG = LoggerFactory.getLogger(GrahamScan.class);

    private ArrayList<Point> hull;

    public GrahamScan(Point... ps) {
        hull = new ArrayList<>();
        scan(ps);
    }

    public ArrayList<Point> hull() { return hull; }

    private void scan(Point[] siteArray) {
        findLowest(siteArray);

        Point p0 = siteArray[0];
        Arrays.sort(siteArray, 1, siteArray.length, new GrahamPointComparator(p0));

        List<Integer> hullIndices = graham(siteArray);
        for (Integer i : hullIndices) {
            hull.add(siteArray[i]);
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

    public static void findLowest(Point[] siteArray) {
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
}
