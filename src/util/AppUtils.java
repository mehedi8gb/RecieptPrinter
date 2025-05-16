package src.util;

public final class AppUtils {

    private AppUtils() {
        // Prevent instantiation
    }

    public static String formatItemName(String name, int maxLength) {
        if (name.length() > maxLength) {
            return name.substring(0, maxLength - 3) + "…";
        }
        return name;
    }

    public static String getCurrentTimeHMS() {
        return java.time.LocalTime.now().withNano(0).toString();
    }

    public static String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }

    public static String maskString(String input, int visibleChars) {
        if (input == null || input.length() <= visibleChars) return input;
        return "*".repeat(input.length() - visibleChars) + input.substring(input.length() - visibleChars);
    }

    // Add more global utils here...
}
