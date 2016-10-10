package tdanford.maps;

import java.awt.*;
import java.util.Arrays;

public class Polygon implements GeometricConnector {

    public final Point[] points;

    public Polygon(Point... ps) {
        points = ps.clone();
    }

    public Point[] hull() {
        return new GrahamScan(points).hull().toArray(new Point[0]);
    }

    public Point center() {
        double fraction = 1.0 / (double)points.length;
        int x = 0, y = 0;

        for(Point p : points) {
            x += (int)Math.round(fraction * p.x);
            y += (int)Math.round(fraction * p.y);
        }

        return new Point(x, y);
    }

    @Override
    public Point[] connected() {
        return points;
    }

    @Override
    public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
        for(int i = 0; i < points.length; i++) {
            int j = (i + 1) % points.length;
            new Edge(points[i], points[j]).paint(g, logical, physical);
        }
    }
}
