package polylines.io;


import polylines.PolyLine;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class PolyLineObjectReader implements IPolyLineReader {

    private ObjectInputStream reader;

    public PolyLineObjectReader(InputStream stream) throws IOException {
        reader = new ObjectInputStream(stream);
    }

    @Override
    public PolyLine readLine() throws IOException {
        try {
            Object obj = reader.readObject();
            if (!(obj instanceof PolyLine)) {
                throw new IOException("Loaded object is not a poly-line");
            }
            return (PolyLine) obj;
        } catch (ClassNotFoundException cnf) {
            throw new IOException(cnf.getMessage());
        }
    }
}
