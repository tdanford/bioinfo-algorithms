package tdanford.maps;

import static java.util.stream.Collectors.toList;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

        final int N = 200;
        final int maxX = 1000, maxY = 1000;
        final Sites sites = new Sites(() -> IntStream.range(0, N).mapToObj(i -> Point.random(maxX, maxY)).collect(toList()));
        final Voronoi voronoi = new Voronoi(new Delaunay(sites));
        voronoi.regenerate();

        final Viewer v = new Viewer(maxX, maxY);
        v.addPaintable(voronoi);
        v.addPaintable(new Paintable.WithColor(Color.red, sites));

        v.makeVisible();
    }
}
