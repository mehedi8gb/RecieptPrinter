package src.com.TMSAgent.model;

import java.util.List;

public class ReceiptDataDTO {
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    private String invoiceId;
    private String cashierName;
    private String date;
    private String time;
    private double subtotal;
    private double discount;
    private double tax;
    private double total;
    private double paid;
    private double change;
    private List<Item> items;

}
