package src;

import src.model.Receipt;
import src.service.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. Parse CLI arguments
        ParsedArguments parsed = ArgumentParserService.parse(args);

        // 2. Load receipt data (from args JSON or fallback sample)
        Receipt receipt = parsed.hasCustomData()
                ? ReceiptLoaderService.loadFrom(parsed.dataSource())
                : src.builder.ReceiptBuilder.buildSampleReceipt();

        // 3. Calculate receipt totals
        ReceiptCalculatorService.calculate(receipt);

        // 4. Render content using selected template
        String content = ReceiptRenderer.render(receipt, parsed.template());

        // 5. Output to console or printer
        System.out.println(content);
        if (parsed.shouldPrint()) {
//            ReceiptPrinterService.printTo(parsed.getPrinterName(), content);
        }
    }
}

