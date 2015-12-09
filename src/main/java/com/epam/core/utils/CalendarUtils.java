package com.epam.core.utils;

import com.epam.core.common.CommonTimeouts;
import com.epam.core.components.element.TextInput;
import com.epam.core.driver.Driver;
import com.epam.core.driver.DriverUnit;
import com.epam.core.logging.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public final class CalendarUtils {

    private CalendarUtils() {
    }

    public static void setDate(TextInput textInput, String date) {
        Logger.logInfo("Make visible following element: " + textInput.getName());
//        textInput.highlightElement();
        WebElement element = textInput.getWrappedElement();

        String elementId = element.getAttribute("id");
        Logger.logDebug("element id: " + elementId);

        String allowInputScript = "$('#" + elementId + "').attr('readonly', false);";
        String highlightElementScript = "arguments[0].style.backgroundColor = 'red';";

        Driver.getDefault().executeScript(highlightElementScript, element);
        Driver.getDefault().executeScript(allowInputScript, element);

        element.sendKeys(date);
        element.sendKeys(Keys.ENTER);

        DriverUnit.waitForSpecifiedTimeout(CommonTimeouts.TIMEOUT_500_MS.getMilliSeconds());
    }
}
