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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvexHull implements Paintable {

    public static Logger LOG = LoggerFactory.getLogger(ConvexHull.class);

    private final Sites sites;
    private final ArrayList<Point> hull;

    public ConvexHull(final Sites sites) {
        this.sites = sites;
        this.hull = new ArrayList<>();
    }

    @Override
    public void regenerate() {
        //sites.regenerate();
        hull.clear();

        Point[] siteArray = sites.get().toArray(new Point[sites.size()]);
        if(siteArray.length >= 3) {
            hull.addAll(new GrahamScan(siteArray).hull());
        }
    }

    @Override
    public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
        final int N = hull.size();
        new Aggregate(IntStream.range(0, N).mapToObj(i -> new Edge(hull.get(i), hull.get((i + 1) % N))).collect(toList()))
            .paint(g, logical, physical);
    }
}
