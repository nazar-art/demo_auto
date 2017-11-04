package net.lelyak.tests;

import net.lelyak.core.common.Asserter;
import net.lelyak.core.common.Config;
import net.lelyak.core.driver.Driver;
import net.lelyak.core.driver.DriverUnit;
import net.lelyak.core.driver.DriversEnum;
import net.lelyak.core.listeners.TestListener;
import net.lelyak.core.logging.Logger;
import net.lelyak.dp.BaseDP;
import net.lelyak.model.dao.modules.DAOFactory;
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

import static net.lelyak.core.driver.Driver.driver;

@ContextConfiguration(locations = {"classpath:test-context.xml"})
@Listeners({TestListener.class, org.uncommons.reportng.HTMLReporter.class,
        org.uncommons.reportng.JUnitXMLReporter.class /*, ReportPortalTestNGListener.class*/})
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

    protected final String baseUrl = Config.getProperty(Config.TEST_HOST);
    protected final String driverName = Config.getProperty(Config.BROWSER);
    protected final DriversEnum driverInstance = DriversEnum.valueOf(driverName.toUpperCase());

    public Asserter asserter = new Asserter();


    @BeforeMethod(alwaysRun = true)
    public void start() {
        Logger.logDebug("Start BEFORE Method");
        Logger.logDebug("Driver : " + driverInstance);
        try {
            driver.init();
            DriverUnit.preconfigureDriver(driver.get());

            if (driverInstance != DriversEnum.ANDROIDHYBRID && driverInstance != DriversEnum.QT) {
                driver.get().manage().deleteAllCookies();

                if (driverInstance != DriversEnum.ANDROID) {
                    Logger.logDebug("Maximize driver window");
                    driver.get().manage().window().maximize();
                }

                driver.get().get(baseUrl);
                Logger.logDebug("Navigate to:" + baseUrl);
            }
        } catch (TimeoutException e) {
            Logger.logEnvironment("Application site is not available");
            closeWindow();
        }
        Logger.logDebug("Finish BEFORE Method");
    }

    @AfterMethod(alwaysRun = true)
    public void end() {
        Logger.logDebug("Start AFTER CLASS");
        if (driverInstance == DriversEnum.ANDROIDHYBRID || driverInstance == DriversEnum.IE) {
            Logger.logDebug("We are closing application");
            if (Driver.getDefault() != null) {
                try {
                    Driver.getDefault().quit();
                } catch (UnreachableBrowserException e) {
                    logger.debug("UnreachableBrowser on close");
                } finally {
                    driver.remove();
                }
            }
        } else {
            closeWindow();
        }
        Logger.logDebug("Finish AFTER CLASS");
    }


    public void closeWindow() {
        Logger.logDebug("We need to close window");
        if (Driver.getDefault() != null) {
            try {
                Set<String> windowHandles = Driver.getDefault().getWindowHandles();
                if (windowHandles != null && !windowHandles.isEmpty()) {
                    if (windowHandles.size() == 1) {
                        Driver.getDefault().quit();
                        return;
                    }
                    for (String windowId : windowHandles) {
                        if (Driver.getDefault() != null) {
                            Driver.getDefault().switchTo().window(windowId);
                            closeBrowser();
                        } else {
                            Logger.logEnvironment("There is no window opened");
                        }
                    }
                }
            } catch (UnreachableBrowserException e) {
                logger.debug("UnreachableBrowser on close");
            } finally {
                driver.remove();
            }
        }
    }

    private void closeBrowser() {
        try {
            Logger.logDebug("We are closing window");
            if (driverInstance == DriversEnum.ANDROID) {
                Driver.getDefault().quit();
            } else {
                Driver.getDefault().close();
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
