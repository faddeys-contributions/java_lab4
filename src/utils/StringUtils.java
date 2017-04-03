package utils;

public class StringUtils {

    public static String[] rpartition(String string, String delimiter) {
        int idx = string.lastIndexOf(delimiter);
        if (idx < 0) {
            return new String[]{"", string};
        } else {
            return new String[]{
                    string.substring(0, idx),
                    string.substring(idx+delimiter.length())
            };
        }
    }

}
