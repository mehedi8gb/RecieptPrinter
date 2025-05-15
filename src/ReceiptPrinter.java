package src;

import java.io.*;
import java.util.Locale;
import javax.print.*;


public class ReceiptPrinter {

public static void printRawBytes(byte[] data, String printerName) throws Exception {
    DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
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
    Doc doc = new SimpleDoc(data, flavor, null);
    job.print(doc, null);
}

    public static void printReceipt(String orderId) throws Exception {
        // 1. Build ESC/POS byte array (simple receipt content)
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Initialize printer
        output.write(new byte[]{0x1B, 0x40});  // ESC @

        // Centered, bold, double height text: "Receipt"
        output.write(new byte[]{0x1B, 0x61, 0x01}); // ESC a 1 (center)
        output.write(new byte[]{0x1B, 0x45, 0x01}); // ESC E 1 (bold on)
        output.write(new byte[]{0x1B, 0x21, 0x30}); // ESC ! 48 (double height+width)
        output.write("Receipt\n".getBytes("UTF-8"));

        // Reset styles and left align
        output.write(new byte[]{0x1B, 0x21, 0x00}); // ESC ! 0 (normal)
        output.write(new byte[]{0x1B, 0x45, 0x00}); // ESC E 0 (bold off)
        output.write(new byte[]{0x1B, 0x61, 0x00}); // ESC a 0 (left)

        // Print order info
        output.write(("Order ID: " + orderId + "\n").getBytes("UTF-8"));
        output.write("Item 1          1  x $10.00\n".getBytes("UTF-8"));
        output.write("Item 2          2  x $15.00\n".getBytes("UTF-8"));
        output.write("-----------------------------\n".getBytes("UTF-8"));
        output.write("Total:         $40.00\n\n".getBytes("UTF-8"));

        // Print date/time (simple example)
        output.write(("Date: " + java.time.LocalDateTime.now().toString() + "\n\n").getBytes("UTF-8"));

        // Feed some lines and cut paper
        output.write(new byte[]{0x1B, 0x64, 0x05}); // ESC d 5 (feed 5 lines)
        output.write(new byte[]{0x1D, 0x56, 0x01}); // GS V 1 (partial cut)

        byte[] escposData = output.toByteArray();

        printRawBytes(escposData, "RONGTA 80mm Series Printer");
    }

    // For testing
    public static void main(String[] args) throws Exception {
        printReceipt("12345");

    }
}
