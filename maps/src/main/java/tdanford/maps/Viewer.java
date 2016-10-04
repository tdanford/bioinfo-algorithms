package tdanford.maps;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;

public class Viewer extends JFrame {

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

    public Action regenerateAction() {
        return new AbstractAction("Regenerate") {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(final Paintable p : paintables) {
                    SwingUtilities.invokeLater(() -> {
                        p.regenerate();
                        drawingPanel.repaint();
                    });
                }
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

            int x1 = 100, y1 = 100, x2 = w - x1, y2 = h - y1;

            g.setColor(Color.black);
            g.drawRect(x1, y1, x2-x1, y2-y1);

            for(Paintable p : paintables) {
                p.paint((Graphics2D)g, maxX, maxY, x1, y1, x2, y2);
            }
        }
    }
}
