package com.epam.core.utils;

import com.epam.core.common.CommonTimeouts;
import com.epam.core.common.Config;
import com.epam.core.components.AbstractPageElement;
import com.epam.core.driver.Driver;
import com.epam.core.exceptions.HtmlElementsException;
import com.epam.core.logging.Logger;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.concurrent.TimeUnit;

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

    public static void highlightElement(WebElement element) {
        if (!Config.getProperty(Config.BROWSER).equalsIgnoreCase("ANDROIDHYBRID")) {

            String bg = element.getCssValue("backgroundColor");

            for (int i = 0; i < 4; i++) {
                Driver.getDefault()
                        .executeScript("arguments[0].style.backgroundColor = 'red'", element);
                Driver.getDefault()
                        .executeScript("arguments[0].style.backgroundColor = '" + bg + "'", element);
            }

//            String highlightElementScript = "arguments[0].style.backgroundColor = 'red';";
//            Driver.getDefault().executeScript(highlightElementScript, element);
        }
    }

    public static boolean waitForReady(WebElement element) {
        Logger.logInfo("Wait for element visibility - " + ReflectionToStringBuilder.toString(element));

        Wait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDefault())
                .withTimeout(CommonTimeouts.TIMEOUT_10_S.getSeconds(), TimeUnit.SECONDS)
                .pollingEvery(CommonTimeouts.TIMEOUT_500_MS.getMilliSeconds(), TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);

        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            Logger.logInfo("DONE: Element is visible - " + ReflectionToStringBuilder.toString(element));
            return true;
        } catch (TimeoutException e) {
            Logger.logWarning("Timeout waiting for element visibility - "
                    + ReflectionToStringBuilder.toString(element));
            return false;
        }
    }

    public static boolean waitForReady(AbstractPageElement pageElement) {
        Logger.logInfo(String.format("Wait for an element visibility %s on page %s",
                pageElement.getName(), pageElement.getPage()));

        Wait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDefault())
                .withTimeout(CommonTimeouts.TIMEOUT_10_S.getSeconds(), TimeUnit.SECONDS)
                .pollingEvery(CommonTimeouts.TIMEOUT_500_MS.getMilliSeconds(), TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);

        try {
            wait.until(ExpectedConditions.visibilityOf(pageElement.getWrappedElement()));
            Logger.logInfo("DONE: Element is visible " + pageElement.getName() + " on page " + pageElement.getPage() + " page");
            return true;
        } catch (TimeoutException e) { // todo replace below log with Location logging
            Logger.logWarning("Timeout waiting for element visibility "
                    + pageElement.getName() + " on page " + pageElement.getPage() + " page");
            return false;
        }
    }


}
