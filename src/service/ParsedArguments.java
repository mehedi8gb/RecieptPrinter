package src.service;

import static src.util.Logger.log;

public record ParsedArguments(String dataSource, String printerName, String template) {

    public boolean hasCustomData() {
    log("Parsed arguments: " + printerName);
        return dataSource != null && !dataSource.isBlank();
    }

    public boolean shouldPrint() {
        return printerName != null && !printerName.isBlank();
    }

    @Override
    public String template() {
        return template != null ? template : "default";
    }

    public String getPrinterName() {
        return printerName != null ? printerName : "default";
    }
}
