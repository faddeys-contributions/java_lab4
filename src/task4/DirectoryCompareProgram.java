package task4;


import java.io.File;
import java.util.*;

public class DirectoryCompareProgram {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Command takes exactly two arguments");
            return;
        }
        File dir1 = new File(args[0]),
             dir2 = new File(args[1]);

        if (!dir1.isDirectory()) {
            System.err.println('"' + args[0] + "\" is not an existing directory");
        }
        if (!dir2.isDirectory()) {
            System.err.println('"' + args[1] + "\" is not an existing directory");
        }

        Map<String, File> files1 = getFlattenedDirectory(dir1),
                          files2 = getFlattenedDirectory(dir2);

        Set<String> commonNames = new HashSet<>();
        commonNames.addAll(files1.keySet());
        commonNames.retainAll(files2.keySet());

        Set<String> firstOnlyNames = new HashSet<>();
        firstOnlyNames.addAll(files1.keySet());
        firstOnlyNames.removeAll(commonNames);

        Set<String> secondOnlyNames = new HashSet<>();
        secondOnlyNames.addAll(files2.keySet());
        secondOnlyNames.removeAll(commonNames);

        if (!firstOnlyNames.isEmpty()) {
            System.out.println("Present only in \"" + args[0] + "\":");
            for (String name : firstOnlyNames) {
                System.out.println(name);
            }
            System.out.println();
        }

        if (!secondOnlyNames.isEmpty()) {
            System.out.println("Present only in \"" + args[1] + "\":");
            for (String name : secondOnlyNames) {
                System.out.println(name);
            }
            System.out.println();
        }

        Set<String> similarNames = new HashSet<>(),
                    differentNames = new HashSet<>();
        for(String name : commonNames) {
            boolean areEqual = filesAreEqual(files1.get(name), files2.get(name));
            (areEqual ? similarNames : differentNames).add(name);
        }

        if (!similarNames.isEmpty()) {
            System.out.println("Similar files:");
            for (String name : similarNames) {
                System.out.println(name);
            }
            System.out.println();
        }

        if (!differentNames.isEmpty()) {
            System.out.println("Different files:");
            for (String name : differentNames) {
                System.out.println(name);
            }
        }

    }

    private static boolean filesAreEqual(File file1, File file2) {
        long dateStamp1 = file1.lastModified() / 86400000;
        long dateStamp2 = file2.lastModified() / 86400000;
        if (dateStamp1 != dateStamp2) return false;
        return (file1.length() == file2.length());
    }

    private static Map<String, File> getFlattenedDirectory(File directory) {
        File files[] = directory.listFiles();
        if (files == null) {
            // this should never happen
            throw new IllegalArgumentException("Argument is not an existing directory");
        }
        Map<String, File> result = new HashMap<>();
        for (File file : files) {
            String name = file.getName();
            if (file.isDirectory()) {
                for(Map.Entry<String, File> item : getFlattenedDirectory(file).entrySet() ) {
                    result.put(joinPath(name, item.getKey()), item.getValue());
                }
            } else {
                result.put(name, file);
            }
        }
        return result;
    }

    private static String joinPath(String... names) {
        return joinPath(Arrays.asList(names));
    }
    private static String joinPath(Collection<String> names) {
        StringJoiner joiner = new StringJoiner(File.separator);
        for(String name : names) {
            joiner.add(name);
        }
        return joiner.toString();
    }

}
