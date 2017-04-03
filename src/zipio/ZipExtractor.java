package zipio;


import utils.IOUtils;

import java.io.*;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor implements AutoCloseable {

    ZipInputStream zip;

    public ZipExtractor(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public ZipExtractor(InputStream stream) {
        zip = new ZipInputStream(stream);
    }

    public void extract(String destinationDirectory,
                        Function<String, Void> callback) throws IOException {
        if (destinationDirectory.charAt(destinationDirectory.length()-1) != File.separatorChar) {
            destinationDirectory += File.separator;
        }
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            ensureDirExists(destinationDirectory, entry);
            callback.apply(entry.getName());
            IOUtils.pipeStreams(zip, new FileOutputStream(
                    destinationDirectory + entry.getName()
            ));
        }
    }

    private void ensureDirExists(String destination, ZipEntry entry) throws IOException {
        String dirName;
        if (entry.isDirectory()) {
            dirName = entry.getName();
        } else {
            dirName = entry.getName();
            int sepIdx = dirName.lastIndexOf(File.separator);
            if (sepIdx >= 0) dirName = dirName.substring(0, sepIdx);
        }
        File dir = new File(destination, dirName);
        if (!dir.exists()) {
            if (!dir.mkdirs()) throw new IOException("Cannot create directory" + dir);
        } else if (!dir.isDirectory()) {
            throw new IOException(dir + "is not a directory");
        }
    }

    @Override
    public void close() throws IOException {
        zip.close();
    }
}
