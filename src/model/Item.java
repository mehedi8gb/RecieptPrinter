package src.model;
import static src.util.AppUtils.*;

public class Item {
    private static final int MAX_ITEM_NAME_LENGTH = 17;  // based on layout
    private String name;
    private int quantity;

    // No-args constructor for Jackson
    public Item() {
    }

    public Item(double unitPrice, int quantity, String name) {
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.name = formatItemName(name, MAX_ITEM_NAME_LENGTH);
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
        return formatItemName(name, MAX_ITEM_NAME_LENGTH);
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

