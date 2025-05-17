package src.com.TMSAgent.builder;

import src.com.TMSAgent.model.Item;
import src.com.TMSAgent.model.Receipt;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceiptBuilder {
    public static Receipt buildSampleReceipt() {
        Receipt receipt = new Receipt();
        receipt.setShopName("KRYSTALMART");
        receipt.setShopAddress("Main Branch");
        receipt.setShopPhone("+880123456789");
        receipt.setInvoiceId("20250514-0001");
        receipt.setCashierName("Rafi");
        receipt.setCustomerName("John Doe");
        receipt.setDate(LocalDate.now().toString());
        receipt.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));

//        receipt.setSubtotal(10000.00);
//        receipt.setTotal(100.00);
//        receipt.setChange(1500.00);
        receipt.setItems(List.of(
                new Item(100, 10, "Barrett Small Test")
        ));
        receipt.setDiscountRate(10);
        receipt.setDiscountType("percentage");
//        receipt.setCoupon(25); // work later
        receipt.setTaxRate(2.5);
        receipt.setTaxType("percentage");
        receipt.setVatRate(5.0);
        receipt.setVatType("percentage");
        receipt.setPaid(1000);

        return receipt;
    }
}

