package com.epam.core.components.element;

import com.epam.core.components.AbstractPageElement;
import com.epam.core.localization.Localization;
import com.epam.core.logging.Logger;
import org.openqa.selenium.WebElement;

public class TextBox extends AbstractPageElement {

    public TextBox(WebElement wrappedElement, String name, String page) {
        super(wrappedElement, name, page);
    }

    public String getText() {
        if (wrappedElement != null) {
            // highlightElement();
            String containerValue = wrappedElement.getText();
            Logger.logDebug(Localization
                    .getMessage(Localization.TEXT_CONTAINER_GET, containerValue, name, page));

            return containerValue;
        } else {
            Logger.logError(Localization.getMessage(
                    Localization.NO_TEXT_CONTAINER, name, page));
        }
        return null;
    }
}
