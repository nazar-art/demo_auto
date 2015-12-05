package com.epam.core.common;

import com.epam.core.driver.Driver;
import com.epam.core.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;


public class CoreElement implements SearchContext {

    private By frame;
    private WebElement parent;

    protected Logger logger;

    public CoreElement() {

    }

    public CoreElement(By frame) {
        this.frame = frame;

    }

    public CoreElement(WebElement parent) {
        this.parent = parent;

    }

    public CoreElement(By frame, WebElement parent) {
        this.frame = frame;
        this.parent = parent;
    }

    private void switchContext() {
        SearchContext context = this.parent;
        if (this.parent == null) {

            Driver.driver.get().switchTo().defaultContent();
            context = Driver.driver.get();
        }
        if (this.frame != null) {
            Driver.driver.get().switchTo().frame(context.findElement(frame));

        }
    }

    public WebElement findElement(By arg0) {
        switchContext();
        return Driver.driver.get().findElement(arg0);
    }

    public List<WebElement> findElements(By arg0) {
        switchContext();
        return Driver.driver.get().findElements(arg0);

    }

    // verify if element comes to existance in specified time
    public boolean exists(int timeoutMiliseconds) {
        // TODO: implement
        return false;
    }
}
