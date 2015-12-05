package com.epam.core.components.element;

import com.epam.core.common.Config;
import com.epam.core.components.AbstractPageElement;
import com.epam.core.driver.Driver;
import com.epam.core.localization.Localization;
import com.epam.core.logging.Logger;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;

public class NavigationLink extends AbstractPageElement {

    public NavigationLink(WebElement wrappedElement, String name, String page) {
        super(wrappedElement, name, page);
    }

    public void click() {
        try {
            if (wrappedElement != null) {

                String browserName = Config.getProperty(Config.BROWSER);

                if (browserName.equalsIgnoreCase(Config.B_CHROME)
                        || browserName.equalsIgnoreCase(Config.B_IE)) {

                    clickByJs();
                    Logger.logInfo(Localization.getMessage(Localization.CLICK_BUTTON, name, page));
                    Logger.logDebug("we passed to:" + Driver.driver.get().getCurrentUrl());
                    return;
                }

                wrappedElement.click();
                Logger.logDebug("we passed to:" + Driver.driver.get().getCurrentUrl());
                Logger.logInfo(Localization.getMessage(Localization.CLICK_BUTTON, name, page));

            } else {
                Logger.logError(Localization.getMessage(Localization.NO_BUTTON, name));
            }
        } catch (MoveTargetOutOfBoundsException e) {
            clickByJs();
            Logger.logInfo(Localization.getMessage(Localization.CLICK_BUTTON, name, page));
            Logger.logDebug("we passed to:" + Driver.driver.get().getCurrentUrl());

        } catch (ElementNotVisibleException e) {
            clickByJs();
            Logger.logInfo(Localization.getMessage(Localization.CLICK_BUTTON, name, page));
            Logger.logDebug("we passed to:" + Driver.driver.get().getCurrentUrl());
        }
    }

    public String getReference() {
        return wrappedElement.getAttribute("href");
    }

    public String getText() {
        return wrappedElement.getText();
    }
}
