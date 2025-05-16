package src.core;

import src.model.Receipt;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReceiptMapper {

    public static Receipt mapFromDynamicSource(String printerName, Receipt dto) {
        Receipt receipt = new Receipt();
        receipt.setShopName(dto.getShopName());
        receipt.setShopAddress(dto.getShopAddress());
        receipt.setShopPhone(dto.getShopPhone());
        receipt.setInvoiceId(dto.getInvoiceId());
        receipt.setCashierName(dto.getCashierName());
        receipt.setDate(dto.getDate() != null ? dto.getDate() : LocalDate.now().toString());
        receipt.setTime(dto.getTime() != null ? dto.getTime() : LocalTime.now().toString());
        receipt.setSubtotal(dto.getSubtotal());
        receipt.setDiscountRate(dto.getDiscountRate());
        receipt.setTax(dto.getTax());
        receipt.setTotal(dto.getTotal());
        receipt.setPaid(dto.getPaid());
        receipt.setChange(dto.getChange());
        receipt.setItems(dto.getItems());

        // Optional: you can later use printerName if logic requires
        System.out.println("Selected printer: " + printerName);

        return receipt;
    }
}

