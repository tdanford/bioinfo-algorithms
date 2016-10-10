package tdanford.maps;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public interface GeometricPrimitive {

    void paint(final Graphics2D g, final LogicalViewport logical, final PhysicalViewport physical, final String label);

    default void paint(final Graphics2D g, final LogicalViewport logical, final PhysicalViewport physical) {
        paint(g, logical, physical, null);
    }

    default int x(final int x, final LogicalViewport logical, final PhysicalViewport physical) {
        double xf = (double)x / (double)logical.width;
        return physical.x1 + (int)Math.round(xf * (double)physical.width());
    }

    default int y(final int y, final LogicalViewport logical, final PhysicalViewport physical) {
        double yf = (double)y / (double)logical.height;
        return physical.y1 + (int)Math.round(yf * (double)physical.height());
    }

    default void drawPoint(final Graphics2D g, final Point p, final int radius, final String label) {
        final int diam = radius * 2;
        g.fillOval(p.x-radius, p.y-radius, diam, diam);
        if(label != null) {
            g.drawString(label, p.x+radius, p.y-radius);
        }
    }

    default void drawEdge(final Graphics2D g, final Edge e) {
        g.drawLine(e.p1.x, e.p1.y, e.p2.x, e.p2.y);
    }

    class Aggregate implements GeometricPrimitive {

        private final Collection<? extends GeometricPrimitive> primitives;

        public Aggregate(GeometricPrimitive... gps) {
            this(Arrays.asList(gps));
        }

        public Aggregate(final Collection<? extends GeometricPrimitive> prims) {
            this.primitives = prims;
        }

        @Override
        public void paint(final Graphics2D g, final LogicalViewport logical, final PhysicalViewport physical, final String label) {
            for(GeometricPrimitive prim : primitives) {
                prim.paint(g, logical, physical);  // paint with no labels.
            }
        }
    }
}
