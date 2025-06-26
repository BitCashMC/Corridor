package gg.bitcash.corridor;

public class CorridorUtils {

    private CorridorUtils() {}

    /**
     *
     * @param str String to check for integer parsability
     * @return whether or not the inputted String can be parsed without yielding a NumberFormatException.
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
