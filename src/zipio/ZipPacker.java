package zipio;


import utils.IOUtils;

import java.io.*;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipPacker implements AutoCloseable {
    private ZipOutputStream zipOut;

    public ZipPacker(String destinationFile) throws IOException {
        this(new FileOutputStream(destinationFile));
    }

    public ZipPacker(OutputStream output) {
        this.zipOut = new ZipOutputStream(output);
    }

    public void write(File fileToPack) throws IOException {
        write(fileToPack, (File f) -> true);
    }

    public void write(File fileToPack,
                      Function<File, Boolean> callback) throws IOException {
        write(fileToPack, callback, "");
    }

    public void write(File fileToPack,
                      Function<File, Boolean> callback,
                      String path) throws IOException {
        if (fileToPack.isDirectory()) {
            path += fileToPack.getName() + File.separator;
            for (File f : fileToPack.listFiles()) {
                write(f, callback, path);
            }
        } else {
            if (!callback.apply(fileToPack)) return;
            zipOut.putNextEntry(new ZipEntry(path+ fileToPack.getName()));
            IOUtils.pipeStreams(
                    new FileInputStream(fileToPack),
                    zipOut);
        }
    }

    @Override
    public void close() throws IOException {
        zipOut.close();
    }
}
