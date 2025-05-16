package src.service;

import src.model.Item;
import src.model.Receipt;

public class ReceiptCalculatorService {

    public static void calculate(Receipt receipt) {
        double subtotal = 0;

        for (Item item : receipt.getItems()) {
            subtotal += item.getTotalPrice();
        }

        subtotal = round(subtotal);
        receipt.setSubtotal(subtotal);

        // Apply coupon (if any)
        double coupon = receipt.getCoupon(); // default 0 if not set
        double afterCoupon = subtotal - coupon;

        // Apply discount
        double discountAmount = receipt.getDiscountType().equalsIgnoreCase("percentage")
                ? afterCoupon * (receipt.getDiscountRate() / 100.0)
                : receipt.getDiscountRate();

        discountAmount = Math.min(discountAmount, afterCoupon);
        double afterDiscount = afterCoupon - discountAmount;

        // Apply tax
        double taxAmount = receipt.getTaxType().equalsIgnoreCase("percentage")
                ? afterDiscount * (receipt.getTaxRate() / 100.0)
                : receipt.getTaxRate();

        // Apply VAT
        double vatAmount = receipt.getVatType().equalsIgnoreCase("percentage")
                ? afterDiscount * (receipt.getVatRate() / 100.0)
                : receipt.getVatRate();

        // Set final computed fields
        receipt.setDiscount(round(discountAmount));
        receipt.setTax(round(taxAmount));
        receipt.setVat(round(vatAmount));
        receipt.setTotal(round(afterDiscount + taxAmount + vatAmount));

        // Optional: Calculate change if paid is already set
        if (receipt.getPaid() > 0) {
            receipt.setChange(round(receipt.getPaid() - receipt.getTotal()));
        }
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
