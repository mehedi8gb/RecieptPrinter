package src.com.TMSAgent.templates;

import src.com.TMSAgent.model.Item;
import src.com.TMSAgent.model.Receipt;

public class DefaultTemplate implements IReceiptTemplate {
    private static final int LINE_WIDTH = 48;
    private static final String LEFT_MARGIN = ""; // 2 spaces margin to center the 43-char block
    public static final int LABEL_WIDTH = 22;

    public static String centerText(String text) {
        int padding = (LINE_WIDTH - text.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text;
    }

    private static String formatLine(String label, double amount) {
        String safeLabel = label.length() > LABEL_WIDTH
                ? label.substring(0, LABEL_WIDTH)
                : label;
        return String.format("| %-24s >>> %14.2f |\n", safeLabel, amount);
    }

    @Override
    public String render(Receipt receipt) {
        StringBuilder sb = new StringBuilder();

        // Header - centered
        sb.append(centerText(receipt.getShopName())).append("\n");
        sb.append(centerText(receipt.getShopAddress())).append("\n");
        sb.append(centerText("Tel: " + receipt.getShopPhone())).append("\n");
        sb.append("-".repeat(LINE_WIDTH)).append("\n");

        String date = receipt.getDate();  // Or format it here
        String timeFormatted = receipt.getTime(); // Should be already formatted
        String invoiceId = receipt.getInvoiceId();
        String cashier = receipt.getCashierName();
        String customer = receipt.getCustomerName();

        sb.append(String.format("%s%-" + LABEL_WIDTH + "s%s\n", LEFT_MARGIN, "Invoice:", invoiceId));
        sb.append(String.format("%s%-" + LABEL_WIDTH + "s%s\n", LEFT_MARGIN, "Date:", date));
        sb.append(String.format("%s%-" + LABEL_WIDTH + "s%s\n", LEFT_MARGIN, "Time:", timeFormatted));
        sb.append(String.format("%s%-" + LABEL_WIDTH + "s%s\n", LEFT_MARGIN, "Cashier:", cashier));
        sb.append(String.format("%s%-" + LABEL_WIDTH + "s%s\n", LEFT_MARGIN, "Customer:", customer));


// Header
        sb.append(LEFT_MARGIN).append("+-----------------+-----+---------+-----------+\n");
        sb.append(LEFT_MARGIN).append("| Item            | Qty | Price   |   Total   |\n");
        sb.append(LEFT_MARGIN).append("+-----------------+-----+---------+-----------+\n");

// Rows
        for (Item item : receipt.getItems()) {
            String name = item.getName();
            int qty = Math.min(item.getQuantity(), 999);
            double price = Math.min(item.getUnitPrice(), 9999.99);
            double total = Math.min(item.getTotalPrice(), 999999.99);

            sb.append(String.format(
                    LEFT_MARGIN + "| %-15s | %3d | %7.2f | %9.2f |\n",
                    name.length() > 15 ? name.substring(0, 15) : name,
                    qty, price, total
            ));
        }

// Footer
        sb.append(LEFT_MARGIN).append("+-----------------+-----+---------+-----------+\n");


        sb.append(String.format(LEFT_MARGIN + "| %-23s %19.2f |\n", "Subtotal:", receipt.getSubtotal()));

        if (receipt.getDiscountRate() > 0) {
            boolean isPercent = receipt.getDiscountType().equalsIgnoreCase("percentage");
            String rateValue = isPercent
                    ? String.valueOf(receipt.getDiscountRate())
                    : "fixed";
            sb.append(LEFT_MARGIN).append(formatLineWithRateMiddle("Discount", receipt.getDiscount(), rateValue, isPercent));
        }

        if (receipt.getTax() > 0) {
            boolean isPercent = receipt.getTaxType().equalsIgnoreCase("percentage");
            String rateValue = isPercent
                    ? String.valueOf(receipt.getTaxRate())
                    : "fixed";
            sb.append(LEFT_MARGIN).append(formatLineWithRateMiddle("Tax", receipt.getTax(), rateValue, isPercent));
        }

        if (receipt.getVat() > 0) {
            boolean isPercent = receipt.getVatType().equalsIgnoreCase("percentage");
            String rateValue = isPercent
                    ? String.valueOf(receipt.getVatRate())
                    : "fixed";
            sb.append(LEFT_MARGIN).append(formatLineWithRateMiddle("VAT", receipt.getVat(), rateValue, isPercent));
        }


// 🔥 Highlighted TOTAL block
        sb.append(LEFT_MARGIN).append("|=================== TOTAL ===================|\n");
        sb.append(LEFT_MARGIN).append(formatLine("TOTAL", receipt.getTotal()));
//        sb.append(LEFT_MARGIN).append("|-----------------------------------------|\n");

// PAID as normal
        sb.append(String.format(LEFT_MARGIN + "| %-23s %19.2f |\n", "Paid:", receipt.getPaid()));

// 💰 Highlighted CHANGE block
//        sb.append(LEFT_MARGIN).append("|-----------------------------------------|\n");
        sb.append(LEFT_MARGIN).append(formatLine("CHANGE", receipt.getChange()));
        sb.append(LEFT_MARGIN).append("|=============================================|\n");


        sb.append("-".repeat(LINE_WIDTH)).append("\n");

        // Footer - centered
        sb.append(centerText("THANK YOU FOR YOUR PURCHASE!")).append("\n");
        sb.append(centerText("Have a good day!")).append("\n");
        // Bottom margin space (e.g., 3 lines)
        sb.append("\n".repeat(3));

        return sb.toString();
    }

    private static String formatLineWithRateMiddle(String label, double amount, String rateValue, boolean isRatePercent) {
        int totalWidth = 44;  // total width inside borders
        int amountWidth = 10; // enough space for amount including decimal places
        int rateWidth = 9;    // enough for e.g. (fixed) or (2.5%)
        int labelWidth = totalWidth - amountWidth - rateWidth - 3; // 3 spaces for padding between sections

        // Format amount right aligned with 2 decimals
        String amountStr = String.format("%" + amountWidth + ".2f", amount);

        // Format rate string with percent or fixed
        String rateStr;
        if (rateValue == null || rateValue.isEmpty()) {
            rateStr = String.format("%" + rateWidth + "s", ""); // empty if no rate
        } else if (isRatePercent) {
            rateStr = String.format("%" + rateWidth + "s", "(" + rateValue + "%)"); // right align e.g. (2.5%)
        } else {
            rateStr = String.format("%" + rateWidth + "s", "(" + rateValue + ")"); // e.g. (fixed)
        }

        // Truncate label if needed
        label = label.length() > labelWidth ? label.substring(0, labelWidth - 3) + "..." : label;

        // Compose the line with padding:
        // | [label left aligned] [rate right aligned] [amount right aligned] |
        return String.format("| %-" + labelWidth + "s %s %s |\n", label, rateStr, amountStr);
    }
}
