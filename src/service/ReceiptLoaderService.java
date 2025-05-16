package src.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import src.model.Receipt;

public class ReceiptLoaderService {
    public static Receipt loadFrom(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Receipt.class);
    }
}

