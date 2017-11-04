package net.lelyak.core.utils;

import net.lelyak.core.common.CommonTimeouts;
import net.lelyak.core.components.element.TextInput;
import net.lelyak.core.driver.Driver;
import net.lelyak.core.driver.DriverUnit;
import net.lelyak.core.logging.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public final class CalendarUtils {

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    private CalendarUtils() {
    }

    public static void setDate(TextInput textInput, String date) {
        Logger.logInfo(String.format("Make visible following element: %s on page %s",
                textInput.getName(), textInput.getPage()));

        WebElement element = textInput.getWrappedElement();

        String elementId = element.getAttribute("id");
        Logger.logDebug("element id: " + elementId);

        String allowInputScript = "$('#" + elementId + "').attr('readonly', false);";

        ElementUtils.highlightElement(element);
        Driver.getDefault().executeScript(allowInputScript, element);

        element.sendKeys(date);
        element.sendKeys(Keys.ENTER);

        DriverUnit.waitForSpecifiedTimeout(CommonTimeouts.TIMEOUT_500_MS.getMilliSeconds());
    }
}
