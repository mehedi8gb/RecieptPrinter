package src;

import src.builder.ReceiptBuilder;
import src.model.Receipt;
import src.service.ReceiptCalculatorService;
import src.service.ReceiptRenderer;

public class Main {
    public static void main(String[] args) throws Exception {
        Receipt receipt = ReceiptBuilder.buildSampleReceipt();

        // 2. Calculate totals
        ReceiptCalculatorService.calculate(receipt);

        // 3. Render receipt
        String content = ReceiptRenderer.render(receipt);

        // 4. Print receipt
        System.out.println(content);
//        ReceiptPrinterService.printTo("RONGTA 80mm Series Printer", content);
    }
}
