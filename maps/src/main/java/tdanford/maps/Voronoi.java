package tdanford.maps;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class Voronoi implements Paintable {

    private Delaunay delaunay;
    private ArrayList<Point> points;
    private Set<Edge> edges;

    public Voronoi(Delaunay del) {
        this.delaunay = del;
        this.edges = new HashSet<>();
        this.points = new ArrayList<>();
    }

    @Override
    public void paint(Graphics2D g, int maxX, int maxY, int x1, int y1, int x2, int y2) {
        int w = x2 - x1, h = y2 - y1;
        g.setStroke(new BasicStroke(2.0f));

        for(Edge e : edges) {
            e.paint(g, maxX, maxY, x1, y1, w, h);
        }
        for(Point p : points) {
            p.paint(g, maxX, maxY, x1, y1, w, h);
        }
    }

    @Override
    public void regenerate() {
        System.out.println("Regenerating voronoi");
        points.clear();
        edges.clear();
        delaunay.regenerate();
        rebuildVoronoi();
        System.out.println(String.format("Rebuilt %d points, %d edges in Voronoi", points.size(), edges.size()));
    }

    private void rebuildVoronoi() {
        final Map<Edge, ArrayList<Triangle>> edgeMap = new HashMap<>();
        final Map<Triangle, Point> centers = new HashMap<>();
        final Map<Point, Triangle> tris = new HashMap<>();

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
                        }
                    }
                } else {
                    // add a ray to infinity
                }
            });
        }
    }
}
