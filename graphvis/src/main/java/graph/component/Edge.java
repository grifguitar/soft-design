package graph.component;

public record Edge(Vertex from, Vertex to, String name) {
    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public String getName() {
        return name;
    }
}
