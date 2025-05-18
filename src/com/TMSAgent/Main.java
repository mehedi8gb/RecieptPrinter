package src.com.TMSAgent;

import com.fasterxml.jackson.databind.JsonNode;
import src.com.TMSAgent.model.Receipt;
import src.com.TMSAgent.service.*;

import static src.com.TMSAgent.util.Logger.*;

/**
 * Main class for executing the receipt printing job.
 * Steps:
 * 1. Parse input arguments
 * 2. Load receipt data
 * 3. Calculate totals
 * 4. Render using template
 * 5. Output to printer or console
 */
public class Main {

    public static void main(String[] args) {
        log("\nStarting receipt printing job...\n");

        // Step 1: Validate and parse JSON input argument
        if (args.length == 0) {
            log("\nError: No input JSON provided.\n");
            return;
        }

        log("\nRaw input JSON: \n" + args[0]);
        JsonNode parsed = JsonArgument.parse(args[0]);

        if (parsed == null) {
            log("\nError: Failed to parse input JSON.\n");
            return;
        }

        log("\nParsed JSON successfully: \n" + parsed.toString());

        // Step 2: Load Receipt object from parsed input
        Receipt receipt = ReceiptLoaderService.loadFrom(parsed.get("dataSource"));
        if (receipt == null) {
            log("\nError: Failed to load receipt data.\n");
            return;
        }
        log("\nReceipt data loaded successfully.\n");

        // Step 3: Optional manual override (can be moved to JSON input)
        /*
        receipt.setDiscount(1000);
        receipt.setDiscountType("fixed");
        receipt.setTax(5);
        receipt.setVat(5);
        */

        // Step 4: Perform receipt calculations
        ReceiptCalculatorService.calculate(receipt);
        log("\nReceipt totals calculated.\n");

        // Step 5: Render the receipt using specified template
        String content = ReceiptRenderer.render(receipt, parsed.get("template").asText());

        // Step 6: Output rendered receipt content
        String printerName = parsed.has("printerName") ? parsed.get("printerName").asText() : "console";
        log("\nTarget Printer: " + printerName);
        log("\nRendered Receipt Content:\n" + content);

        // Uncomment the line below to enable actual printing
        // ReceiptPrinterService.printTo(printerName, content);

        log("Receipt job completed successfully.");
    }
}
