package graph.style;

import drawing_api.DrawingApi;
import graph.Graph;
import graph.component.Edge;

import java.util.ArrayList;
import java.util.List;

public class LinkedListGraph extends Graph {
    private final List<Edge> graph;

    public LinkedListGraph(DrawingApi drawingApi) {
        super(drawingApi);
        graph = new ArrayList<>();
    }

    @Override
    public void addEdge(Edge edge) {
        addAndGetVertexNumber(edge.getFrom());
        addAndGetVertexNumber(edge.getTo());
        graph.add(edge);
    }

    @Override
    public List<Edge> getEdges() {
        return graph;
    }
}
