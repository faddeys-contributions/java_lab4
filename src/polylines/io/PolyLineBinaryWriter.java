package polylines.io;


import polylines.Point;
import polylines.PolyLine;
import polylines.PolyLineExt;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PolyLineBinaryWriter implements IPolyLineWriter {

    private DataOutputStream writer;

    public PolyLineBinaryWriter(OutputStream stream) {
        writer = new DataOutputStream(stream);
    }

    @Override
    public void writeLine(PolyLine pl) throws IOException {
        writer.writeBytes("PLB");
        writer.writeBoolean(pl instanceof PolyLineExt);
        writer.writeInt(pl.size());
        for(Point p : pl) {
            writer.writeInt(p.x());
            writer.writeInt(p.y());
        }
    }
}
