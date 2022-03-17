package drawing_api.style;

import drawing_api.DrawingApi;
import drawing_api.component.Point;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class JavaFxDrawingApi extends Application implements DrawingApi {
    private static final List<Node> objects = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        Group group = new Group(objects);
        stage.setTitle(DrawingApi.TITLE);
        Scene scene = new Scene(
                group, DrawingApi.getWidth(), DrawingApi.getHeight() - DrawingApi.SHIFT, Color.LEMONCHIFFON
        );
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void drawCircle(Point<Double> point, double r) {
        Circle circle = new Circle(point.x, point.y, r);
        circle.setFill(Color.BLUE);
        objects.add(circle);
    }

    @Override
    public void drawLine(Point<Double> from, Point<Double> to) {
        Line line = new Line(from.x, from.y, to.x, to.y);
        line.setStroke(Color.RED);
        objects.add(line);
    }

    @Override
    public void run() {
        launch();
    }
}
