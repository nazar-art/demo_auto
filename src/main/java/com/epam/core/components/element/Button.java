package com.epam.core.components.element;

import com.epam.core.localization.Localization;
import com.epam.core.logging.Logger;
import org.openqa.selenium.WebElement;

public class Button extends NavigationLink {

    public Button(WebElement wrappedElement, String name, String page) {
        super(wrappedElement, name, page);
    }

    public void submit() {
        if (wrappedElement != null) {
            highlightElement();
            wrappedElement.submit();
            Logger.logInfo(Localization.getMessage(Localization.BUTTON_SUBMIT, name, page));

        } else {
            Logger.logError(Localization.getMessage(Localization.NO_BUTTON, name));
        }
    }

}
