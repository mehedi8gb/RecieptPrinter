package src.com.TMSAgent.service;

import src.com.TMSAgent.model.Receipt;
import src.com.TMSAgent.templates.IReceiptTemplate;
import src.com.TMSAgent.templates.TemplateRegistry;

/**
 * Service to render receipts using the selected template strategy.
 */
public class ReceiptRenderer {

    public static String render(Receipt receipt) {
        return render(receipt, "template1");
    }

    public static String render(Receipt receipt, String templateName) {
//        templateName = "EscPosTemplate";
        IReceiptTemplate template = TemplateRegistry.get(templateName);
        return template.render(receipt);
    }
}

