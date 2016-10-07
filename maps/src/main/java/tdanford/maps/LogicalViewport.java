package tdanford.maps;

import java.util.Random;

/**
 * A width/height combination, which dicates the region (starting at the origin) that should be visible
 * in any particular PhysicalViewport.
 *
 * Basically, this determines the (relative) scale of the X and Y axes in whatever viewport we're
 * drawing things in.
 *
 * Also allows easy random generation of points, edges, triangles, etc.
 */
public class LogicalViewport {

    private static final Random random = new Random();

    public final int width, height;

    public LogicalViewport(final int w, final int h) {
        this.width = w;
        this.height = h;
    }

    public Point randomPoint() { return randomPoint(random); }

    public Point randomPoint(Random r) {
        return new Point(r.nextInt(width), r.nextInt(height));
    }

    public Edge randomEdge() { return randomEdge(random); }

    public Edge randomEdge(Random r) {
        return new Edge(randomPoint(r), randomPoint(r));
    }

    public Triangle randomTriangle() { return randomTriangle(random); }

    public Triangle randomTriangle(Random r) {
        return new Triangle(randomPoint(r), randomPoint(r), randomPoint(r));
    }
}
