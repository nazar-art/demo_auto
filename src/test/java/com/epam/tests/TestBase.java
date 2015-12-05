package com.epam.tests;

import com.epam.core.common.Asserter;
import com.epam.core.common.Config;
import com.epam.core.driver.Driver;
import com.epam.core.listeners.TestListener;
import com.epam.core.logging.Logger;
import com.epam.dp.BaseDP;
import com.epam.model.dao.impl.DAOFactory;
import com.epam.ta.reportportal.listeners.testng.ReportPortalTestNGListener;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@ContextConfiguration(locations = {"classpath:test-context.xml"})
@Listeners({TestListener.class, ReportPortalTestNGListener.class})
public class TestBase extends AbstractTestNGSpringContextTests {

    @Autowired
    private DAOFactory daoFactory;

    @PostConstruct
    public void init() {
        if (BaseDP.daoFactory == null) {
            BaseDP.daoFactory = daoFactory;
        }
    }

    static {
        System.setProperty("spring.profiles.active", "defaultTest");
    }

    public final static String baseUrl = Config.getProperty(Config.TEST_HOST);

    public Asserter asserter = new Asserter();

    public static String driverName;

    @BeforeMethod(alwaysRun = true)
    public void start() {
        Logger.logDebug("Start BEFORE Class");
        try {
            Driver.driver.init();
            driverName = Config.getProperty(Config.BROWSER);
            Logger.logDebug("Driver : " + Config.getProperty(Config.BROWSER));
            Driver.driver.get().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

            if (!driverName.equalsIgnoreCase("ANDROIDHYBRID")) {
                Driver.driver.get().manage().deleteAllCookies();

                if (!driverName.equalsIgnoreCase("ANDROID")) {
                    Driver.driver.get().manage().window().maximize();
                    // native screen size at Jenkins
//					driver.get().manage().window().setSize(new Dimension(1040, 784));

                    Logger.logInfo(String.format("start height %s width %s",
                            Driver.driver.get().manage().window().getSize().height,
                            Driver.driver.get().manage().window().getSize().width));
                }
                Driver.driver.get().get(baseUrl);
            }
        } catch (TimeoutException e) {
            Logger.logEnvironment("Command Centre site is not available");
            closeWindow();
        }
        Logger.logDebug("Finish BEFORE Class");
    }

    @AfterMethod(alwaysRun = true)
    public void end() {
        Logger.logDebug("Start AFTER CLASS");
        if (driverName.equalsIgnoreCase("ANDROIDHYBRID")) {
            Logger.logDebug("We are closing application");
            if (Driver.driver.get() != null) {
                try {
                    Driver.driver.get().quit();
                } catch (UnreachableBrowserException e) {
                    logger.debug("UnreachableBrowser on close");
                } finally {
                    Driver.driver.remove();
                }
            }
        } else {
            closeWindow();
        }
        Logger.logDebug("Finish AFTER CLASS");
    }


    public void closeWindow() {
        Logger.logDebug("We need to close window");
        if (Driver.driver.get() != null) {
            try {
                Set<String> windowHandles = Driver.driver.get().getWindowHandles();
                if (windowHandles != null && !windowHandles.isEmpty()) {
                    if (windowHandles.size() == 1) {
                        Driver.driver.get().quit();
                        return;
                    }
                    for (String windowId : windowHandles) {
                        if (Driver.driver.get() != null) {
                            Driver.driver.get().switchTo().window(windowId);
                            closeBrowser();
                        } else {
                            Logger.logEnvironment("There is no window opened");
                        }
                    }
                }
            } catch (UnreachableBrowserException e) {
                logger.debug("UnreachableBrowser on close");
            } finally {
                Driver.driver.remove();
            }
        }
    }

    private void closeBrowser() {
        try {
            Logger.logDebug("We are closing window");
            if (driverName.equalsIgnoreCase("ANDROID")) {
                Driver.driver.get().quit();
            } else {
                Driver.driver.get().close();
            }
        } catch (UnreachableBrowserException e) {
            logger.debug("UnreachableBrowser on close");
        }
    }

    public void restartDriver() {
        end();
        start();
    }

}
