package src.com.TMSAgent.templates;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a registry of all available receipt templates.
 * This class is open for extension, closed for modification.
 */
public class TemplateRegistry {

    private static final Map<String, IReceiptTemplate> templates = new HashMap<>();

    static {
        // Register templates here
        templates.put("template1", new DefaultTemplate());
        templates.put("EscPosTemplate", new EscPosTemplate());
        // templates.put("compact", new CompactTemplate());  <-- add more templates here
    }

    public static IReceiptTemplate get(String name) {
        return templates.getOrDefault(name, templates.get("template1"));
    }

    public static void register(String name, IReceiptTemplate template) {
        templates.put(name.toLowerCase(), template);
    }
}
