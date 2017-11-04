package net.lelyak.core.common;

import net.lelyak.core.logging.Logger;
import org.testng.Assert;

import java.text.MessageFormat;

import static org.testng.Assert.assertTrue;

public final class Asserter {

    private static final String messagePattern = "Verification was not successful, expected value: \"{0}\", but obtained \"{1}\"";

    public Asserter() {
    }

    private enum ErrorLevel {
        FAIL, ERROR, PASS, INFO, WARNING, ENV, DEBUG;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asserter)) {
            return false;
        }
        return true;
    }

    public void assertEquals(Object actualObject, Object expectedObject,
                             String failMessage, String passMessage) {
        assertEquals(actualObject, expectedObject, failMessage, passMessage, ErrorLevel.FAIL);
    }

    public void assertCheckByNull(Object actualObject, String failMessage, String passMessage) {
        boolean ifNull = (actualObject != null);
        assertCondition(ifNull, failMessage, passMessage, ErrorLevel.FAIL);
    }


    public void assertEquals(Object actualObject, Object expectedObject,
                             String failMessage, String passMessage, ErrorLevel errorLevel) {
        if (actualObject == expectedObject) {
            Logger.logDebug(passMessage);
            return;
        }

        if (actualObject.equals(expectedObject)) {
            Logger.logDebug(passMessage);
        } else {
            log(MessageFormat.format(messagePattern, actualObject.toString(),
                    expectedObject.toString()), errorLevel);
            Assert.assertEquals(actualObject, expectedObject, failMessage);
        }
    }

    public void assertFail(boolean condition, String failMessage, String passMessage) {
        assertCondition(condition, failMessage, passMessage, ErrorLevel.FAIL);

    }

    public void assertError(boolean condition, String failMessage, String passMessage) {
        assertCondition(condition, failMessage, passMessage, ErrorLevel.ERROR);
    }

    public void assertPass(boolean condition, String failMessage, String passMessage) {
        assertCondition(condition, failMessage, passMessage, ErrorLevel.PASS);
    }

    public void assertInfo(boolean condition, String failMessage, String passMessage) {
        assertCondition(condition, failMessage, passMessage, ErrorLevel.INFO);
    }

    public void assertWarning(boolean condition, String failMessage, String passMessage) {
        assertCondition(condition, failMessage, passMessage, ErrorLevel.WARNING);
    }

    public void assertEnv(boolean condition, String failMessage, String passMessage) {
        assertCondition(condition, failMessage, passMessage, ErrorLevel.ENV);
    }

    public void assertDebug(boolean condition, String failMessage, String passMessage) {
        assertCondition(condition, failMessage, passMessage, ErrorLevel.DEBUG);
    }

    private void assertCondition(boolean condition, String failMessage,
                                 String passMessage, ErrorLevel level) {
        if (!condition) {
            log(failMessage, level);
            assertTrue(condition, failMessage);
        } else {
            Logger.logPass(passMessage);
        }
    }

    private void log(String message, ErrorLevel level) {
        switch (level) {
            case FAIL:
                Logger.logFail(message);
                break;
            case ERROR:
                Logger.logError(message);
                break;
            case PASS:
                Logger.logPass(message);
                break;
            case INFO:
                Logger.logInfo(message);
                break;
            case WARNING:
                Logger.logWarning(message);
                break;
            case ENV:
                Logger.logEnvironment(message);
                break;
            case DEBUG:
                Logger.logDebug(message);
                break;
        }
    }
}
