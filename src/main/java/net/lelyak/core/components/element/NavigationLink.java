package net.lelyak.core.components.element;

import net.lelyak.core.common.Config;
import net.lelyak.core.components.AbstractPageElement;
import net.lelyak.core.driver.Driver;
import net.lelyak.core.localization.Localization;
import net.lelyak.core.logging.Logger;
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
                    Logger.logDebug("we passed to:" + Driver.getDefault().getCurrentUrl());
                    return;
                }
                highlightElement();
                wrappedElement.click();

                Logger.logDebug("we passed to:" + Driver.getDefault().getCurrentUrl());
                Logger.logInfo(Localization.getMessage(Localization.CLICK_BUTTON, name, page));

            } else {
                Logger.logError(Localization.getMessage(Localization.NO_BUTTON, name));
            }
        } catch (MoveTargetOutOfBoundsException | ElementNotVisibleException e) {
            clickByJs();
            Logger.logInfo(Localization.getMessage(Localization.CLICK_BUTTON, name, page));
            Logger.logDebug("we passed to:" + Driver.getDefault().getCurrentUrl());

        }
    }

    public String getReference() {
        return wrappedElement.getAttribute("href");
    }

    public String getText() {
        return wrappedElement.getText();
    }
}
