package src.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static src.util.Logger.log;

public class JsonArgument {

   public static JsonNode parse(String escapedJson) {
        try {
            // 1. Unescape the JSON string
            String rawJson = escapedJson.replace("\\\"", "\"");
            log("\nUnescaped JSON: " + rawJson);

            // 2. Parse the raw JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(rawJson);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
