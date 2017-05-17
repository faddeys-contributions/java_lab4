package polylines;


import polylines.io.*;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LinesConverterProgram {

    private static final Map<String, Function<InputStream, IPolyLineReader>> READERS;
    private static final Map<String, Function<OutputStream, IPolyLineWriter>> WRITERS;
    static {
        READERS = new HashMap<>();
        READERS.put("csv", PolyLineCSVReader::new);
        READERS.put("bin", PolyLineBinaryReader::new);
        READERS.put("java", PolyLineObjectReader::new);

        WRITERS = new HashMap<>();
        WRITERS.put("csv", PolyLineCSVWriter::new);
        WRITERS.put("bin", PolyLineBinaryWriter::new);
        WRITERS.put("java", PolyLineObjectWriter::new);
    }

    public static void main(String[] argv) {
        if (argv.length != 3 || !WRITERS.containsKey(argv[2])) {
            log("Arguments: <input-file> <output-file> <format>");
            log("Format must be one of \"csv\", \"bin\" or \"java\"");
            return;
        }
        InputStream inputStream;
        try{
            inputStream = new FileInputStream(argv[0]);
        } catch (FileNotFoundException exc) {
            log("Input file not found");
            return;
        }
        inputStream = new BufferedInputStream(inputStream);

        OutputStream outputStream;
        try{
            outputStream = new FileOutputStream(argv[1]);
        } catch (FileNotFoundException exc) {
            log("Cannot open output file");
            return;
        }

        List<PolyLine> lines = new LinkedList<>();
        inputStream.mark(100);
        IPolyLineReader reader = null;
        for(Map.Entry<String, Function<InputStream, IPolyLineReader>> e : READERS.entrySet()) {
            reader = e.getValue().apply(inputStream);
            try {
                lines.add(reader.readLine());
            } catch (IOException exc) {
                try{ inputStream.reset(); } catch (IOException ignored) {}
                continue;
            }
            log("Detected input format: " + e.getKey());
        }
        if (reader == null || lines.size() == 0) {
            log("Cannot detect input file format");
            return;
        }

        while(true) {
            try {
                lines.add(reader.readLine());
            } catch (IOException exc) {
                break;
            }
        }

        log("Got poly-lines:");
        for(PolyLine line : lines)
            log(line.toString());

        IPolyLineWriter writer = WRITERS.get(argv[2]).apply(outputStream);
        try {
            for(PolyLine line : lines)
                writer.writeLine(line);
            outputStream.flush();
            outputStream.close();
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
