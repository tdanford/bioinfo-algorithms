package tdanford.maps;

import static java.util.stream.Collectors.toList;
import static tdanford.maps.Triangle.area2;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Voronoi implements Paintable, Supplier<Stream<GeometricConnector>> {

    private Delaunay delaunay;
    private ArrayList<Point> points;
    private Map<Point, SortedSet<Point>> cells;
    private Set<Edge> edges;
    private Set<Ray> rays;

    public Voronoi(Delaunay del) {
        this.delaunay = del;
        this.edges = new HashSet<>();
        this.points = new ArrayList<>();
        this.rays = new HashSet<>();
        this.cells = new TreeMap<>();
    }

    @Override
    public Stream<GeometricConnector> get() {
        return Stream.concat(edges.stream(), rays.stream());
    }

    public Polygon cell(final Point p) {
        if(cells.containsKey(p)) {
            return new Polygon(cells.get(p).toArray(new Point[cells.get(p).size()]));
        } else {
            return null;
        }
    }

    public Collection<Point> sites() { return delaunay.sites(); }

    public Collection<Point> smoothedSites() {
        return sites().stream().map(this::cell).map(Polygon::center).collect(toList());
    }

    public Stream<Polygon> cells() {
        return delaunay.sites().stream().map(this::cell);
    }

    @Override
    public void paint(Graphics2D g, final LogicalViewport logical, final PhysicalViewport physical, final String label) {
        //g.setStroke(new BasicStroke(2.0f));

        for(Edge e : edges) {
            e.paint(g, logical, physical);
        }
        for(Ray r : rays) {
            r.paint(g, logical, physical);
        }
        /*
        for(Point p : points) {
            p.paint(g, logical, physical);
        }
        */
    }

    @Override
    public void regenerate() {
        System.out.println("Regenerating voronoi");
        points.clear();
        edges.clear();
        rays.clear();
        cells.clear();
        rebuildVoronoi();
        System.out.println(String.format("Rebuilt %d points, %d edges in Voronoi", points.size(), edges.size()));
    }

    private void rebuildVoronoi() {
        final Map<Edge, ArrayList<Triangle>> edgeMap = new HashMap<>();
        final Map<Triangle, Point> centers = new HashMap<>();
        final Map<Point, Triangle> tris = new HashMap<>();

        for(Point site : delaunay.sites()) {
            cells.put(site, new TreeSet<>(new GrahamPointComparator(site)));
        }

        Collection<Triangle> triangles = delaunay.triangles();
        for(final Triangle t : triangles) {
            t.edges().forEach(e -> {
                Edge en = e.normalizePointOrder();
                if(!edgeMap.containsKey(en)) {
                    edgeMap.put(en, new ArrayList<>(2));
                }
                edgeMap.get(en).add(t);
            });

            Point center = t.circleCenter();
            points.add(center);
            centers.put(t, center);
            tris.put(center, t);
        }

        for(Point center : points) {
            Triangle t = tris.get(center);
            t.edges().forEach(edge -> {
                Edge en = edge.normalizePointOrder();
                if(edgeMap.get(en).size() > 1) {
                    for(Triangle ot : edgeMap.get(en)) {
                        if(!ot.equals(t)) {
                            Point ocenter = centers.get(ot);
                            edges.add(new Edge(center, ocenter));

                            cells.get(edge.p1).add(center);
                            cells.get(edge.p2).add(center);
                            cells.get(edge.p1).add(ocenter);
                            cells.get(edge.p2).add(ocenter);
                        }
                    }
                } else {
                    Point midpoint = en.midpoint();
                    Point otherPointOfTriangle = t.oppositePoint(edge);

                    boolean centerIsInsideDelaunay = true;
                    if(area2(edge.p1, edge.p2, otherPointOfTriangle) >= 0) {
                        centerIsInsideDelaunay = area2(edge.p1, edge.p2, center) >= 0;
                    } else {
                        centerIsInsideDelaunay = area2(edge.p2, edge.p1, center) >= 0;
                    }

                    Ray r = null;
                    if(centerIsInsideDelaunay) {
                        r = new Ray(center, midpoint);
                    } else {
                        r = new Ray(center, midpoint).flip();
                    }
                    rays.add(r);

                    cells.get(edge.p1).add(center);
                    cells.get(edge.p2).add(center);
                    cells.get(edge.p1).add(r.interpolate(1000.0));
                    cells.get(edge.p2).add(r.interpolate(1000.0));

                }
            });
        }
    }

}
