package polylines;

import java.io.Serializable;


public class Point implements Serializable {

    private final int _x;
    private final int _y;

    public Point(int x, int y) {
        this._x = x;
        this._y = y;
    }

    public double distance(Point p) {
        return Math.sqrt(x()*p.x() + y()*p.y());
    }

    public static double distance(Point p1, Point p2) {
        return p1.distance(p2);
    }

    public int x() { return _x; }
    public int y() { return _y; }

    public boolean equals(Object other) {
        return other instanceof Point && equals((Point) other);
    }

    public boolean equals(Point other) {
        return x() == other.x() && y() == other.y();
    }


    public String toString() {
        return "(" + Integer.toString(x()) + ", " + Integer.toString(y()) + ")";
    }

    public int hashCode() {
        return Integer.hashCode(x()) ^ Integer.hashCode(y());
    }

}
