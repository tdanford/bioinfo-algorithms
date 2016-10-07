package tdanford.maps;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Point implements Comparable<Point>, GeometricPrimitive {

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

    public static int squaredDistance(final int x1, final int y1, final int x2, final int y2) {
        final int dx = x2 - x1, dy = y2 - y1;
        return dx * dx + dy * dy;
    }

    public static int distance(final int x1, final int y1, final int x2, final int y2) {
        return (int)Math.round(Math.sqrt((double)squaredDistance(x1, y1, x2, y2)));
    }

    public int squaredDistance(final Point p) {
        return squaredDistance(x, y, p.x, p.y);
    }

    public int distance(final Point p) {
        return distance(x, y, p.x, p.y);
    }

    @Override
    public void paint(final Graphics2D g, final LogicalViewport logical, final PhysicalViewport physical, final String label) {
        int diam = RADIUS * 2;
        int xc = x(x, logical, physical), yc = y(y, logical, physical);
        g.fillOval(xc-RADIUS, yc-RADIUS, diam, diam);

        if(label != null) {
            g.drawString(label, xc + RADIUS, yc - RADIUS);
        }
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
