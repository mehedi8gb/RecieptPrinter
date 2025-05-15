package src;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Logger {
    // Logger.java addition:
    private static final String LOG_FILE = "D:\\receipt-printer.log";



    private static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static void logInfo(String message) {
        log("INFO" + message);
    }

    public static void logError(String message) {
        log("ERROR" + message);
    }

    public static void logException(String context, Exception e) {
        log("EXCEPTION" + context + ": " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log("TRACE" + "  at " + element.toString());
        }
    }

    public static void log(String message) {
        String logEntry = LocalDateTime.now() + " - " + message;
        System.out.println(logEntry); // Optional: print to console

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            bw.write(logEntry);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }

}

