package src;

import com.fasterxml.jackson.databind.JsonNode;
import src.model.Receipt;
import src.service.*;
import static src.util.Logger.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        log("\n\nStarting receipt printer...");
        // 1. Parse CLI arguments
        log("\n Unescaped JSON : " + args[0]);
        String raw = JsonArgument.cleanCLIJson(args[0]);
        log("\n escaped JSON : " + raw);

        JsonNode parsed = JsonArgument.parse(raw);
        log("\n Loaded receipt: " + parsed);

        // 2. Load Receipt data from parsed json
        assert parsed != null;
        Receipt receipt = ReceiptLoaderService.loadFrom(parsed);

        log("\n Loaded receipt: " + receipt);

        // 3. Calculate receipt totals
        ReceiptCalculatorService.calculate(receipt);

        // 4. Render content using selected template
        String content = ReceiptRenderer.render(receipt, parsed.get("template").asText());

        // 5. Output to console or printer
        System.out.println(content);
        log("\n\nPrinting to " + parsed.get("printerName").asText());
        log("\n\nContent: \n\n" + content);
        ReceiptPrinterService.printTo(parsed.get("printerName").asText(), content);

        log("\n\nReceipt printed successfully.");
    }
}
