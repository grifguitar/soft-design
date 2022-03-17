package drawing_api;

import drawing_api.component.Point;

public interface DrawingApi {
    int SHIFT = 50;

    String TITLE = "Graph Visualizer by Grigoriy Khlytin";

    static int getWidth() {
        return 800;
    }

    static int getHeight() {
        return 800;
    }

    void drawCircle(Point<Double> point, double r);

    void drawLine(Point<Double> from, Point<Double> to);

    void run();
}
