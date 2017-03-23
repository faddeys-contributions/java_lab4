package polylines;


import polylines.io.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LinesConverterProgram {

    private static final Map<String, Function<InputStream, IPolyLineReader>> READERS;
    private static final Map<String, Function<OutputStream, IPolyLineWriter>> WRITERS;
    static {
        READERS = new HashMap<>();
        READERS.put("csv", PolyLineCSVReader::new);
//        READERS.put("bin", PolyLineBinaryReader::new);
//        READERS.put("java", PolyLineObjectReader::new);

        WRITERS = new HashMap<>();
        WRITERS.put("csv", PolyLineCSVWriter::new);
        WRITERS.put("bin", PolyLineBinaryWriter::new);
        WRITERS.put("java", PolyLineObjectWriter::new);
    }

    public static void main(String[] argv) {
        if (argv.length != 3 || !WRITERS.containsKey(argv[2])) {
            log("Arguments: <input-file> <output-file> <format>");
            log("Format must be one of \"csv\", \"plb\" or \"java\"");
            return;
        }
        InputStream inputStream;
        try{
            inputStream = new FileInputStream(argv[0]);
        } catch (FileNotFoundException exc) {
            log("Input file not found");
            return;
        }

        OutputStream outputStream;
        try{
            outputStream = new FileOutputStream(argv[1]);
        } catch (FileNotFoundException exc) {
            log("Cannot open output file");
            return;
        }

        PolyLine line = null;
        for(Map.Entry<String, Function<InputStream, IPolyLineReader>> e : READERS.entrySet()) {
            IPolyLineReader reader = e.getValue().apply(inputStream);
            try {
                line = reader.readLine();
            } catch (IOException exc) {
                continue;
            }
            log("Detected input format: " + e.getKey());
        }
        if (line == null) {
            log("Cannot detect input file format");
            return;
        }

        log("Got poly-line: " + line.toString());

        IPolyLineWriter writer = WRITERS.get(argv[2]).apply(outputStream);
        try {
            writer.writeLine(line);
        } catch (IOException exc) {
            log("Got error while writing output file: "+exc.getMessage());
            return;
        }
        log("Done.");

    }

    private static void log(String msg) {
        System.out.println(msg);
    }

}
