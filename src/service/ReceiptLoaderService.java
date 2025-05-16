package src.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import src.model.Receipt;

import java.io.DataInput;
import java.io.IOException;

import static src.util.Logger.log;

public class ReceiptLoaderService {
    private static final Log log = LogFactory.getLog(ReceiptLoaderService.class);

    public static Receipt loadFrom(JsonNode json) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.treeToValue(json.get("dataSource"), Receipt.class);
        } catch (IOException e) {
            log("Error loading receipt data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

