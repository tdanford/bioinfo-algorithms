package tdanford.maps;

import java.awt.*;

public class Rasterization implements Paintable {

    private final Voronoi voronoi;

    public Rasterization(Voronoi voronoi) {
        this.voronoi = voronoi;
    }

    @Override
    public void regenerate() {

    }

    @Override
    public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {

    }
}
