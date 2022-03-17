package utils;

import graph.Graph;
import graph.component.Edge;
import graph.component.Vertex;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Parser {
    public static void parse(InputStream inputStream, Graph graph) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String line;
        while ((line = in.readLine()) != null) {

            line = line.replaceAll("\\s", "");

            String[] tokens = line.split("@");
            String[] vertexName = tokens[0].split("--");

            if (tokens.length == 2 && vertexName.length == 2) {
                String edgeName = tokens[1];
                graph.addEdge(new Edge(new Vertex(vertexName[0]), new Vertex(vertexName[1]), edgeName));
            }
            if (vertexName.length == 1) {
                graph.addAndGetVertexNumber(new Vertex(vertexName[0]));
            }

        }
    }
}
