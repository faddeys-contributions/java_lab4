package polylines;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PolyLine implements Iterable<Point>, Serializable {

    protected final List<Point> points;


    public PolyLine() {
        this(new ArrayList<>());
    }

    public PolyLine(List<Point> points) {
        this.points = new ArrayList<>(points);
    }

    public PolyLine(PolyLine polyline) {
        this(polyline.points);
    }

    public void addPoint(int position, Point point) {
        points.add(position, point);
    }

    public void appendPoint(Point point) {
        points.add(point);
    }

    public void removePoint(int position) {
        points.remove(position);
    }

    public double length() {
        double result = 0;
        for (int i = 1; i < points.size(); i++) {
            result += points.get(i).distance(points.get(i-1));
        }
        return result;
    }

    public boolean equals(Object other) {
        return other instanceof PolyLine && equals((PolyLine)other);
    }

    public boolean equals(PolyLine other) {
        if (points.size() != other.points.size()) return false;
        for(int i = 0; i < points.size(); i++) {
            if (!points.get(i).equals(other.points.get(i))) return false;
        }
        return true;
    }

    public Object clone() {
        return new PolyLine(this);
    }

    public String toString() {
        if (points.size() == 0) {
            return "Empty line";
        }
        Point first = points.get(0);
        Point last = points.get(points.size()-1);
        return "Line from " + first.toString() + " to " + last.toString();
    }

    public int hashCode() {
        int code = super.hashCode();
        for (Point p : this) {
            code ^= p.hashCode();
        }
        return code;
    }

    public Iterator<Point> iterator() {
        return points.iterator();
    }
}
