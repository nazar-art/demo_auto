package com.epam.core.driver;

import com.epam.core.common.CommonTimeouts;
import com.epam.core.common.Config;
import com.epam.core.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.epam.core.driver.Driver.chromeService;
import static com.epam.core.driver.Driver.driver;

public final class DriverUnit {

    public static void dragAndDrop(WebElement element, int x, int y) {
        String browserName = Config.getProperty(Config.BROWSER);

        if (browserName.matches("ANDROIDHYBRID")) {
            dragAndDropAndroid(element, (double) x, (double) 0);

        } else {
            Actions act = new Actions(driver.get());

            act.moveToElement(element).build().perform();
            act.clickAndHold().build().perform();
            act.moveByOffset(x, y).build().perform();
            act.release().build().perform();
        }
    }

    public static void moveMouse(WebElement element) {
        Actions act = new Actions(Driver.getDefault());
        act.moveToElement(element).build().perform();// click(element);
    }

    public static void clickMouse(WebElement element) {
        Actions act = new Actions(Driver.getDefault());
        act.click(element).build().perform();
    }

    public static void dragAndDropAndroid(WebElement LocatorFrom, Double x, Double y) {
        Logger.logDebug("dragAndDropAndroid");
        String xto = Double.toString(x);
        String yto = Double.toString(y);
        Driver.getDefault().executeScript(
                "function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c))"
                        + "{a=b;break}"
                        + "if(!a)return!1;"
                        + "document.createEvent?("
                        + "b=document.createEvent(a),"
                        + "a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; "
                        + "simulate(arguments[0],\"mousedown\",0,0); simulate(arguments[0],\"mousemove\",arguments[1],arguments[2]); simulate(arguments[0],\"mouseup\",arguments[1],arguments[2]); ",
                LocatorFrom, xto, yto);

    }

    public static boolean isDisplayed(By by) {
        try {
            WebElement webElement = Driver.getDefault().findElement(by);
            if (webElement != null && webElement.isDisplayed()) {
                return true;
            }
        } catch (NoSuchElementException e) {
            return false;
        }
        return false;
    }

    public static void moveToAnotherTab() {
        for (String winHandle : Driver.getDefault().getWindowHandles()) {
            Driver.getDefault().switchTo().window(winHandle);
        }
    }

    public static void moveToAnotherTab(RemoteWebDriver driver) {
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    public static void openNewTab() {
        String openNewTabCombination = Keys.chord(Keys.CONTROL, "t");
        Driver.getDefault().findElement(By.tagName("body")).sendKeys(openNewTabCombination);
    }

    public static void waitForSpecifiedTimeout(long intervalInMilliSec) {
        try {
            Logger.logDebug("Sleeping for '" + intervalInMilliSec + "' ms");
            Thread.sleep(intervalInMilliSec);
        } catch (InterruptedException e) {
            Logger.logError(e.getMessage());
        }
    }

    public static void switchFrame(WebElement frameLocator) {
        Driver.getDefault().switchTo().frame(frameLocator);
    }

    public static void switchDefaultContent() {
        Driver.getDefault().switchTo().defaultContent();
    }

    public static void closeAnyWindowExcept(String windowNeed) {

        if (Driver.getDefault() != null) {
            try {
                Set<String> windowHandles = Driver.getDefault().getWindowHandles();
                if (windowHandles != null && !windowHandles.isEmpty()) {
                    if (windowHandles.size() >= 2) {
                        for (String windowId : windowHandles) {
                            if (Driver.getDefault() != null) {
                                Driver.getDefault().switchTo().window(windowId);
                                if (!windowId.equals(windowNeed)) {
                                    closeBrowser();
                                }

                            } else {
                                Logger.logEnvironment("There is no window opened");
                            }
                        }

                    }
                }
            } catch (UnreachableBrowserException e) {
                e.printStackTrace();
            } finally {
                Driver.getDefault().switchTo().window(windowNeed);
            }
        }
    }

    private static void closeBrowser() {
        try {
            if (Config.getProperty(Config.BROWSER).equalsIgnoreCase(Config.B_CHROME)) {
                chromeService.get().stop();
                chromeService.remove();
            } else {
                Logger.logDebug("We are closing window");
                Driver.getDefault().close();
            }
        } catch (UnreachableBrowserException e) {
            e.printStackTrace();
        }
    }

    public static RemoteWebDriver getNewDriver() {
        String driverType = Config.getProperty(Config.BROWSER).toUpperCase();
        return getNewDriver(DriversEnum.valueOf(driverType));
    }

    private static RemoteWebDriver getNewDriver(DriversEnum driverType) {
        Logger.logDebug("Create new instance of Driver. Driver type: " + driverType.toString());
        RemoteWebDriver driver = driverType.getDriver();
        preconfigureDriver(driver);
        return driver;
    }

    private static void preconfigureDriver(RemoteWebDriver driver) {
        driver.manage().timeouts()
                .pageLoadTimeout(
                        CommonTimeouts.DEFAULT_WAIT_TO_LOADING_TIMEOUT.getMilliSeconds(), TimeUnit.MILLISECONDS);
        driver.manage().timeouts()
                .implicitlyWait(
                        CommonTimeouts.DEFAULT_ELEMENT_WAITING_TIMEOUT.getMilliSeconds(), TimeUnit.MILLISECONDS);

        driver.manage().window().maximize();
    }

    public static void closeDriver(RemoteWebDriver driver) {
        driver.close();
    }
}
