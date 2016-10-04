package tdanford.maps;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public class Sites implements Paintable, Supplier<Collection<Point>> {

    private final ArrayList<Point> sites;
    private final Supplier<Collection<Point>> siteGenerator;

    public Sites(Supplier<Collection<Point>> siteGenerator) {
        this.siteGenerator = siteGenerator;
        this.sites = new ArrayList<>();
    }

    @Override
    public void paint(Graphics2D g, int maxX, int maxY, int x1, int y1, int x2, int y2) {
        int w = x2 - x1, h = y2 - y1;
        int i = 0;
        for(Point p : sites) {
            p.paint(g, maxX, maxY, x1, y1, w, h, String.valueOf(i++));
        }
    }

    @Override
    public void regenerate() {
        sites.clear();
        sites.addAll(siteGenerator.get());
    }

    @Override
    public Collection<Point> get() {
        return sites;
    }
}
