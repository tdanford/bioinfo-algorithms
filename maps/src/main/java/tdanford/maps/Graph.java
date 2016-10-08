package tdanford.maps;

import java.awt.Graphics2D;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Graph implements Paintable {

    private Supplier<Stream<GeometricConnector>> edgeSupplier;

    private Set<Point> nodes;
    private Set<GeometricConnector> edges;
    private Map<Point, List<GeometricConnector>> nodeEdges;

    public Graph(Supplier<Stream<GeometricConnector>> es) {
        this.edges = new TreeSet<>();
        this.nodes = new TreeSet<>();
        this.nodeEdges = new TreeMap<>();

        this.edgeSupplier = es;
    }

    @Override
    public void regenerate() {
        edges.clear();
        nodes.clear();
        nodeEdges.clear();

        Stream<GeometricConnector> es = edgeSupplier.get();

        es.forEach(edge -> {
            for(Point p : edge.connected()) {
                nodes.add(p);
                if(!nodeEdges.containsKey(p)) {
                    nodeEdges.put(p, new ArrayList<>());
                }
                nodeEdges.get(p).add(edge);
            }
        });
    }

    @Override
    public void paint(Graphics2D g, LogicalViewport logical, PhysicalViewport physical, String label) {
        for(Point p : nodeEdges.keySet()) {
            for(GeometricConnector e : nodeEdges.get(p)) {
                Point first = e.connected()[0];
                if(first.equals(p)) {   // this ensures that we only draw each edge once.
                    e.paint(g, logical, physical);
                }
            }
        }
    }
}
