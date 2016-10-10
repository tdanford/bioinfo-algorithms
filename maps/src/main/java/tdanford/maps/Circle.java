package tdanford.maps;

import java.awt.*;

public class Circle implements GeometricPrimitive {

    private final Point center;
    private final int radius;

    public Circle(final Point center, final int radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Finds a circle from three points -- the circle (possibly degenerate) passes
     * through all three points, and its center is the "circumcenter" of the three
     * points.)
     *
     * @param p1 First point that the circle must pass through.
     * @param p2 Second point that the circle must pass through.
     * @param p3 Third point that the circle must pass through.
     */
    public Circle(final Point p1, final Point p2, final Point p3) {
        this.center = Triangle.circleCenter(p1, p2, p3);
        this.radius = (int)Math.round(Math.sqrt(center.distance(p1)));
    }

    public Point center() { return center; }
    public int radius() { return radius; }

    @Override
    public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
        final Point pc = physical.project(logical, center);
        final Point pw = physical.project(logical, new Point(center.x + radius, center.y));
        final Point ph = physical.project(logical, new Point(center.x, center.y - radius));

        int w = pw.x - pc.x, h = ph .y - pc.y;

        g.drawOval(pc.x - w, pc.y - h, 2 * w, 2 * h);
    }
}
