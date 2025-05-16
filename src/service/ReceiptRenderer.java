package src.service;

import src.model.Item;
import src.model.Receipt;

public class ReceiptRenderer {
    private static final int LINE_WIDTH = 48;
    private static final String LEFT_MARGIN = "   "; // 2 spaces margin to center the 43-char block
    public static final int LABEL_WIDTH = 22;
    public static final int PRICE_WIDTH = 10;

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


    public static String render(Receipt receipt) {
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


        // Items table with margin to center block
        sb.append(LEFT_MARGIN).append("+-----------------------------------------+\n");
        sb.append(LEFT_MARGIN).append("| Items      | Qty |   Price  |    Total  |\n");
        sb.append(LEFT_MARGIN).append("+-----------------------------------------+\n");

        for (Item item : receipt.getItems()) {
            String name = truncate(item.getName(), 16); // safe truncate
            int qty = item.getQuantity();
            double price = item.getUnitPrice();
            double total = item.getTotalPrice();

            // %16s  = Item Name (max 16)
            // %3d   = Quantity
            // %7.2f = Price (6 digits + dot)
            // %8.2f = Total (7 digits + dot)
            sb.append(String.format(
                    LEFT_MARGIN + "| %-10s | %3d | %7.2f | %8.2f |\n",
                    name,
                    qty,
                    price,
                    total
            ));
        }


        sb.append(LEFT_MARGIN).append("+-----------------------------------------+\n");

        sb.append(String.format(LEFT_MARGIN + "| %-23s %15.2f |\n", "Subtotal:", receipt.getSubtotal()));
        sb.append(String.format(LEFT_MARGIN + "| %-23s %15.2f |\n", "Discount:", receipt.getDiscount()));
        sb.append(String.format(LEFT_MARGIN + "| %-23s %15.2f |\n", "Tax:", receipt.getTax()));

// 🔥 Highlighted TOTAL block
        sb.append(LEFT_MARGIN).append("|=========================================|\n");
        sb.append(LEFT_MARGIN).append(formatLine("TOTAL", receipt.getTotal()));
        sb.append(LEFT_MARGIN).append("|-----------------------------------------|\n");

// PAID as normal
        sb.append(String.format(LEFT_MARGIN + "| %-23s %15.2f |\n", "Paid:", receipt.getPaid()));

// 💰 Highlighted CHANGE block
        sb.append(LEFT_MARGIN).append("|-----------------------------------------|\n");
        sb.append(LEFT_MARGIN).append(formatLine("CHANGE", receipt.getTotal()));
        sb.append(LEFT_MARGIN).append("|=========================================|\n");



        sb.append("-".repeat(LINE_WIDTH)).append("\n");

        // Footer - centered
        sb.append(centerText("THANK YOU FOR YOUR PURCHASE!")).append("\n");
        sb.append(centerText("Please come again!")).append("\n");
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

    private static String alignRight(String value, int width) {
        return " ".repeat(Math.max(0, width - value.length())) + value;
    }

    private static String formatMoney(double value) {
        String formatted = String.format("%.2f", value);
        // You could round/trim if needed: truncate when value is too long
        if (formatted.length() > 8) {
            return formatted.substring(0, 7) + "…"; // e.g., "100000…"
        }
        return formatted;
    }

    private static String renderTotalLine(String label, double value) {
        String formattedLabel = String.format("%-26s", label);
        String formattedValue = alignRight(formatMoney(value), 10);
        return String.format(LEFT_MARGIN + "| %s | %s |\n", formattedLabel, formattedValue);
    }


}
