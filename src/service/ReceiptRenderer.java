package src.service;

import src.model.Item;
import src.model.Receipt;
import static src.util.AppUtils.*;

public class ReceiptRenderer {
    private static final int LINE_WIDTH = 48;
    private static final String LEFT_MARGIN = "  "; // 2 spaces margin to center the 43-char block
    public static final int LABEL_WIDTH = 22;
    public static final int PRICE_WIDTH = 10;
    private static final int ITEM_NAME_WIDTH = 14;   // 16 total with spaces

    private static String truncate(String value, int length) {
        if (value.length() <= length) return value;
        return value.substring(0, length - 1) + "…";
    }

    public static String centerText(String text) {
        int padding = (LINE_WIDTH - text.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text;
    }

    private static String formatLine(String label, double amount) {
        String safeLabel = label.length() > LABEL_WIDTH
                ? label.substring(0, LABEL_WIDTH)
                : label;
        return String.format("| %-22s >>> %12.2f |\n", safeLabel, amount);
    }


    public static String render(Receipt receipt, String template) {
        StringBuilder sb = new StringBuilder();

        // Header - centered
        sb.append(centerText(receipt.getShopName())).append("\n");
        sb.append(centerText(receipt.getShopAddress())).append("\n");
        sb.append(centerText("Tel: " + receipt.getShopPhone())).append("\n");
        sb.append("-".repeat(LINE_WIDTH)).append("\n");

        String date = receipt.getDate();
        String timeRaw = receipt.getTime();
        String timeFormatted = formatTimeHMS(timeRaw);

        sb.append(String.format("%s%-7s%s\n", LEFT_MARGIN, "Invoice:", receipt.getInvoiceId()));
        sb.append(String.format("%s%-7s%s\n", LEFT_MARGIN, "Date:", date));
        sb.append(String.format("%s%-7s%s\n", LEFT_MARGIN, "Time:", timeFormatted));
        sb.append(String.format("%s%-7s%s\n", LEFT_MARGIN, "Cashier:", receipt.getCashierName()));


        // Header
        sb.append(LEFT_MARGIN).append("+----------------+-----+--------+----------+\n");
        sb.append(LEFT_MARGIN).append("| Item           | Qty | Price  |  Total   |\n");
        sb.append(LEFT_MARGIN).append("+----------------+-----+--------+----------+\n");

        // Rows
        for (Item item : receipt.getItems()) {
            String name = truncate(item.getName(), ITEM_NAME_WIDTH);
            int qty = Math.min(item.getQuantity(), 999); // limit quantity display
            double price = Math.min(item.getUnitPrice(), 9999.99); // cap to fit
            double total = Math.min(item.getTotalPrice(), 999999.99); // cap to fit

            // Safe formatted row
            sb.append(String.format(
                    LEFT_MARGIN + "| %-14s | %3d | %6.2f | %7.2f |\n",
                    name, qty, price, total
            ));
        }

        // Footer
        sb.append(LEFT_MARGIN).append("+----------------+-----+--------+----------+\n");
        sb.append(String.format(LEFT_MARGIN + "| %-23s %15.2f |\n", "Subtotal:", receipt.getSubtotal()));

        if (receipt.getDiscountRate() > 0) {
            String rate = receipt.getDiscountType().equalsIgnoreCase("percentage")
                    ? receipt.getDiscountRate() + "%"
                    : "৳";
            sb.append(LEFT_MARGIN).append(formatLineWithRateMiddle("Discount", receipt.getDiscount(), rate));
        }

        if (receipt.getTax() > 0) {
            String rate = receipt.getTaxType().equalsIgnoreCase("percentage")
                    ? receipt.getTaxRate() + "%"
                    : "৳";
            sb.append(LEFT_MARGIN).append(formatLineWithRateMiddle("Tax", receipt.getTax(), rate));
        }

        if (receipt.getVat() > 0) {
            String rate = receipt.getVatType().equalsIgnoreCase("percentage")
                    ? receipt.getVatRate() + "%"
                    : "৳";
            sb.append(LEFT_MARGIN).append(formatLineWithRateMiddle("VAT", receipt.getVat(), rate));
        }




// 🔥 Highlighted TOTAL block
        sb.append(LEFT_MARGIN).append("|================= TOTAL ==================|\n");
        sb.append(LEFT_MARGIN).append(formatLine("TOTAL", receipt.getTotal()));
//        sb.append(LEFT_MARGIN).append("|-----------------------------------------|\n");

// PAID as normal
        sb.append(String.format(LEFT_MARGIN + "| %-23s %15.2f |\n", "Paid:", receipt.getPaid()));

// 💰 Highlighted CHANGE block
//        sb.append(LEFT_MARGIN).append("|-----------------------------------------|\n");
        sb.append(LEFT_MARGIN).append(formatLine("CHANGE", receipt.getChange()));
        sb.append(LEFT_MARGIN).append("|==========================================|\n");



        sb.append("-".repeat(LINE_WIDTH)).append("\n");

        // Footer - centered
        sb.append(centerText("THANK YOU FOR YOUR PURCHASE!")).append("\n");
        sb.append(centerText("Have a good day!")).append("\n");
        // Bottom margin space (e.g., 3 lines)
        sb.append("\n".repeat(3));

        return sb.toString();
    }

    // Utility: Extract HH:mm:ss from full time string
    private static String formatTimeHMS(String timeRaw) {
        // Expected format example: "03:45:54.185776700"
        // Return only "03:45:54"
        if (timeRaw == null || timeRaw.length() < 8) return timeRaw;
        return timeRaw.substring(0, 8);
    }

    private static String formatLineWithSymbol(String label, double amount, String symbol) {
        String line;
        int totalWidth = 41; // 1 + 39 + 1 for border (|...|)

        // Left part: label with symbol
        String labelWithSymbol = label;
        if (symbol != null && !symbol.isEmpty()) {
            labelWithSymbol += " (" + symbol + ")";
        }

        // Adjust for amount formatting
        String amountStr = String.format("%.2f", amount);

        int labelMaxWidth = totalWidth - amountStr.length() - 5; // space and border safety
        if (labelWithSymbol.length() > labelMaxWidth) {
            labelWithSymbol = truncate(labelWithSymbol, labelMaxWidth);
        }

        line = String.format("| %-"+labelMaxWidth+"s %"+(totalWidth-labelMaxWidth-3)+"s |\n", labelWithSymbol, amountStr);

        return line;
    }

    private static String formatLineWithRateMiddle(String label, double amount, String rateSymbol) {
        int totalWidth = 40;  // Total width inside the |...............|
        int amountWidth = 8;
        int rateWidth = 10;
        int labelWidth = totalWidth - amountWidth - rateWidth - 3; // 3 for 2 spaces and border

        String amountStr = String.format("%.2f", amount);
        String rateStr = rateSymbol != null && !rateSymbol.isEmpty() ? "(" + rateSymbol + ")" : "";

        // Truncate label if too long
        label = truncate(label, labelWidth);

        return String.format(
                "| %-"+labelWidth+"s %" + rateWidth + "s %" + amountWidth + "s |\n",
                label, rateStr, amountStr
        );
    }
}
