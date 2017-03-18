package polylines.io;


import polylines.PolyLine;

import java.io.IOException;

public interface IPolyLineWriter {

    void writeLine(PolyLine pl) throws IOException;

}
