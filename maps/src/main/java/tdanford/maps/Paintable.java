package tdanford.maps;

import java.awt.*;

public interface Paintable extends GeometricPrimitive {

    void regenerate();

    /**
     * Lifts a GeometricPrimitive into a Paintable
     */
    class Geometric implements Paintable {
        private final GeometricPrimitive primitive;
        public Geometric(final GeometricPrimitive prim) {
            this.primitive = prim;
        }

        @Override
        public void regenerate() {
        }

        @Override
        public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
            primitive.paint(g, logical, physical, label);
        }
    }

    /**
     * Wraps a Paintable, but doesn't call the internal regenerate() method on the Paintable.
     */
    class NoRegenerate implements Paintable {
        private Paintable paintable;
        public NoRegenerate(Paintable p) { this.paintable = p;}

        @Override
        public void regenerate() {
        }

        @Override
        public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
            paintable.paint(g, logical, physical, label);
        }
    }

    class Invisible implements Paintable {

        private final Paintable inner;

        public Invisible(final Paintable p) {
            this.inner = p;
        }

        @Override
        public void regenerate() {
            inner.regenerate();
        }

        @Override
        public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
            // do nothing
        }
    }

    /**
     * Draws a Paintable with a particular color
     */
    class WithColor implements Paintable {
        private Color color;
        private Paintable paintable;

        public WithColor(Color c, Paintable p) {
            this.color = c;
            this.paintable = p;
        }

        @Override
        public void regenerate() {
            paintable.regenerate();
        }

        @Override
        public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
            Color c = g.getColor();
            g.setColor(color);
            paintable.paint(g, logical, physical, label);
            g.setColor(c);
        }
    }
}
