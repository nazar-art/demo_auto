package net.lelyak.core.driver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import net.lelyak.core.logging.Logger;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import net.lelyak.core.common.Config;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public enum DriversEnum {

    FF {
        @Override
        public RemoteWebDriver getDriver() {
            FirefoxDriverManager.getInstance().setup();

            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            FirefoxProfile firefoxProfile = new FirefoxProfile();
            firefoxProfile.setPreference("browser.cache.disk.enable", false);
            firefoxProfile.setPreference("browser.cache.memory.enable", false);
            firefoxProfile.setPreference("browser.cache.offline.enable", false);
            firefoxProfile.setPreference("network.http.use-cache", false);
            firefoxProfile.setPreference("browser.download.folderList", 2);
            firefoxProfile.setPreference("intl.accept_languages", "en-US");
            firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
//            firefoxProfile.setPreference("browser.download.dir", "D:\\");
            firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv, application/pdf");

            capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
            return new FirefoxDriver(capabilities);
        }
    },

    ANDROIDHYBRID {
        @Override
        public RemoteWebDriver getDriver() {
            try {
                DesiredCapabilities capabilities = DesiredCapabilities.android();
                capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "4.4");
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
//                capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "com.easyfinancial.indirect.mobile");
//                capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, ".MobileApp");

                URL url = new URL("http://127.0.0.1:4723/wd/hub");

                AppiumDriver<WebElement> driver = null;
                try {
                    driver = new AndroidDriver<WebElement>(url, capabilities);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(driver.getContext());
                Set<String> contextNames = driver.getContextHandles();
                for (String contextName : contextNames) {
                    System.out.println(contextName);
                    try {

                        if (contextName.contains("WEBVIEW")) {
                            driver.context(contextName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        driver.context("WEBVIEW_0");
                    }
                }
                return driver;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    },

    ANDROID {
        @Override
        public RemoteWebDriver getDriver() {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "4.4");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Browser");
            capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "100");
//            capabilities.setCapability(MobileCapabilityType.DEVICE_READY_TIMEOUT, "30");
            URL url;
            try {
                url = new URL("http://127.0.0.1:4723/wd/hub");

                RemoteWebDriver remoteWebDriver = new RemoteWebDriver(url,
                        capabilities);
                return remoteWebDriver;
            } catch (MalformedURLException e) {

                e.printStackTrace();
            }
            return null;
        }
    },

    CHROME {
        @Override
        public RemoteWebDriver getDriver() {
            ChromeDriverManager.getInstance().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            return new ChromeDriver(capabilities);
        }
    },

    IE {
        @Override
        public RemoteWebDriver getDriver() {
            InternetExplorerDriverManager.getInstance().setup();

//            File ieFile = new File(Config.getProperty(Config.IE_PATH));
//            System.setProperty("webdriver.ie.driver", ieFile.getAbsolutePath());

            DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            capabilities.setJavascriptEnabled(true);
            capabilities.setCapability("unexpectedAlertBehaviour", "accept");
            capabilities.setCapability("ignoreProtectedModeSettings", true);
            capabilities.setCapability("disable-popup-blocking", true);
            capabilities.setCapability("enablePersistentHover", true);
            capabilities.setVersion("IE11");
            return new InternetExplorerDriver(capabilities);
        }
    },

    QT {
        @Override
        public RemoteWebDriver getDriver() {
            RemoteWebDriver remoteWebDriver = null;

            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("maximize", true);
            //specify reuseUI to have WebDriver terminate any previous session and reuse its windows
            cap.setCapability("reuseUI", true);

            //specify to select the first found window
            cap.setCapability("browserStartWindow", "*");

            LoggingPreferences logs = new LoggingPreferences();
            Level level = Level.ALL;
            logs.enable(LogType.DRIVER, level);
            logs.enable(LogType.BROWSER, level);
            logs.enable(LogType.PERFORMANCE, level);
            // specify log level
            cap.setCapability(CapabilityType.LOGGING_PREFS, logs);

            try {
//                URL url = new URL("http://localhost:9517");
//                URL url = new URL(Config.getProperty("qt.app.url"));
                URL url = new URL("http://localhost:9530");
                remoteWebDriver = new RemoteWebDriver(url, cap);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return remoteWebDriver;
        }
    };

    public abstract RemoteWebDriver getDriver();
}
