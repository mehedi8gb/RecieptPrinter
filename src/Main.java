package src;

import com.fasterxml.jackson.databind.JsonNode;
import src.model.Receipt;
import src.service.*;
import static src.util.Logger.*;

public class Main {
    public static void main(String[] args) throws Exception {
        log("\n\nStarting receipt printer...");
        // 1. Parse CLI arguments
        JsonNode parsed = JsonArgument.parse(args[0]);
        log("\nParsed arguments: " + parsed);

        // 2. Load Receipt data from parsed json
//        Receipt receipt = src.builder.ReceiptBuilder.buildSampleReceipt();
        assert parsed != null;
        Receipt receipt = ReceiptLoaderService.loadFrom(parsed);

        log("\n Loaded receipt: " + receipt);

        // 3. Calculate receipt totals
        ReceiptCalculatorService.calculate(receipt);

        // 4. Render content using selected template
        String content = ReceiptRenderer.render(receipt, parsed.get("template").asText());

        // 5. Output to console or printer
//        System.out.println(content);

            log("\n\nPrinting to " + parsed.get("printerName").asText());
            log("\n\nContent: \n\n" + content);
//            ReceiptPrinterService.printTo(parsed.getPrinterName(), content);
        log("\n\nReceipt printed successfully.");
        log("\nReceipt printer finished.\n");

    }
}

