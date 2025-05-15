package src.core;

import java.util.Map;
import java.util.regex.*;

public class TemplateResolver {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(\\w+)\\}\\}");

    public String resolve(String template, Map<String, String> data) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = data.getOrDefault(key, "{{" + key + "}}");
            matcher.appendReplacement(result, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(result);
        return result.toString();
    }
}


