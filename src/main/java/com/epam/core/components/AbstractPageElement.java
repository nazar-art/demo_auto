package com.epam.core.components;


import com.google.common.base.Function;
import com.epam.core.common.Config;
import com.epam.core.driver.Driver;
import com.epam.core.logging.Logger;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AbstractPageElement extends AbstractSearchContext<WebElement> implements IPageElement {

    protected RemoteWebDriver driver = Driver.getDefault();
    protected final WebElement wrappedElement;
    protected final String name;
    protected final String page;

    public AbstractPageElement(final WebElement wrappedElement, final String name, final String page) {
        super(wrappedElement);
        this.wrappedElement = wrappedElement;
        this.name = name;
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public String getPage() {
        return page;
    }

    public static WebElement getFromCalendar(String date, String index) {
        try {
            WebElement myElement = Driver.getDefault().findElementByXPath(
                    "(//td[contains(text(),'" + date + "')])[" + index + "]");
            if (myElement.isDisplayed()) {
                Logger.logInfo("PageElement " + date + " is displayed");
                return myElement;
            }
        } catch (NoSuchElementException e) {
            Logger.logInfo("PageElement " + date + " not displayed");
            return null;
        }

        return null;
    }

    public static boolean clickIfExistInCalendar(String date, String index) {
        try {
            Driver.getDefault().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            WebElement myElement = Driver.getDefault().findElementByXPath(
                    "(//td[contains(text(),'" + date + "')])[" + index + "]");
            if (myElement.getAttribute("class").contains("dp_caption")) {
                myElement = Driver.getDefault().findElementByXPath(
                        "(//td[contains(text(),'" + date + "')])["
                                + (new Integer(index) + 1) + "]");
            }
            if (myElement.isDisplayed()) {
                Logger.logInfo("PageElement " + date + " is displayed");
                try {
                    WebDriverWait wait = new WebDriverWait(Driver.getDefault(),
                            1);
                    wait.until(ExpectedConditions.visibilityOf(myElement));
                    Logger.logInfo("PageElement " + date + " click");
                    // myElement.click();(20)
                    Driver.getDefault().executeScript(
                            "$(arguments[0]).click();", myElement);
                    return true;
                } catch (Exception e) {
                    Logger.logDebug("visibilityOfElementWait " + e.getMessage());
                    return false;
                }
            }
        } catch (NoSuchElementException e) {
            Logger.logInfo("PageElement " + date + " not displayed");
            return false;
        } finally {
            Driver.getDefault().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        }

        return false;
    }

    public static boolean clickIfExistInNextPayCalendar(String day) {
        Logger.logDebug("Calendar day: " + day);
        for (int row = 1; row < 8; row++) {
            for (int column = 1; column < 8; column++) {
                try {
                    Driver.getDefault().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                    List<WebElement> myElements = Driver.driver
                            .get()
                            .findElementsByXPath(
                                    "((//section[@id='PayFrequencyScreen']//div[@class='Zebra_DatePicker dp_visible']//table[@class='dp_daypicker']//td[@class='dp_disabled'])[contains(text(),'"
                                            + day + "')])");
                    List<WebElement> currentDisabled = Driver.driver
                            .get()
                            .findElementsByXPath(
                                    "((//section[@id='PayFrequencyScreen']//div[@class='Zebra_DatePicker dp_visible']//table[@class='dp_daypicker']//td[@class='dp_disabled dp_disabled_current'])[contains(text(),'"
                                            + day + "')])");
                    if (currentDisabled.size() > 0) {
                        return false;
                    }
                    if (myElements.size() > 0) {
                        Logger.logInfo("There is disabled dates");
                        for (WebElement element : myElements) {
                            String text = element.getText();
                            if (text.equalsIgnoreCase(day)) {
                                Logger.logInfo("Date that we want to pick is disabled");
                                // TODO: Here we must create additional logic to
                                // pick some random day if current is disabled
                                return false;
                            }
                        }
                        // return false;
                    }
                    WebElement myElement = Driver.driver
                            .get()
                            .findElementByXPath(
                                    "(//section[@id='PayFrequencyScreen']//div[@class='Zebra_DatePicker dp_visible']//table[@class='dp_daypicker']//td[contains(text(),'"
                                            + day + "')])");

                    Logger.logInfo("PageElement "
                            + "(.//*[@id='PayFrequencyScreen']/div[4]/div/section/div[1]/div[2]/div/div[1]/div/table[2]/tbody/tr["
                            + row + "]/td[" + column + "][contains(text(),'"
                            + day + "')])");
                    if (myElement.isDisplayed()) {
                        Logger.logInfo("Calendar day [" + day
                                + "] is displayed");
                        try {
                            WebDriverWait wait = new WebDriverWait(
                                    Driver.getDefault(), 1);
                            wait.until(ExpectedConditions
                                    .visibilityOf(myElement));
                            Logger.logInfo("PageElement " + day + " click");
                            Driver.driver
                                    .get()
                                    .executeScript(
                                            "arguments[0].style.backgroundColor = 'red';",
                                            myElement);
                            if (!myElement.getAttribute("class")
                                    .equalsIgnoreCase("dp_not_in_month")) {
                                myElement.click();
                            } else { // todo try change to match here instead of
                                // contains
                                List<WebElement> dates = Driver.driver
                                        .get()
                                        .findElementsByXPath(
                                                "(//section[@id='PayFrequencyScreen']//div[@class='Zebra_DatePicker dp_visible']//table[@class='dp_daypicker']//td[contains(text(),'"
                                                        + day + "')])");

                                for (WebElement webElement : dates) {
                                    if (webElement.getAttribute("class")
                                            .isEmpty()) {
                                        webElement.click();
                                        Logger.logInfo("PageElement " + day
                                                + " clicked");
                                    } else {
                                        Driver.driver
                                                .get()
                                                .executeScript(
                                                        "arguments[0].style.backgroundColor = 'red';",
                                                        webElement);
                                    }
                                }
                            }
                            return true;
                        } catch (Exception e) {
                            Logger.logDebug("visibilityOfElementWait " + e.getMessage());
                        }
                    }
                } catch (NoSuchElementException e) {
                    Logger.logInfo("PageElement " + day + " not displayed");
                    return true; // todo if we already typed data to calendar we
                    // should to return
                    // or we will be cycling here very long time
                    // and get this exception
                } finally {
                    Driver.getDefault().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                }

            }
        }
        return false;
    }

    public static boolean clickIfExistInSecondNextPayCalendar(String day) {
        for (int i = 2; i < 8; i++) {
            for (int j = 1; j < 8; j++) {
                try {
                    WebElement myElement = Driver.driver
                            .get()
                            .findElementByXPath(
                                    "(.//*[@id='PayFrequencyScreen']/div[4]/div/section/div[1]/div[2]/div/div[2]/div/table[2]/tbody/tr["
                                            + i
                                            + "]/td["
                                            + j
                                            + "][contains(text(),'"
                                            + day
                                            + "')])");
                    // Logger.logInfo("PageElement " +
                    // "(.//*[@id='PayFrequencyScreen']/div[4]/div/section/div[1]/div[2]/div/div[1]/div/table[2]/tbody/tr["
                    // + i +"]/td[" + j + "][contains(text(),'" + day + "')])");
                    if (myElement.isDisplayed()) {
                        Logger.logInfo("PageElement " + day + " is displayed");
                        try {
                            WebDriverWait wait = new WebDriverWait(
                                    Driver.getDefault(), 1);
                            wait.until(ExpectedConditions
                                    .visibilityOf(myElement));
                            Logger.logInfo("PageElement " + day + " click");
                            myElement.click();
                            return true;
                        } catch (Exception e) {
                            Logger.logDebug("visibilityOfElementWait "
                                    + e.getMessage());
                        }
                    }
                } catch (NoSuchElementException e) {
                    Logger.logInfo("PageElement " + day + " not displayed");

                }
            }
        }

        return false;
    }

    public static RemoteWebDriver switchToIFrame(String frame) {
        return (RemoteWebDriver) Driver.getDefault().switchTo().frame(frame);
    }

    public static RemoteWebDriver switchToContent() {
        return (RemoteWebDriver) Driver.getDefault().switchTo()
                .defaultContent();
    }

    public boolean visibilityOfElementWait() {
        if (wrappedElement != null) {
            try {
                WebDriverWait wait = new WebDriverWait(Driver.getDefault(), 10);
                wait.until(ExpectedConditions.visibilityOf(wrappedElement));
                return true;
            } catch (Exception e) {
                Logger.logDebug("visibilityOfElementWait " + e.getMessage());
                return false;
            }
        } else {
            Logger.logError("PageElement " + name + " not exist");
        }
        return false;
    }

    public boolean animationWait() {
        if (wrappedElement != null) {

            try {
                WebDriverWait wait = new WebDriverWait(Driver.getDefault(), 10);
                wait.until(new Function<WebDriver, Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        String object = Driver.driver
                                .get()
                                .executeScript(
                                        "return $(arguments[0]).is( \":animated\" );",
                                        wrappedElement).toString();
                        Logger.logInfo("Object is animated = " + object);
                        Boolean res = new Boolean(object);
                        res = !res.booleanValue();
                        return res;
                    }
                });
                return true;
            } catch (Exception e) {
                Logger.logDebug("Error in animation wait: " + e.getMessage());
                return false;
            }
        } else {
            Logger.logError("PageElement " + name + " not exist");
        }
        return false;
    }

    public boolean positionChangeWait() {
        if (wrappedElement != null) {

            try {
                WebDriverWait wait = new WebDriverWait(Driver.getDefault(), 10);
                wait.until(new Function<WebDriver, Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        String position1 = Driver.driver
                                .get()
                                .executeScript(
                                        "return $(arguments[0]).position();",
                                        wrappedElement).toString();
                        Logger.logInfo("Location 1 = "
                                + wrappedElement.getLocation());

                        Logger.logInfo("Position 1 = " + position1);
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            Logger.logError(e.getMessage());
                        }
                        String position2 = Driver.driver
                                .get()
                                .executeScript(
                                        "return $(arguments[0]).position();",
                                        wrappedElement).toString();
                        Logger.logInfo("Location 2 = "
                                + wrappedElement.getLocation());

                        Logger.logInfo("Position 2 = " + position2);
                        String by = null;
                        String locator = null;
                        boolean result = position1.equals(position2);

                        if (result) {
                            if (position2.equals("{top=0, left=0}")) {
                                return false;
                            } else {
                                return result;
                            }

                        }
                        return result;
                    }
                });
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Logger.logDebug("Error in position wait:  " + e.getMessage());
                return false;
            }
        } else {
            Logger.logError("PageElement " + name + " not exist");
        }
        return false;
    }

    public boolean visibilityOfElementWait(int seconds) {
        if (wrappedElement != null) {
            try {
                Driver.getDefault().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                WebDriverWait wait = new WebDriverWait(Driver.getDefault(),
                        seconds);
                wait.until(ExpectedConditions.visibilityOf(wrappedElement));
                return true;
            } catch (Exception e) {
                Logger.logDebug("visibilityOfElementWait " + e.getMessage());
                return false;
            } finally {
                Driver.getDefault().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            }

        } else {
            Logger.logError("PageElement " + name + " not exist");
        }
        return false;
    }

    public boolean isDisplayed() {
        try {
            if (wrappedElement != null) {
                try {
                    return wrappedElement.isDisplayed();
                } catch (NoSuchElementException e) {
                    Logger.logError("PageElement " + name + " not displayed");
                    return false;
                }
            } else {
                Logger.logError("PageElement " + name + " not exist");
            }
            return false;
        } catch (ElementNotVisibleException e) {
            Logger.logDebug("ElementNotVisibleException");
            return false;
        }
    }

    public String getTagName() {
        if (wrappedElement != null) {
            return wrappedElement.getTagName();
        } else {
            Logger.logError("PageElement " + name + " not exist");
        }
        return null;
    }

    public String getAttribute(String name) {
        if (wrappedElement != null) {
            highlightElement();
            return wrappedElement.getAttribute(name);
        } else {
            Logger.logInfo("PageElement " + name + " not exist");
        }
        return null;
    }

    public boolean isPresent() {
        if (wrappedElement != null) {
            try {
                return wrappedElement.isDisplayed();
            } catch (NoSuchElementException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public WebElement getWrappedElement() {
        return wrappedElement;
    }

    public void highlightElement() {
        if (!Config.getProperty("driver").equalsIgnoreCase("ANDROIDHYBRID")) {
            String bg = wrappedElement.getCssValue("backgroundColor");
            for (int i = 0; i < 3; i++) {
                driver.executeScript("arguments[0].style.backgroundColor = '" + "red" + "'", wrappedElement);
                driver.executeScript("arguments[0].style.backgroundColor = '" + bg + "'", wrappedElement);
            }
        }
    }

    public void focusJs() {
        driver.executeScript("arguments[0].focus();", wrappedElement);
    }

    public void focus() {
        if ("input".equals(wrappedElement.getTagName())) {
            wrappedElement.sendKeys("");
        } else {
            new Actions(driver).moveToElement(wrappedElement).clickAndHold()
                    .perform();
        }
    }

    public void clickByJs() {
        try {
            driver.executeScript("arguments[0].click()", wrappedElement);
        } catch (NoSuchElementException e) {
            Logger.logDebug("Element is not present when click by JS");
        }
    }

    private void updateWrappedElement(WebElement el) {
        try {
            Field[] field2 = this.getClass().getDeclaredFields();
            java.lang.annotation.Annotation[] ann = this.getClass()
                    .getDeclaredAnnotations();
            Field field = this.getClass().getDeclaredField("wrappedElement");
            String class1 = this.getClass().toString();
            // this.wrappedElement
            field.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(this, el);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
