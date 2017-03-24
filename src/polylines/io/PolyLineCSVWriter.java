package polylines.io;


import polylines.Point;
import polylines.PolyLine;
import polylines.PolyLineExt;

import java.io.*;

public class PolyLineCSVWriter implements IPolyLineWriter {

    private DataOutput writer;

    public PolyLineCSVWriter(OutputStream stream) {
        writer = new DataOutputStream(stream);
    }

    public void writeLine(PolyLine line) throws IOException{
        writer.writeBytes((line instanceof PolyLineExt) ? "e" : "b");
        for (Point p : line) {
            writer.writeBytes("," + p.x() + " " + p.y());
        }
        writer.writeBytes("\n");
    }

}
