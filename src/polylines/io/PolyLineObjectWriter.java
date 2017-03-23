package polylines.io;

import polylines.PolyLine;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class PolyLineObjectWriter implements IPolyLineWriter {

    private ObjectOutputStream writer = null;
    private OutputStream _passed_stream;

    public PolyLineObjectWriter(OutputStream stream) {
        _passed_stream = stream;
    }

    @Override
    public void writeLine(PolyLine pl) throws IOException {
        if (writer == null) {
            writer = new ObjectOutputStream(_passed_stream);
        }
        writer.writeObject(pl);
    }
}
