package tdanford.maps;

import static java.util.stream.Collectors.toList;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Calculates and stores the Delaunay triangulation of a set of points.
 */
public class Delaunay implements Paintable {

    private final Sites siteSupplier;
    private final ArrayList<Point> sites;

    private final ArrayList<IndexedTriangle> triangles;
    private final Map<Point, Set<Edge>> edges;

    public Delaunay(Sites siteSupplier) {
        this.siteSupplier = siteSupplier;
        this.sites = new ArrayList<>();
        this.edges = new HashMap<>();
        this.triangles = new ArrayList<>();
    }

    public void setNewSites(Collection<Point> newSites) {
        sites.clear();
        triangles.clear();
        edges.clear();
        sites.addAll(newSites);
        calculate();
    }

    public void paint(Graphics2D g, int mx, int my, int x1, int y1, int x2, int y2) {
        int w = x2 - x1, h = y2 - y1;
        g.setStroke(new BasicStroke(2.0f));

        for(IndexedTriangle t : triangles) {
            drawEdge(g, t.i, t.j, mx, my, x1, y1, w, h);
            drawEdge(g, t.j, t.k, mx, my, x1, y1, w, h);
            drawEdge(g, t.k, t.i, mx, my, x1, y1, w, h);

            drawSite(g, t.i, mx, my, x1, y1, w, h);
            drawSite(g, t.j, mx, my, x1, y1, w, h);
            drawSite(g, t.k, mx, my, x1, y1, w, h);
        }
    }

    @Override
    public void regenerate() {
        siteSupplier.regenerate();
        setNewSites(siteSupplier.get());
    }

    private void drawSite(Graphics2D g, int i, int mx, int my, int x1, int y1, int w, int h) {
        Point p1 = sites.get(i);
        int radius = 3;
        int diam = radius * 2;

        int p1x = x1 + (int)Math.round((double)w * (double)p1.x / (double)mx);
        int p1y = y1 + (int)Math.round((double)h * (double)p1.y / (double)my);

        g.fillOval(p1x-radius, p1y-radius, diam, diam);
        g.drawString(String.valueOf(i), p1x + radius, p1y - radius);
    }

    private void drawEdge(Graphics2D g, int i, int j, int mx, int my, int x1, int y1, int w, int h) {
        Point p1 = sites.get(i), p2 = sites.get(j);

        int p1x = x1 + (int)Math.round((double)w * (double)p1.x / (double)mx);
        int p1y = y1 + (int)Math.round((double)h * (double)p1.y / (double)my);

        int p2x = x1 + (int)Math.round((double)w * (double)p2.x / (double)mx);
        int p2y = y1 + (int)Math.round((double)h * (double)p2.y / (double)my);

        g.drawLine(p1x, p1y, p2x, p2y);
    }

    /**
     * Simple O(n^4) algorithm, implementation based on pg. 202 in Computational Geometry in C
     */
    public void calculate() {
        //System.out.println("\n--------------\n");
        int N = sites.size();
        int[] z = new int[N];
        int[] x = new int[N], y = new int[N];

        for(int i = 0; i < N; i++) {
            x[i] = sites.get(i).x;
            y[i] = sites.get(i).y;
            z[i] = x[i] * x[i] + y[i] * y[i];
        }

        for(int i = 0; i < N-2; i++) {
            for(int j = i + 1; j < N; j++) {
                for(int k = i + 1; k < N; k++) {

                    boolean flag = false;
                    if(j != k) {
                        double xn = (y[j] - y[i]) * (z[k] - z[i]) - (y[k] - y[i]) * (z[j] - z[i]);
                        double yn = (x[k] - x[i]) * (z[j] - z[i]) - (x[j] - x[i]) * (z[k] - z[i]);
                        double zn = (x[j] - x[i]) * (y[k] - y[i]) - (x[k] - x[i]) * (y[j] - y[i]);

                        flag = zn < 0;
                        if(flag) {
                            for(int m = 0; flag && m < N; m++) {
                                boolean norm = ((x[m] - x[i]) * xn + (y[m] - y[i]) * yn + (z[m] - z[i]) * zn <= 0);
                                flag = flag && norm;
                            }
                        }

                        if(flag) {
                            triangles.add(new IndexedTriangle(i, j, k));
                            //System.out.println(String.format("* %d, %d, %d", i, j, k));
                            addEdge(i, j);
                            addEdge(j, i);
                            addEdge(j, k);
                            addEdge(k, j);
                            addEdge(i, k);
                            addEdge(k, i);
                        }
                    }

                }
            }
        }
    }

    private void addEdge(final int i, final int j) {
        if(!edges.containsKey(sites.get(i))) {
            edges.put(sites.get(i), new HashSet<>());
        }
        edges.get(sites.get(i)).add(new Edge(sites.get(i), sites.get(j)));
    }

    private static class IndexedTriangle {
        public final int i, j, k;
        public IndexedTriangle(final int i, final int j, final int k) {
            this.i = i;
            this.j = j;
            this.k = k;
        }

        public int hashCode() { return Objects.hash(i, j, k); }

        public boolean equals(Object o) {
            if(!(o instanceof IndexedTriangle)) { return false; }
            IndexedTriangle t = (IndexedTriangle)o;
            return Objects.equals(i, t.i) && Objects.equals(j, t.j) && Objects.equals(k, t.k);
        }
    }

    public Collection<Triangle> triangles() {
        return triangles.stream().map(t ->
            new Triangle(sites.get(t.i), sites.get(t.j), sites.get(t.k))).collect(toList());
    }

    public int N() { return sites.size(); }
    public Point site(final int i) { return sites.get(i); }
    public Collection<Point> sites() { return sites; }
    public Collection<Edge> edges(Point p) { return edges.get(p); }
    public Stream<Edge> allEdges() { return edges.entrySet().stream().flatMap(e -> e.getValue().stream()); }
}
