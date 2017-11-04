package net.lelyak.core.components.element;

import net.lelyak.core.components.AbstractPageElement;
import net.lelyak.core.driver.Driver;
import net.lelyak.core.localization.Localization;
import net.lelyak.core.logging.Logger;
import org.openqa.selenium.WebElement;

public class TextInput extends AbstractPageElement {

    public static final String VALUE_ATTRIBUTE = "value";

    public TextInput(WebElement wrappedElement, String name, String page) {
        super(wrappedElement, name, page);
    }

    public String getText() {
        return wrappedElement.getAttribute(VALUE_ATTRIBUTE);
    }

    public void sendText(String text) {
        if (wrappedElement != null) {
            highlightElement();
            wrappedElement.clear();

            if (text != null && !text.isEmpty()) {
                wrappedElement.sendKeys(text);
                Logger.logInfo(Localization
                        .getMessage(Localization.INPUT_SET_VALUE, String.join(text, "[", "]"), name, page));
            }
        } else {
            Logger.logError(Localization.getMessage(Localization.NO_INPUT, name, page));
        }
    }

    public void click() {
        if (wrappedElement != null) {
            wrappedElement.click();
            Logger.logDebug("Click element");
        } else {
            Logger.logError(Localization.getMessage(Localization.NO_INPUT, name, page));
        }
    }

    public void clear() {
        if (wrappedElement != null) {
            wrappedElement.clear();
            wrappedElement.click();
            Logger.logDebug(String.format("Clear input element \"%s\" on page \"%s\"", name, page));
        } else {
            Logger.logError(Localization.getMessage(Localization.NO_INPUT, name, page));
        }
    }

    public void sendTextByJs(String text) {
        Driver.getDefault().executeScript("arguments[0].value='" + text + "'", wrappedElement);
        Driver.getDefault().executeScript("$(arguments[0]).change();", wrappedElement);
        Logger.logDebug("Send text '" + text + "' in " + this.name);
    }
}
