package drawing_api.component;

public class Point<T> {
    public T x;
    public T y;

    public Point(T x, T y) {
        this.x = x;
        this.y = y;
    }
}
