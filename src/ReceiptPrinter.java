package src;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import javax.print.*;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

        // ESC @ = Initialize printer
        output.write(new byte[]{0x1B, 0x40});

        // ESC 3 0 = Set line spacing to 0 dots (minimum possible spacing)
        output.write(new byte[]{0x1B, 0x33, 0x00});

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

    private static String centeredText(String text, boolean bold) {
        String result = String.format("%" + ((32 + text.length()) / 2) + "s", text);
        if (bold) {
            return (char) 0x1B + "E" + result + (char) 0x1B + "F" + "\n"; // Bold ON/OFF
        } else {
            return result + "\n";
        }
    }

    private static String dashedLine() {
        return "--------------------------------\n";
    }

    private static String leftRightText(String left, String right) {
        int space = 32 - left.length() - right.length();
        return left + " ".repeat(Math.max(1, space)) + right + "\n";
    }

    private static String formatItemLine(String qty, String item, String price) {
        item = item.length() > 18 ? item.substring(0, 18) : item;
        return String.format("%-4s %-18s %8s\n", qty, item, price);
    }


    public static String convertReceiptHtmlToEscPos(String html) {
        StringBuilder escPos = new StringBuilder();

        // ESC @ (Initialize)
        escPos.append((char) 0x1B).append("@");

        // Header
        escPos.append(centeredText("KRYSTALMART", true));
        escPos.append(centeredText("Main Branch", false));
        escPos.append(centeredText("abc", false));
        escPos.append(centeredText("Tel: (555) 123-4567", false));
        escPos.append(dashedLine());

        // Invoice Details
        escPos.append(leftRightText("Invoice:", "20250514-0004"));
        escPos.append(leftRightText("Sale ID:", "46"));
        escPos.append(leftRightText("Date:", "May 14, 2025, 12:00 AM"));
        escPos.append(leftRightText("Payment:", "cash"));
        escPos.append(dashedLine());

        // Items Table Header
        escPos.append("QTY  ITEM                 PRICE\n");
        escPos.append("--------------------------------\n");

        // Items
        escPos.append(formatItemLine("1", "Product 2", "৳700.00"));
        escPos.append(formatItemLine("1", "Barrett Small", "৳1,000.00"));
        escPos.append(dashedLine());

        // Totals
        escPos.append(leftRightText("Subtotal:", "৳1,700.00"));
        escPos.append(leftRightText("TOTAL:", "৳1,700.00"));
        escPos.append(dashedLine());

        // Payment info
        escPos.append("Payment Information:\n");
        escPos.append("Method: cash\n");
        escPos.append("Status: completed\n");
        escPos.append(dashedLine());

        // Note
        escPos.append("[NOTE] Sale processed via POS system.\n");
        escPos.append("Payment method: cash\n");
        escPos.append(dashedLine());

        // Footer
        escPos.append(centeredText("Thank you for your purchase!", false));
        escPos.append(centeredText("Tax ID: TAX-12345678", false));
        escPos.append(centeredText("5/15/2025", false));

        // Feed and cut (if supported in your system)
        escPos.append("\n");
        escPos.append((char) 0x1D).append("V").append((char) 0x01); // Partial cut

        return escPos.toString();
    }


    public static void printReceiptFromHtmlString(String html, String printerName) throws Exception {
        byte[] escposData = convertHtmlToEscPos(html);
        printRawBytes(escposData, printerName);
    }

    public static void main(String[] args) throws Exception {
        Logger.logInfo("=== Application started ===");
        try {
            if (args.length < 1) {
                System.out.println("Missing required input.");
                return;
            }

            String raw = URLDecoder.decode(args[0], StandardCharsets.UTF_8);

            // Split using a custom delimiter (like |)
            String[] parts = raw.split("\\|", 2);
            if (parts.length < 2) {
                System.out.println("Invalid input format. Expected 'printer|html'");
                return;
            }

            String printerName = parts[0];
            String html;


            if (parts[1].isEmpty()) {
                html = parts[1];
            } else {
                // Fallback demo
                html = """
                        <pre>
                                      KRYSTALMART
                                      Main Branch
                                         abc
                                   Tel: (555) 123-4567
                        --------------------------------------------
                        Invoice:          20250514-0001
                        Sale ID:          43
                        Date:             May 14, 2025, 12:00 AM
                        Payment:          cash
                        ----------------------------------------
                        Customer Details: Guest Customer
                        ----------------------------------------
                        Qty  Item                       Price
                        1    Product 2                 ৳700.00
                        1    Barrett Small             ৳1,000.00
                        ----------------------------------------
                        Subtotal:                      ৳1,700.00
                        ----------------------------------------
                        TOTAL:                         ৳1,700.00
                        ----------------------------------------
                        Note:     Sale processed via POS system.
                        ----------------------------------------
                             Thank you for your purchase!
                                 Tax ID: TAX-12345678
                                       5/16/2025
                        </pre>
                        """;
            }


            Logger.logInfo("Parsed Printer Name: " + printerName);
            Logger.logInfo("HTML Content Length: " + html.length());
            System.out.println(html); // stdout or use your transport method
            printReceiptFromHtmlString(html, printerName);
        } catch (Exception e) {
            Logger.logException("Exception in main()", e);
        }
    }
}
