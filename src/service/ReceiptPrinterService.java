package src.service;

import javax.print.*;
import java.io.*;
import java.util.Locale;

public class ReceiptPrinterService {

    // ESC/POS cut command: 1D 56 41 00
    private static final byte[] CUT_COMMAND = new byte[]{0x1D, 0x56, 0x41, 0x00};

    public static void print(String receiptContent) throws IOException, PrintException {
        // Save receipt content to temp file
        File tempFile = File.createTempFile("receipt", ".txt");
        try (FileOutputStream fos = new FileOutputStream(tempFile);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {

            writer.write(receiptContent);
            writer.flush();
            fos.write(CUT_COMMAND); // Append cut command
        }

        // Send to printer
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob job = service.createPrintJob();

        try (InputStream stream = new FileInputStream(tempFile)) {
            Doc doc = new SimpleDoc(stream, flavor, null);
            job.print(doc, null);
        }

        // Cleanup
        tempFile.delete();
    }

    public static void printTo(String printerName, String receiptContent) throws IOException, PrintException {
        // Save receipt content to temp file
        File tempFile = File.createTempFile("receipt", ".txt");
        try (FileOutputStream fos = new FileOutputStream(tempFile);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {

            writer.write(receiptContent);
            writer.flush();
            fos.write(CUT_COMMAND); // Append cut command
        }

        // Send to printer
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, null);

        PrintService printer = null;
        for (PrintService service : services) {
            if (service.getName().toLowerCase(Locale.ROOT).contains(printerName.toLowerCase(Locale.ROOT))) {
                printer = service;
                break;
            }
        }
        if (printer == null) {
            throw new RuntimeException("Printer not found: " + printerName);
        }
        DocPrintJob job = printer.createPrintJob();

        try (InputStream stream = new FileInputStream(tempFile)) {
            Doc doc = new SimpleDoc(stream, flavor, null);
            job.print(doc, null);
        }

        // Cleanup
        tempFile.delete();
    }
}


