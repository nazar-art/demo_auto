package com.epam.core.components;


import com.epam.core.driver.Driver;
import com.epam.core.logging.Logger;
import com.epam.core.utils.ElementUtils;
import com.google.common.base.Function;
import lombok.Getter;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;


@Getter
public class AbstractPageElement extends AbstractSearchContext<WebElement> implements IPageElement {

    protected final WebElement wrappedElement;
    protected final String name;
    protected final String page;

    public AbstractPageElement(final WebElement wrappedElement, final String name, final String page) {
        super(wrappedElement);
        this.wrappedElement = wrappedElement;
        this.name = name;
        this.page = page;
    }

    @Override
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
            Logger.logDebug("ElementNotVisibleException - " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getTagName() {
        if (wrappedElement != null) {
            return wrappedElement.getTagName();
        } else {
            Logger.logError("PageElement " + name + " not exist");
        }
        return null;
    }

    @Override
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

    public void highlightElement() {
        ElementUtils.highlightElement(this.getWrappedElement());
    }

    public boolean visibilityOfElementWait() {
        return ElementUtils.waitForReady(this.getWrappedElement());

        // old code
        /*if (wrappedElement != null) {
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
        return false;*/
    }

    public boolean visibilityOfElementWait(int seconds) {
        if (wrappedElement != null) {
            try {
                Driver.getDefault().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                WebDriverWait wait = new WebDriverWait(Driver.getDefault(), seconds);
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

    public void clickByJs() {
        try {
            Driver.getDefault().executeScript("arguments[0].click()", wrappedElement);
        } catch (NoSuchElementException e) {
            Logger.logDebug("Element is not present when click by JS");
        }
    }

    public void focus() {
        if ("input".equals(wrappedElement.getTagName())) {
            wrappedElement.sendKeys("");
        } else {
            new Actions(Driver.getDefault())
                    .moveToElement(wrappedElement)
                    .clickAndHold()
                    .perform();
        }
    }

    public void focusJs() {
        Driver.getDefault().executeScript("arguments[0].focus();", wrappedElement);
    }

    public boolean animationWait() {
        if (wrappedElement != null) {

            try {
                WebDriverWait wait = new WebDriverWait(Driver.getDefault(), 10);
                wait.until(new Function<WebDriver, Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        String object = Driver.getDefault()
                                .executeScript(
                                        "return $(arguments[0]).is( \":animated\" );",
                                        wrappedElement).toString();
                        Logger.logInfo("Object is animated = " + object);
                        Boolean res = Boolean.valueOf(object);
                        res = !res;
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
                        String position1 = Driver.getDefault()
                                .executeScript("return $(arguments[0]).position();",
                                        wrappedElement).toString();

                        Logger.logInfo("Location 1 = " + wrappedElement.getLocation());
                        Logger.logInfo("Position 1 = " + position1);
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            Logger.logError(e.getMessage());
                        }
                        String position2 = Driver.getDefault()
                                .executeScript("return $(arguments[0]).position();",
                                        wrappedElement).toString();
                        Logger.logInfo("Location 2 = " + wrappedElement.getLocation());

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

    private void updateWrappedElement(WebElement el) {
        try {
            Field[] field2 = this.getClass().getDeclaredFields();
            java.lang.annotation.Annotation[] ann = this.getClass().getDeclaredAnnotations();
            Field field = this.getClass().getDeclaredField("wrappedElement");
            String class1 = this.getClass().toString();
            // this.wrappedElement
            field.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(this, el);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
