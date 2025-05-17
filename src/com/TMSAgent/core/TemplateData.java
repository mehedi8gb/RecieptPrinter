package src.com.TMSAgent.core;

import java.util.HashMap;
import java.util.Map;

public class TemplateData {
    public static Map<String, String> getSampleData() {
        Map<String, String> data = new HashMap<>();
        data.put("shopName", "Demo Shop");
        data.put("address", "123 Main St");
        data.put("phone", "+123456789");
        data.put("items", "<div>Item A - $10</div><div>Item B - $20</div>");
        data.put("total", "$30");
        return data;
    }
}


