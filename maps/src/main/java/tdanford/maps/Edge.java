package tdanford.maps;

import static tdanford.maps.Triangle.area2;
import static tdanford.maps.Triangle.collinear;
import java.awt.*;
import java.util.Objects;

public class Edge implements Comparable<Edge> {

    public final Point p1, p2;

    public Edge(final Point p1, final Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

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

    public void paint(Graphics2D g, int maxX, int maxY, int x1, int y1, int w, int h) {
        double x1f = (double)p1.x / maxX, y1f = (double)p1.y / maxY;
        double x2f = (double)p2.x / maxX, y2f = (double)p2.y / maxY;
        int px1 = x1 + (int)Math.round(x1f * w);
        int py1 = y1 + (int)Math.round(y1f * h);
        int px2 = x1 + (int)Math.round(x2f * w);
        int py2 = y1 + (int)Math.round(y2f * h);
        g.drawLine(px1, py1, px2, py2);
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
}

