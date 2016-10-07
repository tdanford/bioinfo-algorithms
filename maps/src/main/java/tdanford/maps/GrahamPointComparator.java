package tdanford.maps;

import java.util.Comparator;

/**
 * Sorts points first on angle and then on distance; this is a sort order on
 * points useful for Graham's algorithm for finding the convex hull in 2D.
 */
public class GrahamPointComparator implements Comparator<Point> {

    private final Point base;

    public GrahamPointComparator(final Point base) {
        this.base = base;
    }

    @Override
    public int compare(Point p1, Point p2) {

        int area2 = Triangle.area2(base, p1, p2);
        if(area2 > 0) { return -1; }
        if(area2 < 0) { return 1; }

        int d1 = base.distance(p1), d2 = base.distance(p2);
        if(d1 < d2) { return -1; }
        if(d1 > d2) { return 1; }

        return 0;
    }
}
