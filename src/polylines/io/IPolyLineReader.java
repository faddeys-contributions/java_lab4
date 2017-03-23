package polylines.io;


import polylines.PolyLine;

import java.io.IOException;

public interface IPolyLineReader {

    PolyLine readLine() throws IOException;

}
