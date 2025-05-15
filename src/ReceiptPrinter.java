package src;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import javax.print.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

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

    // Parse HTML and convert to ESC/POS bytes
    public static byte[] convertHtmlToEscPos(String html) throws Exception {
        Document doc = Jsoup.parse(html);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // ESC @ = Initialize
        output.write(new byte[]{0x1B, 0x40});

        Element body = doc.body();
        processNodes(body.childNodes(), output);

        // Feed ONLY 1 line, then cut (no excessive waste)
        output.write("\n".getBytes(StandardCharsets.UTF_8)); // Minimal feed
        output.write(new byte[]{0x1D, 0x56, 0x01});          // Partial cut

        return output.toByteArray();
    }


    // Recursively handle nodes with minimal formatting support (bold, center, linebreak)
    private static void processNodes(java.util.List<Node> nodes, ByteArrayOutputStream output) throws Exception {
        for (Node node : nodes) {
            if (node instanceof TextNode textNode) {
                String text = textNode.text().trim();
                if (!text.isEmpty()) {
                    output.write(text.getBytes(StandardCharsets.UTF_8));
                }
            } else if (node instanceof Element el) {
                switch (el.tagName()) {
                    case "b", "strong" -> {
                        output.write(new byte[]{0x1B, 0x45, 0x01}); // Bold ON
                        processNodes(el.childNodes(), output);
                        output.write(new byte[]{0x1B, 0x45, 0x00}); // Bold OFF
                    }
                    case "center" -> {
                        output.write(new byte[]{0x1B, 0x61, 0x01}); // Center
                        processNodes(el.childNodes(), output);
                        output.write(new byte[]{0x1B, 0x61, 0x00}); // Left
                    }
                    case "br" -> output.write("\n".getBytes(StandardCharsets.UTF_8));
                    case "h1", "h2", "h3" -> {
                        output.write(new byte[]{0x1B, 0x21, 0x30}); // Double width + height
                        output.write(new byte[]{0x1B, 0x45, 0x01}); // Bold ON
                        processNodes(el.childNodes(), output);
                        output.write(new byte[]{0x1B, 0x45, 0x00}); // Bold OFF
                        output.write(new byte[]{0x1B, 0x21, 0x00}); // Reset
                        output.write("\n".getBytes(StandardCharsets.UTF_8));
                    }
                    case "p" -> {
                        processNodes(el.childNodes(), output);
                        output.write("\n".getBytes(StandardCharsets.UTF_8)); // One line per paragraph
                    }
                    case "pre" -> {
                        // Directly preserve formatting inside <pre>
                        String preText = el.text();
                        output.write(preText.getBytes(StandardCharsets.UTF_8));
                        output.write("\n".getBytes(StandardCharsets.UTF_8));
                    }

                    default -> processNodes(el.childNodes(), output);
                }
            }
        }
    }


    // Your existing method + dynamic HTML to ESC/POS conversion & print
    public static void printReceiptFromHtmlString(String html, String orderId) throws Exception {
        // Replace placeholder dynamically
//        String processedHtml = html.replace("{{orderId}}", orderId);

        // Convert rendered HTML to ESC/POS
        byte[] escposData = convertHtmlToEscPos(html);

        // Send raw bytes to printer
        printRawBytes(escposData, "RONGTA 80mm Series Printer");
    }


    public static void main(String[] args) throws Exception {

        String sampleHtml = """
                <center><h1>*** RECEIPT ***</h1></center>
                <pre>
                    Shop Name                  : Apex Deli
                    Date                       : 2025-05-15
                    Order ID                   : #12345
                ------------------------------------------------
                    Item                 Qty   Price   Total
                ------------------------------------------------
                    Burger               1     10.00   10.00
                    Fries                2      5.00   10.00
                    Coke                 1      3.00    3.00
                ------------------------------------------------
                    Subtotal                           23.00
                    Tax (5%)                            1.15
                ------------------------------------------------
                    <b>Total                           24.15</b>
                ------------------------------------------------
                
                    <center>Thank you for your order!</center>
                    </pre>
                """;


        printReceiptFromHtmlString(sampleHtml, "12345");
    }
}
