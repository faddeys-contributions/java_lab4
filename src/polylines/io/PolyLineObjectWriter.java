package polylines.io;

import polylines.PolyLine;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class PolyLineObjectWriter implements IPolyLineWriter {

    private ObjectOutputStream writer;

    public PolyLineObjectWriter(OutputStream stream) throws IOException {
        writer = new ObjectOutputStream(stream);
    }

    @Override
    public void writeLine(PolyLine pl) throws IOException {
        writer.writeObject(pl);
    }
}
