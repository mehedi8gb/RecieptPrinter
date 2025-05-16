package src.service;

public record ParsedArguments(String dataSource, String printerName, String template) {

    public boolean hasCustomData() {
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

