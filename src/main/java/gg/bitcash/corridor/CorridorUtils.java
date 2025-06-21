package gg.bitcash.corridor;

public class CorridorUtils {

    private CorridorUtils() {}

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
