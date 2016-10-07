package tdanford.maps;

import com.google.common.base.Preconditions;

/**
 * Represents a region on screen, where something is being drawn.
 */
public class PhysicalViewport {

    public final int x1, y1, x2, y2;

    public PhysicalViewport(final int x1, final int y1, final int x2, final int y2) {
        Preconditions.checkArgument(x2 > x1, "Viewport must have non-zero width");
        Preconditions.checkArgument(y2 > y1, "Viewport must have non-zero height");
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public boolean contains(final int x, final int y) {
        return x1 <= x && x < x2 && y1 <= y && y < y2;
    }

    public int width() { return x2 - x1; }

    public int height() { return y2 - y1; }
}
