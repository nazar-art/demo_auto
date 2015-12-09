package com.epam.core.utils;

import com.epam.core.common.CommonTimeouts;
import com.epam.core.components.element.TextInput;
import com.epam.core.driver.Driver;
import com.epam.core.driver.DriverUnit;
import com.epam.core.logging.Logger;
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
