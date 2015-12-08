package com.epam.core.components;

import com.epam.core.common.CommonTimeouts;
import com.epam.core.components.element.TextInput;
import com.epam.core.driver.Driver;
import com.epam.core.driver.DriverUnit;
import com.epam.core.exceptions.HtmlElementsException;
import com.epam.core.logging.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.lang.reflect.Field;
import java.net.URL;

public final class ElementUtils {

    public static boolean existsInClasspath(final String fileName) {
        return getResourceFromClasspath(fileName) != null;
    }

    public static URL getResourceFromClasspath(final String fileName) {
        return Thread.currentThread().getContextClassLoader().getResource(fileName);
    }

    public static boolean isOnRemoteWebDriver(WebElement element) {
        if (!isRemoteWebElement(element)) {
            return false;
        }

        // Since subclasses of RemoteWebElement were finally removed in Selenium 2.26.0, WebElements on local drivers
        // are also instances of RemoteWebElement class. The only way that we found at the current moment to find out
        // whether WebElement instance is on remote driver is to check the class of RemoteWebElement "parent" filed,
        // which contains WebDriver instance to which this RemoteWebElement belongs.
        // As this field has protected access this is done by reflection.
        // TODO It's is a kind of a dirty hack to be improved in future versions.
        RemoteWebElement remoteWebElement = (RemoteWebElement) element;
        try {
            Field elementParentFiled = RemoteWebElement.class.getDeclaredField("parent");
            elementParentFiled.setAccessible(true);
            WebDriver elementParent = (WebDriver) elementParentFiled.get(remoteWebElement);
            return elementParent.getClass().equals(RemoteWebDriver.class);
        } catch (NoSuchFieldException e) {
            throw new HtmlElementsException("Unable to find out if WebElement is on remote driver", e);
        } catch (IllegalAccessException e) {
            throw new HtmlElementsException("Unable to find out if WebElement is on remote driver", e);
        }
    }

    public static boolean isRemoteWebElement(WebElement element) {
        return element.getClass().equals(RemoteWebElement.class);
    }

    public static void setDate(TextInput textInput, String date) {
        Logger.logInfo("Make visible following element: " + textInput.getName());
        textInput.highlightElement();
        WebElement element = textInput.getWrappedElement();

        String elementId = element.getAttribute("id");
        Logger.logDebug("element id: " + elementId);

        String allowInputScript = "$('#" + elementId + "').attr('readonly', false);";
        Driver.getDefault().executeScript(allowInputScript, element);

        element.sendKeys(date);
        element.sendKeys(Keys.ENTER);

        DriverUnit.waitForSpecifiedTimeout(CommonTimeouts.TIMEOUT_500_MS.getMilliSeconds());
    }
}
