package utils;


import java.io.*;
import java.util.function.Function;

public class IOUtils {

    public static void copyFile(File source, String destination) throws IOException {
        try(InputStream input = new BufferedInputStream(new FileInputStream(source));
            OutputStream output = new FileOutputStream(destination)
        ) {
            pipeStreams(input, output);
        }
    }

    public static boolean copyFile(File source, String destination,
                                   Function<IOException, Void> handler) {
        try {
            copyFile(source, destination);
        } catch (IOException exc) {
            handler.apply(exc);
            return false;
        }
        return true;
    }

    public static void pipeStreams(InputStream input, OutputStream output) throws IOException {
        byte buffer[] = new byte[1024];
        int nBytes;
        while(true) {
            nBytes = input.read(buffer);
            if (nBytes < 0) break;
            output.write(buffer, 0, nBytes);
        }
    }

}
