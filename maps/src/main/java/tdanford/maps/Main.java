package tdanford.maps;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.awt.*;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static tdanford.maps.Paintable.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("main({})", Stream.of(args).collect(joining(" ")));

        final int N = 100;
        final int maxX = 1000, maxY = 1000;
        LogicalViewport logical = new LogicalViewport(maxX, maxY);
        Supplier<Collection<Point>> pointSupplier =
            () -> IntStream.range(0, N).mapToObj(i -> logical.randomPoint()).collect(toList());

        final Sites sites = new Sites(pointSupplier);
        final Delaunay delaunay = new Delaunay(sites);
        final Voronoi voronoi = new Voronoi(delaunay);
        final Graph graph = new Graph(voronoi);

        final Viewer v = new Viewer(maxX, maxY);
        v.addPaintable(new WithColor(Color.red, sites));
        //v.addPaintable(new WithColor(Color.green, new ConvexHull(sites)));
        v.addPaintable(new Invisible(delaunay));
        //v.addPaintable(new Paintable.WithColor(new Color(0, 0, 255, 50), delaunay));
        v.addPaintable(new Invisible(voronoi));
        v.addPaintable(new WithColor(Color.black, graph));

        v.regenerate();

        v.makeVisible();
    }
}
