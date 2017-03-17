package polylines;


import java.util.List;
import java.util.StringJoiner;

public class PolyLineExt extends PolyLine {

    public PolyLineExt() { super(); }
    public PolyLineExt(List<Point> pts) { super(pts); }
    public PolyLineExt(PolyLine polyline) { super(polyline); }

    public boolean isInRectangle(Point top_left, Point right_bottom) {
        for(Point p : this) {
            if(p.x() < top_left.x() || right_bottom.x() < p.x()) {
                return false;
            }
            if(p.y() < right_bottom.y() || top_left.y() < p.y()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        for (Point p : this) {
            joiner.add(p.toString());
        }
        return "[" + joiner.toString() + "]";
    }

}
