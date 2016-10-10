package tdanford.maps;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Viewer extends JFrame {

    private static final Logger LOG = LoggerFactory.getLogger(Viewer.class);

    private final ArrayList<Paintable> paintables;
    private final DrawingPanel drawingPanel;
    private final int maxX, maxY;

    public Viewer(final int mx, final int my) {
        super("Viweer");
        this.maxX = mx;
        this.maxY = my;

        paintables = new ArrayList<>();
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(drawingPanel = new DrawingPanel(maxX, maxY), BorderLayout.CENTER);

        JButton button = new JButton(regenerateAction());
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(button);
        c.add(panel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void regenerate() {
        LOG.info("regenerate() : {} paintables", paintables.size());
        for(final Paintable p : paintables) {
            p.regenerate();
        }
        drawingPanel.repaint();
    }

    public Action regenerateAction() {
        return new AbstractAction("Regenerate") {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    regenerate();
                });
            }
        };
    }

    public void addPaintable(Paintable p) {
        paintables.add(p);
        drawingPanel.addPaintable(p);
        drawingPanel.repaint();
    }

    public void makeVisible() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            pack();
        });
    }

    private static class DrawingPanel extends JPanel {

        private ArrayList<Paintable> paintables;
        private final int maxX, maxY;

        public DrawingPanel(final int mx, final int my) {
            super();
            this.maxX = mx;
            this.maxY = my;
            paintables = new ArrayList<>();
            setPreferredSize(new Dimension(600, 600));
        }

        public void addPaintable(Paintable p) { paintables.add(p); }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension d = getSize();
            int w = d.width, h = d.height;
            g.setColor(Color.white);
            g.fillRect(0, 0, w, h);

            int boundary = 25;

            int x1 = boundary, y1 = boundary, x2 = w - boundary, y2 = h - boundary;
            g.setClip(x1, y1, x2-x1, y2-y1);

            g.setColor(Color.black);
            g.drawRect(x1, y1, x2-x1, y2-y1);

            final PhysicalViewport physical = new PhysicalViewport(x1, y1, x2, y2);
            final LogicalViewport logical = new LogicalViewport(maxX, maxY);

            for(Paintable p : paintables) {
                p.paint((Graphics2D)g, logical, physical);
            }
        }
    }
}
