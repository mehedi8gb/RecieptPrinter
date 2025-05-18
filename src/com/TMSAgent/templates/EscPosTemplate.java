package src.com.TMSAgent.templates;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst.*;
import com.github.anastaciocintra.escpos.Style;

import src.com.TMSAgent.model.Item;
import src.com.TMSAgent.model.Receipt;
import src.com.TMSAgent.util.Logger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EscPosTemplate implements IReceiptTemplate {

    @Override
    public String render(Receipt receipt) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            OutputStream os = System.out;
            EscPos escpos = new EscPos(os); // ✅ Correct

            Style title = new Style().setFontSize(Style.FontSize._2, Style.FontSize._2).setJustification(Justification.Center);
            Style subtitle = new Style().setBold(true).setJustification(Justification.Center);
            Style left = new Style().setJustification(Justification.Left_Default);
            Style center = new Style().setJustification(Justification.Center);
            Style line = new Style().setJustification(Justification.Left_Default);

            // Header
            escpos.writeLF(title, receipt.getShopName());
            escpos.writeLF(subtitle, receipt.getShopAddress());
            escpos.writeLF(subtitle, "Tel: " + receipt.getShopPhone());
            escpos.feed(1);

            escpos.writeLF(left, "TESTING: " + receipt.getInvoiceId());
            escpos.writeLF(left, "Date: " + receipt.getDate());
            escpos.writeLF(left, "Time: " + receipt.getTime());
            escpos.writeLF(left, "Cashier: " + receipt.getCashierName());
            escpos.writeLF(left, "Customer: " + receipt.getCustomerName());
            escpos.writeLF("------------------------------------------");

            // Table Header
            escpos.writeLF(center, String.format("%-15s %3s %8s %9s", "Item", "Qty", "Price", "Total"));
            escpos.writeLF("------------------------------------------");

            // Items
            for (Item item : receipt.getItems()) {
                String name = item.getName().length() > 15
                        ? item.getName().substring(0, 15)
                        : item.getName();
                escpos.writeLF(String.format("%-15s %3d %8.2f %9.2f",
                        name, item.getQuantity(), item.getUnitPrice(), item.getTotalPrice()));
            }

            escpos.writeLF("------------------------------------------");
            escpos.writeLF(String.format("Subtotal:%32.2f", receipt.getSubtotal()));

            if (receipt.getDiscountRate() > 0) {
                boolean isPercent = receipt.getDiscountType().equalsIgnoreCase("percentage");
                String rate = isPercent ? String.format(" (%.1f%%)", receipt.getDiscountRate()) : " (fixed)";
                escpos.writeLF(String.format("Discount%s:%26.2f", rate, receipt.getDiscount()));
            }

            if (receipt.getTax() > 0) {
                boolean isPercent = receipt.getTaxType().equalsIgnoreCase("percentage");
                String rate = isPercent ? String.format(" (%.1f%%)", receipt.getTaxRate()) : " (fixed)";
                escpos.writeLF(String.format("Tax%s:%31.2f", rate, receipt.getTax()));
            }

            if (receipt.getVat() > 0) {
                boolean isPercent = receipt.getVatType().equalsIgnoreCase("percentage");
                String rate = isPercent ? String.format(" (%.1f%%)", receipt.getVatRate()) : " (fixed)";
                escpos.writeLF(String.format("VAT%s:%31.2f", rate, receipt.getVat()));
            }

            escpos.writeLF("==========================================");
            escpos.writeLF(new Style().setBold(true), String.format("TOTAL:%33.2f", receipt.getTotal()));
            escpos.writeLF(String.format("Paid:%34.2f", receipt.getPaid()));
            escpos.writeLF(new Style().setBold(true), String.format("CHANGE:%32.2f", receipt.getChange()));
            escpos.writeLF("==========================================");

            escpos.feed(1);
            escpos.writeLF(center, "THANK YOU FOR YOUR PURCHASE!");
            escpos.writeLF(center, "Have a good day!");
            escpos.feed(4);
            escpos.cut(EscPos.CutMode.FULL);

            escpos.close();
            return byteStream.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            Logger.logException("Error rendering receipt in EscPosTemplate", e);
            return "ERROR GENERATING RECEIPT";
        }
    }
}
