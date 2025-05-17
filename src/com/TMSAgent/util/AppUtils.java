package src.com.TMSAgent.util;

public final class AppUtils {

    private AppUtils() {
        // Prevent instantiation
    }

    public static String formatItemName(String name, int maxLength) {
        if (name.length() > maxLength) {
            return name.substring(0, maxLength - 3) + ".";
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


public static String padLeft(String text, int length) {
    return " ".repeat(Math.max(0, length - text.length())) + text;
}

public static String padRight(String text, int length) {
    return text + " ".repeat(Math.max(0, length - text.length()));
}

public static String trimToLength(String text, int maxLength) {
    return text.length() > maxLength ? text.substring(0, maxLength) : text;
}


}
