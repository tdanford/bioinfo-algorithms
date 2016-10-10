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
    private Supplier<Collection<Point>> siteGenerator;

    public Sites(Supplier<Collection<Point>> siteGenerator) {
        this.siteGenerator = siteGenerator;
        this.sites = new ArrayList<>();
    }

    public void chainSiteSupplier(Supplier<Collection<Point>> newGenerator) {
        siteGenerator = new Chain<Point>(newGenerator, siteGenerator);
    }

    public void setToggleSiteSupplier(Supplier<Collection<Point>> newGen) {
        siteGenerator = new Toggling<>(siteGenerator, newGen);
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

    public static class Toggling<T> implements Supplier<T> {

        private boolean first;
        private Supplier<T> firstSupplier, secondSupplier;

        public Toggling(Supplier<T> fs, Supplier<T> ss) {
            firstSupplier = fs;
            secondSupplier = ss;
            first = false;
        }

        @Override
        public T get() {
            first = !first;
            return first ? firstSupplier.get() : secondSupplier.get();
        }
    }

    public static class Chain<T> implements Supplier<Collection<T>> {

        private Supplier<Collection<T>> rest, first;

        public Chain(Supplier<Collection<T>> first, Supplier<Collection<T>> rest) {
            this.first = first;
            this.rest = rest;
        }

        @Override
        public Collection<T> get() {
            Collection<T> f = first.get();
            if(!f.isEmpty()) {
                return f;
            } else {
                return rest.get();
            }
        }
    }
}
