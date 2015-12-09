package com.epam.core.driver;

import com.epam.core.common.Config;
import com.epam.core.parallel.WebDriverPool;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.RemoteWebDriver;


public class Driver {

    public final static WebDriverPool driver = new WebDriverPool(Config.getIntProperty(Config.WD_THREAD_COUNT));

    public final static ThreadLocal<ChromeDriverService> chromeService = new ThreadLocal<ChromeDriverService>();

    public static RemoteWebDriver getDefault() {
        return driver.get();
    }

    public static void getUrl(String url) {
        getDefault().get(url);
    }

    public static WebElement findById(String id) {
        return getDefault().findElement(By.id(id));
    }

    public static WebElement findByXpath(String xpathSelector) {
//        Logger.logInfo("SEARCHING FOR XPATH: " + xpathSelector);
        return getDefault().findElement(By.xpath(xpathSelector));
    }

    public static WebElement findByCss(String cssSelector) {
        return getDefault().findElement(By.cssSelector(cssSelector));
    }


}
