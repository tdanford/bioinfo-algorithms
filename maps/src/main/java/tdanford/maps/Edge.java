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

    public static Point intersection(final Point a, final Point b, final Point c, final Point d) {
        if(!intersect(a, b, c, d)) {
            return null;
        } else {
            final int xb = b.x - a.x, yb = b.y - a.y;
            final int xd = d.x - c.x, yd = d.y - c.y;
            final int xhat = a.x - c.x, yhat = a.y - c.y;

            if(xb == 0) {
                // it's a vertical line
                if(xd == 0) {
                    return null;
                } else {
                    final double t = (double)xhat / (double)xd;
                    return new Point(a.x, c.y + (int)Math.round(t * yd));
                }

            } else if(yb == 0) {
                // it's a horizontal line
                if(yd == 0) {
                    return null;
                } else {
                    final double t = (double)yhat / (double)yd;
                    return new Point(c.x + (int)Math.round(t * xd), a.y);
                }

            } else {
                final double xb_yb = (double)xb / (double)yb;
                final double t2 = (xhat - yhat * xb_yb) / (xb_yb * yd - xd);

                return new Point(c.x + (int)Math.round(t2 * xd), c.y + (int)Math.round(t2 * yd));
            }
        }
    }

    public static boolean left(final Point a, final Point b, final Point c) {
        return area2(a, b, c) > 0;
    }

    public static boolean intersectProp(final Point a, final Point b, final Point c, final Point d) {
        if(collinear(a, b, c) ||
            collinear(a, b, d) ||
            collinear(c, d, a) ||
            collinear(c, d, b)) {
            return false;
        }

        return xor(left(a, b, c), left(a, b, d)) &&
            xor(left(c, d, a), left(c, d, b));
    }

    public static boolean between(final Point a, final Point b, final Point c) {
        if(!collinear(a, b, c)) { return false; }

        if(a.x != b.x) {
            return ((a.x <= c.x) && (c.x <= b.x)) ||
                ((a.x >= c.x) && (c.x >= b.x));
        } else {
            return ((a.y <= c.y) && (c.y <= b.y)) ||
                ((a.y >= c.y) && (c.y >= b.y));
        }
    }

    public Point intersection(final Edge e) {
        return intersection(p1, p2, e.p1, e.p2);
    }

    public static boolean intersect(final Point a, final Point b, final Point c, final Point d) {
        if(intersectProp(a, b, c, d)) {
            return true;
        } else if(between(a, b, c) || between(a, b, d) || between(c, d, a) || between(c, d, b)) {
            return true;
        } else {
            return false;
        }
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
        return intersectProp(a, b, c, d);
    }

    public boolean between(final Point p) {
        return between(p1, p2, p);
    }

    public boolean intersect(final Edge e) {
        return intersect(p1, p2, e.p1, e.p2);
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

