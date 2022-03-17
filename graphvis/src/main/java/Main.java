import drawing_api.DrawingApi;
import drawing_api.style.AwtDrawingApi;
import drawing_api.style.JavaFxDrawingApi;
import graph.Graph;
import graph.style.LinkedListGraph;
import graph.style.MatrixGraph;
import utils.Parser;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    private static final String MESSAGE = "expected two command line arguments: --awt/--javafx --list/--matrix";

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new RuntimeException(MESSAGE);
        }

        DrawingApi drawingApi = switch (args[0]) {
            case "--awt" -> new AwtDrawingApi();
            case "--javafx" -> new JavaFxDrawingApi();
            default -> throw new RuntimeException(MESSAGE);
        };

        Graph graph = switch (args[1]) {
            case "--list" -> new LinkedListGraph(drawingApi);
            case "--matrix" -> new MatrixGraph(drawingApi);
            default -> throw new RuntimeException(MESSAGE);
        };

        try {
            Parser.parse(new FileInputStream("src/main/java/graph_input.txt"), graph);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        graph.drawGraph();

        drawingApi.run();
    }
}
