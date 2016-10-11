package tdanford.maps;

import java.awt.*;

public class FortuneVoronoi {
}

class ScanLineItem {

    public ScanLineItem left, right;

    public ScanLineItem() {
        this.left = this.right = null;
    }

    public ScanLineItem(final ScanLineItem left, final ScanLineItem right) {
        this.left = left;
        this.right = right;
    }

    public void remove() {
        left.right = right;
        right.left = left;
        this.left = this.right = null;
    }

    public void insertAfter(final ScanLineItem prev) {
        final ScanLineItem next = prev.right;
        left = prev;
        right = next;
        if(left != null) { left.right = this; }
        if(right != null) { right.left = this; }
    }

    public void insertBefore(final ScanLineItem next) {
        final ScanLineItem prev = next.left;
        left = prev;
        right = next;
        if(left != null) { left.right = this; }
        if(right != null) { right.left = this; }
    }
}

class Parabola extends ScanLineItem implements GeometricPrimitive {

    public final Point site;

    public Parabola(final Point site) {
        this.site = site;
    }

    @Override
    public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
        drawPoint(g, physical.project(logical, site), 3, null);

    }
}
