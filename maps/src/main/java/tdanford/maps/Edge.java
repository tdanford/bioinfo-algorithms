package tdanford.maps;

import static tdanford.maps.Triangle.area2;
import static tdanford.maps.Triangle.collinear;
import java.awt.*;
import java.util.Objects;

public class Edge implements Comparable<Edge>, GeometricConnector {

    public final Point p1, p2;

    public Edge(final Point p1, final Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point[] connected() { return new Point[] { p1, p2 }; }

    public String toString() { return String.format("%s -> %s", p1, p2); }

    public int hashCode() { return Objects.hash(p1, p2); }

    public boolean equals(Object o) {
        if(!(o instanceof Edge)) { return false; }
        Edge e = (Edge) o;
        return Objects.equals(p1, e.p1) && Objects.equals(p2, e.p2);
    }

    public int compareTo(Edge e) {
        int c = p1.compareTo(e.p1);
        if(c != 0) { return c; }
        return p2.compareTo(e.p2);
    }

    public Edge normalizePointOrder() {
        if(p1.compareTo(p2) <= 0) { return this; }
        return new Edge(p2, p1);
    }

    public static boolean left(final Point a, final Point b, final Point c) {
        return area2(a, b, c) > 0;
    }

    public boolean left(Point c) {
        return left(p1, p2, c);
    }

    public boolean leftOn(Point c) {
        return area2(p1, p2, c) >= 0;
    }

    private static boolean xor(boolean a, boolean b) {
        return (a || b) && !(a && b);
    }

    public boolean intersectProp(final Edge e) {
        final Point a = p1, b = p2, c = e.p1, d = e.p2;
        if(collinear(a, b, c) ||
            collinear(a, b, d) ||
            collinear(c, d, a) ||
            collinear(c, d, b)) {
            return false;
        }

        return xor(left(a, b, c), left(a, b, d)) &&
            xor(left(c, d, a), left(c, d, b));
    }

    public Point midpoint() {
        int x = (p1.x + p2.x) / 2;
        int y = (p1.y + p2.y) / 2;
        return new Point(x, y);
    }

    @Override
    public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
        int px1 = x(p1.x, logical, physical), py1 = y(p1.y, logical, physical);
        int px2 = x(p2.x, logical, physical), py2 = y(p2.y, logical, physical);
        g.drawLine(px1, py1, px2, py2);
    }
}

