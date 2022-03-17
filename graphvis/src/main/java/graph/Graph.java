package graph;

import drawing_api.DrawingApi;
import drawing_api.component.Point;
import graph.component.Edge;
import graph.component.Vertex;

import java.util.HashMap;
import java.util.List;

public abstract class Graph {
    /**
     * Bridge to drawing api
     */
    protected final DrawingApi drawingApi;

    /**
     * Mapping
     */
    private final HashMap<Vertex, Integer> vertices;
    private final HashMap<Integer, Vertex> numbers;
    private final HashMap<Vertex, Point<Double>> coord;

    public Graph(DrawingApi drawingApi) {
        this.drawingApi = drawingApi;
        this.vertices = new HashMap<>();
        this.numbers = new HashMap<>();
        this.coord = new HashMap<>();
    }

    public abstract void addEdge(Edge edge);

    public abstract List<Edge> getEdges();

    public int addAndGetVertexNumber(Vertex vertex) {
        if (!vertices.containsKey(vertex)) {
            vertices.put(vertex, vertices.size());
        }
        int num = vertices.get(vertex);
        numbers.put(num, vertex);
        return num;
    }

    public Vertex getVertexByNumber(int num) {
        return numbers.get(num);
    }

    public void drawGraph() {
        double r = Math.min(DrawingApi.getWidth(), DrawingApi.getHeight()) / 3.0;
        Point<Double> c = new Point<>(DrawingApi.getWidth() / 2.0, DrawingApi.getHeight() / 2.0);
        double step = 2 * 3.14 / vertices.size();

        vertices.forEach((v, num) -> {
            Point<Double> point = new Point<>(c.x + r * Math.cos(step * num), c.y + r * Math.sin(step * num));
            drawingApi.drawCircle(point, 10.0);
            coord.put(v, point);
        });

        for (Edge edge : getEdges()) {
            drawingApi.drawLine(coord.get(edge.getFrom()), coord.get(edge.getTo()));
        }
    }
}
