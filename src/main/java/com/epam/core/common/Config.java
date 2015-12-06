package com.epam.core.common;

import org.testng.Reporter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    public static Properties config;
    private static final String PROPERTY_FILE = "project.properties";

    public static final String BROWSER = "driver";
    public static final String B_CHROME = "chrome";
    public static final String B_FF = "ff";
    public static final String B_IE = "ie";

    public static final String CHROME_PATH = "chrome.path";
    public static final String IE_PATH = "ie.path";
    public static final String TEST_HOST = "host.for.test";

    public static final String WD_THREAD_COUNT = "wd.threadCount";

    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        String propertyValue = defaultValue;
        try {
            if (config == null) {
                config = loadProperties(PROPERTY_FILE);
            }
            if (config.containsKey(key)) {
                propertyValue = config.getProperty(key);
            }
        } catch (Exception e) {
            Reporter.log("Error while reading config: " + e.getMessage(), 2, true);
        }
        return propertyValue;
    }

    public static Properties loadProperties(String path) throws Exception {
        Properties result = new Properties();
        InputStream in = Config.class.getClassLoader().getResourceAsStream(path);
        if (in == null) {
            in = new FileInputStream(path);
        }
        result.load(in);
        return result;
    }
}
