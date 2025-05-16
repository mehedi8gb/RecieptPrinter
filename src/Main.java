package src;

import src.model.Item;
import src.model.Receipt;
import src.service.ReceiptPrinterService;
import src.service.ReceiptRenderer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Receipt receipt = new Receipt();
        receipt.setShopName("KRYSTALMART");
        receipt.setShopAddress("Main Branch");
        receipt.setShopPhone("+880123456789");
        receipt.setInvoiceId("20250514-0001");
        receipt.setCashierName("Rafi");
        receipt.setDate(LocalDate.now().toString());
        receipt.setTime(LocalTime.now().toString());
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
        receipt.calculateTotal();


        // 3. Render receipt
        String content = ReceiptRenderer.render(receipt);

        // 4. Print
        System.out.println(content);
        ReceiptPrinterService.printTo("RONGTA 80mm Series Printer", content);
    }
}
