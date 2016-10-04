package tdanford.maps;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Point implements Comparable<Point> {

    public static final int RADIUS = 3;

    private static final Random rand = new Random();

    public static Point random(final Random r, final int maxX, final int maxY) {
        int x = r.nextInt(maxX), y = r.nextInt(maxY);
        return new Point(x, y);
    }

    public static Point random(final int maxX, final int maxY) {
        return random(rand, maxX, maxY);
    }

    public final int x, y;

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public void paint(Graphics2D g, int maxX, int maxY, int x1, int y1, int w, int h) {
        int diam = RADIUS * 2;
        double xf = (double)x / maxX, yf = (double)y / maxY;
        int x = x1 + (int)Math.round(xf * w);
        int y = y1 + (int)Math.round(yf * h);
        g.fillOval(x-RADIUS, y-RADIUS, diam, diam);
    }

    public String toString() { return String.format("%d,%d", x, y); }

    public int hashCode() {
        return Objects.hash(x, y);
    }

    public boolean equals(Object o) {
        if(!(o instanceof Point)) { return false; }
        Point p = (Point) o;
        return Objects.equals(x, p.x) && Objects.equals(y, p.y);
    }

    @Override
    public int compareTo(Point o) {
        if(x < o.x) { return -1; }
        if(x > o.x) { return 1; }
        if(y < o.y) { return -1; }
        if(y > o.y) { return 1; }
        return 0;
    }
}
