package src.service;

public class ArgumentParserService {
    public static ParsedArguments parse(String[] args) {
        String jsonData = args.length > 0 ? args[0] : null;
        String printerName = args.length > 1 ? args[1] : null;
        String template = args.length > 2 ? args[2] : "default";

        return new ParsedArguments(jsonData, printerName, template);
    }
}
