package task3;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.util.Date;


public class DirectoryCopyProgram {

    static private long nowTS = System.currentTimeMillis();

    static class Arguments {
        @NotNull
        final File inputDir;

        @NotNull
        final File outputDir;

        final OutputStream log;

        final int nDays;

        Arguments(String[] args) throws IllegalArgumentException {

            if (args.length < 2) {
                throw new IllegalArgumentException(
                        "Missing arguments: input dir and output dir"
                );
            }
            this.inputDir = new File(args[0]);
            if (!this.inputDir.isDirectory()) {
                throw new IllegalArgumentException("Input file is not a directory");
            }

            this.outputDir = new File(args[1]);
            if (this.outputDir.exists() && !this.outputDir.isDirectory()) {
                throw new IllegalArgumentException("Output file is not a directory");
            }

            if (args.length == 2) {
                this.log = null;
                this.nDays = 3;
            } else {
                if (args.length != 4 && args.length != 6) {
                    throw new IllegalArgumentException(
                            "Wrong number of arguments"
                    );
                }
                int pos = 2;
                OutputStream log = null;
                Integer nDays = null;
                while (pos < args.length) {
                    if ("--log".equals(args[pos])) {
                        if (log != null) {
                            throw new IllegalArgumentException("Log file defined twice");
                        }
                        try {
                            log = new FileOutputStream(args[pos + 1]);
                        } catch (FileNotFoundException exc) {
                            throw new IllegalArgumentException("Log file cannot be opened");
                        }
                        pos += 2;
                        continue;
                    }
                    if ("--ndays".equals(args[pos])) {
                        if (nDays != null) {
                            throw new IllegalArgumentException("N days defined twice");
                        }
                        try {
                            nDays = Integer.parseInt(args[pos+1]);
                        } catch (NumberFormatException nfe) {
                            throw new IllegalArgumentException("N days is not an integer");
                        }
                        pos += 2;
                    }
                }
                if (nDays == null) {
                    nDays = 3;
                }

                this.log = log;
                this.nDays = nDays;
            }
        }

    }

    private static boolean wasModified(File file, int nDays) {
        long modifiedTS = file.lastModified();
        // 86400000 is a number of milliseconds in day
        return nowTS - modifiedTS < nDays*86400000;
    }

    public static void main(String strArgs[]) {
        Arguments args;
        try {
            args = new Arguments(strArgs);
        } catch (IllegalArgumentException exc) {
            System.err.println(exc.getMessage());
            return;
        }

        PrintStream output;
        if (args.log == null) {
            output = System.out;
        } else {
            output = new PrintStream(new BroadcastingOutputStream(
                    System.out, args.log
            ));
        }

        if (!args.outputDir.exists()) {
            if (!args.outputDir.mkdirs()) {
                System.err.println("Cannot create oupt directory");
                return;
            }
        }
        File filesToCopy[] = args.inputDir.listFiles();
        for(File file : filesToCopy) {
            if (!wasModified(file, args.nDays)) continue;
            if (file.isDirectory()) {
                output.println("Skipping directory: " + file.getName());
                continue;
            }
            try(
                    InputStream source = new BufferedInputStream(new FileInputStream(file));
                    OutputStream destination = new FileOutputStream(
                            args.outputDir.getAbsolutePath()
                            + File.separator + file.getName())
            ) {

                byte buffer[] = new byte[1024];
                int nBytes;
                while(true) {
                    nBytes = source.read(buffer);
                    if (nBytes < 0) break;
                    destination.write(buffer, 0, nBytes);
                }
                output.println("Copied file: " + file.getName());
            } catch (FileNotFoundException exc) {
                output.println("Cannot copy file: " + file.getName());
            } catch (IOException exc) {
                output.println("Error while copying file: " + file.getName());
            }
        }
    }

}
