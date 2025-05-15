package src.service;

import javax.print.*;
import java.io.*;

public class ReceiptPrinterService {

    public static void print(String receiptContent) throws IOException, PrintException {
        // Save to temp file
        File tempFile = File.createTempFile("receipt", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(receiptContent);
            writer.flush();
        }

        // Print job
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob job = service.createPrintJob();

        try (InputStream stream = new FileInputStream(tempFile)) {
            Doc doc = new SimpleDoc(stream, flavor, null);
            job.print(doc, null);
        }

        // Delete temp file (optional)
        tempFile.delete();
    }
}

