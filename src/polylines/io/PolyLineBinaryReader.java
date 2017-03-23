package polylines.io;


import polylines.Point;
import polylines.PolyLine;
import polylines.PolyLineExt;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class PolyLineBinaryReader implements IPolyLineReader {

    DataInputStream reader;

    public PolyLineBinaryReader(InputStream stream) {
        reader = new DataInputStream(stream);
    }

    @Override
    public PolyLine readLine() throws IOException {
        checkSignature();
        boolean isExt = reader.readBoolean();
        int nPoints = reader.readInt();
        PolyLine line;
        if (isExt) {
            line = new PolyLineExt();
        } else {
            line = new PolyLine();
        }
        for (int i = 0; i < nPoints; i++) {
            line.appendPoint(readPoint());
        }
        return line;
    }

    private void checkSignature() throws IOException {
        char signature[] = {
                (char) reader.readByte(),
                (char) reader.readByte(),
                (char) reader.readByte()
        };
        if (!Arrays.equals(signature, "PLB".toCharArray())) {
            throw new IOException("Not a poly-line binary file");
        }
    }

    private Point readPoint() throws IOException {
        return new Point(
                reader.readInt(),
                reader.readInt()
        );
    }

}
