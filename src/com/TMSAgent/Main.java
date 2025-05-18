package src.com.TMSAgent;

import com.fasterxml.jackson.databind.JsonNode;
import src.com.TMSAgent.model.Receipt;
import src.com.TMSAgent.service.*;

import javax.print.PrintException;
import javax.swing.*;
import java.io.IOException;

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

    public static void main(String[] args) throws PrintException, IOException {
        // Step 1: Validate and parse JSON input argument
        if (args.length == 0) {
            log("\nError: No input provided.\n");
            System.out.println("Usage: java -jar TMSAgent.jar <json_input>");
            JOptionPane.showMessageDialog(null,
                    "Error: No input provided.\nUsage: java -jar TMSAgent.jar \"json_input\"",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JsonNode parsed = JsonArgument.parse(args[0]);

        assert parsed != null;
        // Step 2: Load Receipt object from parsed input
        Receipt receipt = ReceiptLoaderService.loadFrom(parsed.get("dataSource"));

        log("\nReceipt data loaded successfully.\n invoiceId: " + receipt.getInvoiceId());

        // Step 3: Optional manual override (can be moved to JSON input)
        /*
        receipt.setDiscount(1000);
        receipt.setDiscountType("fixed");
        receipt.setTax(5);
        receipt.setVat(5);
        */

        // Step 4: Perform receipt calculations
        ReceiptCalculatorService.calculate(receipt);

        // Step 5: Render the receipt using specified template
        String content = ReceiptRenderer.render(receipt, parsed.get("template").asText());

        // Step 6: Output rendered receipt content
        String printerName = parsed.has("printerName") ? parsed.get("printerName").asText() : "console";
        log("\nTarget Printer: " + printerName);
        log("\nRendered Receipt Content:\n" + content);

        // Uncomment the line below to enable actual printing
        ReceiptPrinterService.printTo(printerName, content);

        log("Receipt job completed successfully.");
    }
}
