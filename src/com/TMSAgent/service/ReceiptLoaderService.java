package src.com.TMSAgent.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import src.com.TMSAgent.model.Receipt;

import java.io.IOException;

import static src.com.TMSAgent.util.Logger.log;

public class ReceiptLoaderService {

    public static Receipt loadFrom(JsonNode json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.treeToValue(json.get("dataSource"), Receipt.class);
        } catch (IOException e) {
            log("Error loading receipt data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

