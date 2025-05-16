package src.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Logger {
    private static final String LOG_FILE = getLogPath();

    private static String getLogPath() {
        String userHome = System.getProperty("user.home");
        String logDirPath = userHome + File.separator + "receipt-printer";
        File logDir = new File(logDirPath);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        return logDirPath + File.separator + "receipt-printer.log";
    }

    private static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static void logInfo(String message) {
        log("[INFO] " + message);
    }

    public static void logError(String message) {
        log("[ERROR] " + message);
    }

    public static void logException(String context, Exception e) {
        log("[EXCEPTION] " + context + ": " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log("  at " + element.toString());
        }
    }

    public static void log(String message) {
        String logEntry = LocalDateTime.now() + " - " + message;
        System.out.println(logEntry); // Optional

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            bw.write(logEntry);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Logger failed: " + e.getMessage());
        }
    }
}
