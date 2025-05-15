package src.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReceiptTemplateLoader {
    public static String loadTemplate(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }
}
