package tdanford.maps;

import java.awt.*;
import java.util.ArrayList;
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

    class Aggregate implements GeometricPrimitive {

        private final Collection<? extends GeometricPrimitive> primitives;

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
