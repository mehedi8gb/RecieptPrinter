package src.model;

public class Item {
    private static final int MAX_ITEM_NAME_LENGTH = 16;  // based on layout
    private String name;
    private int quantity;

    public Item(double unitPrice, int quantity, String name) {
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.name = formatItemName(name);
    }

    private String formatItemName(String name) {
        if (name.length() > MAX_ITEM_NAME_LENGTH) {
            return name.substring(0, MAX_ITEM_NAME_LENGTH - 3) + ".";
        }
        return name;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private double unitPrice;

    public double getTotalPrice() {
        return quantity * unitPrice;
    }

    // Constructor + Getters + Setters
}

