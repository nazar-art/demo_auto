package com.epam.core.components.element;

import com.epam.core.components.AbstractPageElement;
import com.epam.core.localization.Localization;
import com.epam.core.logging.Logger;
import org.openqa.selenium.WebElement;

public class TextInput extends AbstractPageElement {

    public static final String VALUE_ATTRIBUTE = "value";

    public TextInput(WebElement wrappedElement, String name, String page) {
        super(wrappedElement, name, page);
    }

    public void sendTextByJs(String text) {
        driver.executeScript("arguments[0].value='" + text + "'", wrappedElement);
        driver.executeScript("$(arguments[0]).change();", wrappedElement);
        Logger.logDebug("Send text '" + text + "' in " + this.name);
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
            }
//            Logger.logInfo(Localization.getMessage(Localization.INPUT_SET_VALUE, name, page));
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
            Logger.logDebug("Clear element");
        } else {
            Logger.logError(Localization.getMessage(Localization.NO_INPUT, name, page));
        }
    }
}
