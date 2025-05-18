package src.com.TMSAgent.templates;

import src.com.TMSAgent.model.Receipt;

public interface IReceiptTemplate {
    String render(Receipt receipt);
}
