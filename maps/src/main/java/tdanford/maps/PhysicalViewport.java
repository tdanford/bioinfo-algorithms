package tdanford.maps;

import java.util.function.Function;
import com.google.common.base.Preconditions;

/**
 * Represents a region on screen, where something is being drawn.
 */
public class PhysicalViewport {

    public final int x1, y1, x2, y2;
    private final Point ul, ur, ll, lr;
    private final Edge top, bottom, left, right;

    public PhysicalViewport(final int x1, final int y1, final int x2, final int y2) {
        Preconditions.checkArgument(x2 > x1, "Viewport must have non-zero width");
        Preconditions.checkArgument(y2 > y1, "Viewport must have non-zero height");
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.ul = new Point(x1, y1);
        this.ur = new Point(x2, y1);
        this.ll = new Point(x1, y2);
        this.lr = new Point(x2, y2);

        this.top = new Edge(ul, ur);
        this.bottom = new Edge(ll, lr);
        this.left = new Edge(ul, ll);
        this.right = new Edge(ur, lr);
    }

    public boolean contains(final int x, final int y) {
        return x1 <= x && x < x2 && y1 <= y && y < y2;
    }

    public boolean contains(final Point p) { return contains(p.x, p.y); }

    public Point boundaryIntersection(final Point p1, final Point p2) {
        Point i = Edge.intersection(ul, ur, p1, p2);
        if(i != null) { return i; }
        i = Edge.intersection(ll, lr, p1, p2);
        if(i != null) { return i; }
        i = Edge.intersection(ul, ll, p1, p2);
        if(i != null) { return i; }
        i = Edge.intersection(ur, lr, p1, p2);
        return i;
    }

    public Point boundaryIntersection(final Edge e) {
        Point i = top.intersection(e);
        if(i != null) { return i; }
        i = bottom.intersection(e);
        if(i != null) { return i; }
        i = left.intersection(e);
        if(i != null) { return i; }
        i = right.intersection(e);
        return i;
    }

    public int width() { return x2 - x1; }

    public int height() { return y2 - y1; }

    public Point project(final LogicalViewport logical, final Point p) {
        return new Point(
            p.x(p.x, logical, this),
            p.y(p.y, logical, this));
    }

    public Function<Point, Point> projection(final LogicalViewport logical) {
        return point -> new Point(
            point.x(point.x, logical, this),
            point.y(point.y, logical, this));
    }
}
