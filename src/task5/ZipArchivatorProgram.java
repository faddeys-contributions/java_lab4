package task5;


import utils.StringUtils;
import zipio.ZipExtractor;
import zipio.ZipPacker;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipArchivatorProgram {

    enum Action {
        PACK, EXTRACT, LIST;

        static Action map(String shortcut) {
            switch (shortcut) {
                case "p": return PACK;
                case "x": return EXTRACT;
                case "l": return LIST;
                default: return null;
            }
        }
    }

    static class Arguments {
        final Action action;
        final String source, destination;

        private Arguments(Action action, String source, String destination) {
            this.action = action;
            this.source = source;
            this.destination = destination;
        }

        static Arguments parse(String argv[]) {
            if (argv.length != 2 && argv.length != 3) {
                return null;
            }
            Action action = Action.map(argv[0]);
            if (action == null) return null;
            String source = argv[1];
            String destination = argv.length == 3 ? argv[2] : null;
            if (destination == null) {
                if (Action.EXTRACT.equals(action)) {
                    destination = "";
                } else if (Action.PACK.equals(action)) {
                    String absSource = new File(source).getAbsolutePath();
                    if (absSource.charAt(absSource.length()-1) == File.separatorChar) {
                        absSource = absSource.substring(0, absSource.length()-1);
                    }
                    destination = absSource + ".zip";
                }
            }
            return new Arguments(action, source, destination);
        }

        static String getUsageText() {
            return "Usage:\n"
                    + "packing: <program> p <file-or-dir> [destination-file]\n"
                    + "extracting: <program> x <archive> [destination-file]\n"
                    + "listing: <program> l <archive>";
        }
    }

    public static void main(String argv[]) {
        Arguments args = Arguments.parse(argv);
        if (args == null) {
            System.out.println(Arguments.getUsageText());
            return;
        }
        try {
            if (Action.EXTRACT.equals(args.action)) {
                ZipExtractor extractor = new ZipExtractor(new File(args.source));
                extractor.extract(args.destination, (String file) -> {
                    System.out.println(file);
                    return null;
                });
                extractor.close();
            } else if (Action.PACK.equals(args.action)) {
                ZipPacker packer = new ZipPacker(
                        new BufferedOutputStream(new FileOutputStream(args.destination))
                );
                packer.write(
                    new File(args.source),
                    (File f) -> {
                        String relPath = f.getPath().substring(
                                args.source.length()
                        );
                        System.out.println(relPath);
                        return true;
                    }
                );
                packer.close();
            } else if (Action.LIST.equals(args.action)) {
                ZipInputStream zip = new ZipInputStream(
                        new FileInputStream(args.source)
                );
                ZipEntry entry;
                while((entry = zip.getNextEntry()) != null) {
                    System.out.println(entry.getName());
                }
                zip.close();
            }
        } catch (IOException exc) {
            exc.printStackTrace();
            System.out.println("Error: " + exc.getMessage());
        }
    }

}
