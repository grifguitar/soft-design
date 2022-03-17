package drawing_api.style;

import drawing_api.DrawingApi;
import drawing_api.component.Point;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class AwtDrawingApi extends Frame implements DrawingApi {
    private static final List<Shape> objects = new ArrayList<>();

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setPaint(new Color(1.0F, 0.98039216F, 0.8039216F));
        g.fillRect(0, 0, DrawingApi.getWidth(), DrawingApi.getHeight());
        for (Shape shape : objects) {
            if (shape instanceof Ellipse2D) {
                g.setPaint(Color.BLUE);
                g.fill(shape);
                continue;
            }
            if (shape instanceof Line2D) {
                g.setPaint(Color.RED);
                g.draw(shape);
                continue;
            }
            g.setPaint(Color.BLACK);
            g.draw(shape);
        }
    }

    @Override
    public void drawCircle(Point<Double> point, double r) {
        objects.add(new Ellipse2D.Double(point.x - r, point.y - r + DrawingApi.SHIFT, 2 * r, 2 * r));
    }

    @Override
    public void drawLine(Point<Double> from, Point<Double> to) {
        objects.add(new Line2D.Double(from.x, from.y + DrawingApi.SHIFT, to.x, to.y + DrawingApi.SHIFT));
    }

    @Override
    public void run() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        this.setSize(DrawingApi.getWidth(), DrawingApi.getHeight());
        this.setTitle(DrawingApi.TITLE);
        this.setVisible(true);
    }
}
