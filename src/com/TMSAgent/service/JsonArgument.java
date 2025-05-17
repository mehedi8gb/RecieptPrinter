package src.com.TMSAgent.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static src.com.TMSAgent.util.Logger.log;

public class JsonArgument {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Cleans CLI or web-passed JSON string by decoding and unescaping.
     */
    public static String cleanCLIJson(String rawInput) {
        try {

            String prefix = "tmsagent:";
            if (rawInput.startsWith(prefix)) {
                rawInput = rawInput.substring(prefix.length());
            }

            // STEP 2: URL-decode
            String urlDecoded = URLDecoder.decode(rawInput, StandardCharsets.UTF_8);
            log("\ndecoded\n" + urlDecoded);

            // STEP 3: Unwrap double quotes if the string is wrapped (PowerShell/CMD)
            if ((urlDecoded.startsWith("\"") && urlDecoded.endsWith("\"")) ||
                    (urlDecoded.startsWith("'") && urlDecoded.endsWith("'"))) {
                urlDecoded = urlDecoded.substring(1, urlDecoded.length() - 1);
            }

            // STEP 4: Replace escape sequences like \" with "

            return urlDecoded.replace("\\\"", "\"");
        } catch (Exception e) {
            System.err.println("⚠️ Failed to clean CLI JSON: " + e.getMessage());
            return rawInput;
        }
    }

    /**
     * Parses cleaned JSON into Jackson JsonNode.
     */
    public static JsonNode parse(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            System.err.println("❌ Failed to parse JSON: " + e.getMessage());
            return null;
        }
    }
}
