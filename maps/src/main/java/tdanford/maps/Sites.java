package tdanford.maps;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sites implements Paintable, Supplier<Collection<Point>>, GeometricPrimitive {

    private static Logger LOG = LoggerFactory.getLogger(Sites.class);

    private final ArrayList<Point> sites;
    private final Supplier<Collection<Point>> siteGenerator;

    public Sites(Supplier<Collection<Point>> siteGenerator) {
        this.siteGenerator = siteGenerator;
        this.sites = new ArrayList<>();
    }

    @Override
    public void regenerate() {
        LOG.info("regenerate()");
        sites.clear();
        sites.addAll(siteGenerator.get());
        LOG.info("Generated {} sites", sites.size());
    }

    @Override
    public Collection<Point> get() {
        return sites;
    }

    @Override
    public void paint(final Graphics2D g, final LogicalViewport logical, final PhysicalViewport physical, final String label) {
        new GeometricPrimitive.Aggregate(sites).paint(g, logical, physical);
    }

    public int size() {
        return sites.size();
    }
}
