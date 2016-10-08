package tdanford.maps;

import static tdanford.maps.Triangle.area2;
import static tdanford.maps.Triangle.collinear;
import java.awt.*;
import java.util.Objects;

public class Ray implements Comparable<Ray>, GeometricConnector {

    public final Point p1, p2;

    public Ray(final Point p1, final Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point[] connected() { return new Point[] { p1 }; }

    public Ray flip() {
        int dx = p2.x - p1.x, dy = p2.y - p1.y;
        return new Ray(p1, new Point(p1.x - dx, p1.y - dy));
    }

    public String toString() { return String.format("%s -> %s", p1, p2); }

    public int hashCode() { return Objects.hash(p1, p2); }

    public boolean equals(Object o) {
        if(!(o instanceof Ray)) { return false; }
        Ray e = (Ray) o;
        return Objects.equals(p1, e.p1) && Objects.equals(p2, e.p2);
    }

    public int compareTo(Ray e) {
        int c = p1.compareTo(e.p1);
        if(c != 0) { return c; }
        return p2.compareTo(e.p2);
    }

    @Override
    public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
        int px1 = x(p1.x, logical, physical), py1 = y(p1.y, logical, physical);
        int px2 = x(p2.x, logical, physical), py2 = y(p2.y, logical, physical);

        int dx = px2 - px1, dy = py2 - py1;
        if(dx == 0 && dy == 0) {
            return;
        }

        while(physical.contains(px2, py2)) {
            //dx *= 2;
            //dy *= 2;
            px2 += dx;
            py2 += dy;
        }

        g.drawLine(px1, py1, px2, py2);
    }
}

