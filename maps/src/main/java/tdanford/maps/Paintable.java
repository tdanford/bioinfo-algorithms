package tdanford.maps;

import java.awt.*;

public interface Paintable {
    void paint(Graphics2D g, int maxX, int maxY, int x1, int y1, int x2, int y2);
    void regenerate();

    class WithColor implements Paintable {
        private Color color;
        private Paintable paintable;

        public WithColor(Color c, Paintable p) {
            this.color = c;
            this.paintable = p;
        }

        @Override
        public void paint(Graphics2D g, int maxX, int maxY, int x1, int y1, int x2, int y2) {
            Color c = g.getColor();
            g.setColor(color);
            paintable.paint(g, maxX, maxY, x1, y1, x2, y2);
            g.setColor(c);
        }

        @Override
        public void regenerate() {
            // max the regeneration
        }
    }
}
