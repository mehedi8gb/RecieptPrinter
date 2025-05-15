package src.printer;

import javax.print.*;
import java.io.*;
import java.nio.file.Files;

public class PrintServiceManager {
    public void printHtml(String html) {
        try {
            // Use temp file
            File tempFile = File.createTempFile("receipt", ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(html);
            }

            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            DocPrintJob job = service.createPrintJob();

// Read existing print data (receipt bytes)
            byte[] receiptBytes = Files.readAllBytes(tempFile.toPath());

// Append cut command
            byte[] cutPaper = new byte[] { 0x1D, 'V', 66, 0 }; // Full cut
            byte[] fullData = new byte[receiptBytes.length + cutPaper.length];
            System.arraycopy(receiptBytes, 0, fullData, 0, receiptBytes.length);
            System.arraycopy(cutPaper, 0, fullData, receiptBytes.length, cutPaper.length);


            try (InputStream stream = new FileInputStream(tempFile)) {
// Print combined byte stream
            Doc doc = new SimpleDoc(fullData, flavor, null);
            job.print(doc, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

