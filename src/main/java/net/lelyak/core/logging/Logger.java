package net.lelyak.core.logging;

import net.lelyak.core.common.HTMLReport;
import net.lelyak.core.driver.Driver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

public final class Logger {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("WD");
    private static LogLevel logLevel;
    private static boolean isTest = false;
    private static boolean isHidden = false;

    private Logger() {
    }

    public enum LogLevel {

        INFO(1), PASS(2), WARNING(3), ENV(4), FAIL(5), ERROR(6);

        public final int level;

        LogLevel(int logLevel) {
            level = logLevel;
        }
    }

    public static String makeScreenshot() {
        String fileName = System.currentTimeMillis() + "Test";
        File screenshot = Driver.getDefault().getScreenshotAs(OutputType.FILE);
        File outputFile = new File("LoggerScreenshots/" + fileName + ".png");

//        System.out.println(outputFile.getAbsolutePath());
        Logger.logInfo("SAVED SCREENSHOT - " + outputFile.getAbsolutePath());

        try {
            FileUtils.copyFile(screenshot, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HTMLReport.createThumbnail(outputFile, fileName);
        return outputFile.getName();
    }

    public static void logPass(String message) {
        logMessage(message, "PASS:");
        verifyLogLevel(LogLevel.PASS);
    }

    public static void logFail(String message) {
        logMessage(message, "FAIL:");
        verifyLogLevel(LogLevel.FAIL);

        try {
            HTMLReport.addScreenshot(makeScreenshot());
        } catch (org.openqa.selenium.WebDriverException e) {
            Logger.logInfo(e.getMessage());
        }

        if (isTest) {
            hideHtmlDetails();
        }
    }

    public static void logInfo(String message) {
        logMessage(message, "INFO:");
        verifyLogLevel(LogLevel.INFO);
    }

    public static void logEnvironment(String message) {
        logMessage(message, "ENVIRONMENT:");
        verifyLogLevel(LogLevel.ENV);
    }

    public static void logError(String message) {
        logMessage(message, "ERROR:");
        verifyLogLevel(LogLevel.ERROR);

        if (isTest) {
            hideHtmlDetails();
        }
    }

    public static void logWarning(String message) {
        logMessage(message, "WARNING:");
        verifyLogLevel(LogLevel.WARNING);
    }

    public static void logSkipped(String message) {
        logMessage(message, "SKIPPED:");
    }

    public static void logDebug(String message) {
        logMessage(message, "DEBUG:");
    }

    private static void logMessage(String message, String level) {
        if (level.equalsIgnoreCase("PASS:")) {
            logger.info(LogMarkers.MARKER_PASS, message);
        }
        if (level.equalsIgnoreCase("ERROR:")) {
            logger.error(LogMarkers.MARKER_ERROR, message);
        }
        if (level.equalsIgnoreCase("FAIL:")) {
            logger.error(LogMarkers.MARKER_FAIL, message);
        }
        if (level.equalsIgnoreCase("DEBUG:")) {
            logger.debug(LogMarkers.MARKER_DEBUG, message);
        }
        if (level.equalsIgnoreCase("INFO:")) {
            logger.info(LogMarkers.MARKER_INFO, message);
        }
        Reporter.log(htmlMessage("", level, message));
        if (isTest) {
            hideHtmlDetails();
        }
    }

    public static void hideHtmlDetails() {
        if (!isHidden) {
            Reporter.log("<script type='text/javascript'>"
                    + "$(document).ready(function(){"
                    + "$('.debug').hide();"
                    + "$('.reporter-method-div').first().before('<input type=\"checkbox\" class=\"showDebug\"/><label for=\"showDebug\">Show debug details</label>');"
                    + "if(typeof(imagePreview) == \"function\"){"
                    + "imagePreview();"
                    + "}"
                    + "$('.reporter-method-output-div').hide();"
                    + "$('.showDebug').click( function() {"
                    + "debugOption();"
                    + "});"
                    + "$('.reporter-method-div').click( function() {"
                    + "$(this).find('.reporter-method-output-div').slideToggle('slow');"
                    + "debugOption();"
                    + "});"
                    + "});"
                    + "this.debugOption = function() { "
                    + "$('.showDebug').is(':checked') ? $('.debug').slideDown('slow') : $('.debug').slideUp('slow');"
                    + "};" + "</script>");
            isHidden = true;
        }
    }

    private static String htmlMessage(String date, String level, String message) {
        String pattern = "<div class=\"{4}\"><b><font color = \"{0}\">{1} {2} {3}</font></b></div>";

        if (level.equalsIgnoreCase("PASS:")) {
            return MessageFormat.format(pattern, "green", date, level, message, "pass");
        }
        if (level.equalsIgnoreCase("ERROR:") || level.equalsIgnoreCase("FAIL:")) {
            return MessageFormat.format(pattern, "red", date, level, message, "error");
        }
        if (level.equalsIgnoreCase("SKIPPED:")) {
            return MessageFormat.format(pattern, "yellow", date, level, message, "skipped");
        }
        if (level.equalsIgnoreCase("DEBUG:")) {
            return MessageFormat.format(pattern, "LightGray", date, level, message, "debug");
        } else {
            return MessageFormat.format(pattern, "black", date, level, message, "other");
        }
    }

    public static LogLevel getLogLevel() {
        return logLevel;
    }

    public static boolean isTest() {
        return isTest;
    }

    public static void setTest(boolean test) {
        isTest = test;
    }

    private static void verifyLogLevel(LogLevel logLevel) {
        if (Logger.logLevel == null) {
            Logger.logLevel = logLevel;
        } else if (Logger.logLevel.level < logLevel.level) {
            Logger.logLevel = logLevel;
        }
    }
}
