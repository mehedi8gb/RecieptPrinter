package src.model;

import java.util.List;

import static java.lang.Math.round;

public class Receipt {
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    private String invoiceId;
    private String cashierName;
    private String date;
    private String time;
    private double subtotal;
    private double tax;
    private double total;
    private double paid;
    private double change;
    private double coupon;
    private double vat;
    private double vatRate; // percentage e.g. 5.0 for 5%
    private double taxRate; // percentage e.g. 2.5 for 2.5%
    private double discount;
    private double discountRate;
    private String discountType = "fixed"; // or "fixed"
    private String taxType = "fixed";      // or "fixed"
    private String vatType = "fixed";      // or "fixed"


    private List<Item> items;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPaid() {
        return paid;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public double getCoupon() {
        return coupon;
    }

    public void setCoupon(double coupon) {
        this.coupon = coupon;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getVatType() {
        return vatType;
    }

    public void setVatType(String vatType) {
        this.vatType = vatType;
    }

    public double getVat() {
        return vat;
    }


    public void setPaid(double paid) {
        this.paid = paid;
        if (this.total > 0 && paid >= this.total) {
            this.change = round(paid - this.total);
        } else {
            this.change = 0;
        }
    }

    public void calculateTotal() {
        double subtotal = 0;

        for (Item item : items) {
            subtotal += item.getTotalPrice();
        }

        this.subtotal = round(subtotal);

        // Apply coupon
        double afterCoupon = subtotal - coupon;

        // Apply discount
        double discountAmount = discountType.equals("percentage")
                ? afterCoupon * (this.discountRate / 100.0)
                : this.discountRate;
        discountAmount = Math.min(discountAmount, afterCoupon);
        double afterDiscount = afterCoupon - discountAmount;

        // Apply tax
        double taxAmount = taxType.equals("percentage")
                ? afterDiscount * (taxRate / 100.0)
                : taxRate;

        // Apply VAT
        double vatAmount = vatType.equals("percentage")
                ? afterDiscount * (vatRate / 100.0)
                : vatRate;

        // Final total
        this.tax = round(taxAmount);
        this.vat = round(vatAmount);
        this.discount = round(discountAmount);
        this.total = round(afterDiscount + tax + vat);

        // Optional: recalculate change if paid is already set
        if (paid > 0) {
            this.change = round(paid - total);
        }
    }


    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}

