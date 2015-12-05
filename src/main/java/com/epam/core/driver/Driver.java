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
//    public final static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();
//    public final static ThreadLocal<ChromeDriverService> chromeService = new ThreadLocal<ChromeDriverService>();

    //	public static RemoteWebDriver driver;
    public static void getUrl(String url) {
        driver.get().get(url);
    }

    public static WebElement findElementById(String id) {
        return driver.get().findElement(By.id(id));
    }

    public static RemoteWebDriver getDefault() {
        return driver.get();
    }
}
