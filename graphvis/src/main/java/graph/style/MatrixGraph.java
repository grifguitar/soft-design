package graph.style;

import drawing_api.DrawingApi;
import graph.Graph;
import graph.component.Edge;

import java.util.ArrayList;
import java.util.List;

public class MatrixGraph extends Graph {
    private final List<List<List<String>>> graph;

    public MatrixGraph(DrawingApi drawingApi) {
        super(drawingApi);
        graph = new ArrayList<>();
    }

    @Override
    public void addEdge(Edge edge) {
        int from = addAndGetVertexNumber(edge.getFrom());
        int to = addAndGetVertexNumber(edge.getTo());
        checkAndIncreaseSize(Math.max(from, to));
        graph.get(from).get(to).add(edge.getName());
        graph.get(to).get(from).add(edge.getName());
    }

    @Override
    public List<Edge> getEdges() {
        List<Edge> result = new ArrayList<>();
        for (int i = 0; i < graph.size(); i++) {
            for (int j = i; j < graph.size(); j++) {
                for (String name : graph.get(i).get(j)) {
                    result.add(new Edge(getVertexByNumber(i), getVertexByNumber(j), name));
                }
            }
        }
        return result;
    }

    private void checkAndIncreaseSize(int index) {
        if (graph.size() <= index) {
            for (int i = 0; i < graph.size(); i++) {
                for (int j = graph.size(); j <= index; j++) {
                    graph.get(i).add(new ArrayList<>());
                }
            }
            for (int i = graph.size(); i <= index; i++) {
                List<List<String>> list = new ArrayList<>();
                for (int j = 0; j <= index; j++) {
                    list.add(new ArrayList<>());
                }
                graph.add(list);
            }
        }
    }
}
