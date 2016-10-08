package tdanford.maps;

import java.awt.*;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * An oriented Triangle.
 *
 * Triangle stores an <i>ordered</i> set of three points, and represents them as a
 * Triangle.  The ordering is important because it determines the "direction" (clockwise
 * or counter-clockwise) of the triangle and therefore the sign of its area, it's
 * equality with other triangles, it's ordering with respect to other triangles, etc.
 *
 * Triangle also provides a basic set of operators (area2, left, and circleCenter) on
 * groups of three points.
 */
public class Triangle implements Comparable<Triangle>, GeometricConnector {

    public final Point p1, p2, p3;

    public Triangle(final Point p1, final Point p2, final Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public String toString() { return String.format("<%s : %s : %s>", p1, p2, p3); }

    public int hashCode() { return Objects.hash(p1, p2, p3); }

    public boolean equals(Object o) {
        if(!(o instanceof Triangle)) { return false; }
        Triangle e = (Triangle) o;
        return Objects.equals(p1, e.p1) && Objects.equals(p2, e.p2) && Objects.equals(p3, e.p3);
    }

    public static Point circleCenter(final Point a, final Point b, final Point c) {
        int D = 2 * (a.y * c.x + b.y * a.x - b.y * c.x - a.y * b.x - c.y * a.x + c.y * b.x);
        int ax2 = a.x * a.x, ay2 = a.y * a.y;
        int bx2 = b.x * b.x, by2 = b.y * b.y;
        int cx2 = c.x * c.x, cy2 = c.y * c.y;

        int x = (b.y * ax2 - c.y * ax2 - by2 * a.y + cy2 * a.y + bx2 * c.y + ay2 * b.y +
            cx2 * a.y - cy2 * b.y - cx2 * b.y - bx2 * a.y + by2 * c.y - ay2 * c.y) / D;

        int y = (ax2 * c.x + ay2 * c.x + bx2 * a.x - bx2 * c.x + by2 * a.x - by2 * c.x
            - ax2 * b.x - ay2 * b.x - cx2 * a.x + cx2 * b.x - cy2 * a.x + cy2 * b.x) / D;

        return new Point(x, y);
    }

    public Point circleCenter() {
        return circleCenter(p1, p2, p3);
    }

    public static int area2(final Point a, final Point b, final Point c) {
        return a.x * b.y - a.y * b.x +
            a.y * c.x - a.x * c.y +
            b.x * c.y - c.x * b.y;
    }

    public static boolean collinear(final Point a, final Point b, final Point c) { return area2(a, b, c) == 0; }

    public int area2() {
        return area2(p1, p2, p3);
    }

    public boolean collinear() { return area2() == 0; }

    public Stream<Edge> edges() { return Stream.of(new Edge(p1, p2), new Edge(p2, p3), new Edge(p3, p1)); }

    /**
     * Given an edge in the triangle, this returns the Point of the Triangle which is <i>not</i> a
     * part of the edge.  Note that the ordering of the points in the Edge does not need to reflect
     * the ordering of the points in the Triangle.
     *
     * @param edge And edge containing of the three points in the triangle
     * @return The third point (if the Edge given is not a part of the triangle, then this method returns
     *         an undefined point of the Triangle).
     */
    public Point oppositePoint(Edge edge) {
        if(p1.equals(edge.p1)) {
            if(p2.equals(edge.p2)) {
                return p3;
            } else {
                return p2;
            }
        } else if (p1.equals(edge.p2)) {
            if(p2.equals(edge.p1)) {
                return p3;
            } else {
                return p2;
            }
        } else {
            return p1;
        }
    }

    @Override
    public int compareTo(Triangle o) {
        int c = p1.compareTo(o.p1);
        if(c != 0) { return c; }
        c = p2.compareTo(o.p2);
        if(c != 0) { return c; }
        return p3.compareTo(o.p3);
    }

    @Override
    public Point[] connected() {
        return new Point[] { p1, p2, p3 };
    }

    @Override
    public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
        new Aggregate(new Edge(p1, p2), new Edge(p2, p3), new Edge(p3, p1)).paint(g, logical, physical, label);
    }
}
