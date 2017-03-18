package polylines.io;


import polylines.Point;
import polylines.PolyLine;
import polylines.PolyLineExt;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class PolyLineCSVWriter implements IPolyLineWriter {

    private OutputStreamWriter writer;

    public PolyLineCSVWriter(OutputStream stream) {
        writer = new OutputStreamWriter(stream);
    }

    public void writeLine(PolyLine line) throws IOException{
        writer.write((line instanceof PolyLineExt) ? "e" : "b");
        for (Point p : line) {
            writer.write("," + p.x() + " " + p.y());
        }
        writer.write("\n");
    }

}
