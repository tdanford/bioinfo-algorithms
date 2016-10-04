package tdanford.maps;

import static tdanford.maps.Triangle.area2;
import static tdanford.maps.Triangle.collinear;
import java.awt.*;
import java.util.Objects;

public class Ray implements Comparable<Ray> {

    public final Point p1, p2;

    public Ray(final Point p1, final Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

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

    public void paint(Graphics2D g, int maxX, int maxY, int x1, int y1, int w, int h) {
        double x1f = (double)p1.x / maxX, y1f = (double)p1.y / maxY;
        double x2f = (double)p2.x / maxX, y2f = (double)p2.y / maxY;
        int px1 = x1 + (int)Math.round(x1f * w);
        int py1 = y1 + (int)Math.round(y1f * h);
        int px2 = x1 + (int)Math.round(x2f * w);
        int py2 = y1 + (int)Math.round(y2f * h);

        int dx = px2 - px1, dy = py2 - py1;
        if(dx == 0 && dy == 0) {
            return;
        }
        while((x1 <= px2 && px2 <= (x1 + w)) && (y1 <= py2 && py2 <= (y1 + h))) {
            dx *= 2;
            dy *= 2;
            px2 += dx;
            py2 += dy;
        }

        g.drawLine(px1, py1, px2, py2);
    }
}

