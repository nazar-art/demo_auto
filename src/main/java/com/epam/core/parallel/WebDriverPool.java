package com.epam.core.parallel;

import com.epam.core.common.Config;
import com.epam.core.driver.DriversEnum;
import com.epam.core.logging.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public final class WebDriverPool extends ThreadLocal<RemoteWebDriver> {

    private static AtomicInteger maxThreadCount = new AtomicInteger();
    private static AtomicInteger poolSize = new AtomicInteger();
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unPaused = pauseLock.newCondition();

    public WebDriverPool(int threadCount) {
        maxThreadCount.set(threadCount);
    }

    public void init() {
        pauseLock.lock();
        try {
            Logger.logDebug("Init>> pool size: " + poolSize.get());
            while (maxThreadCount.get() <= poolSize.get()) {
                Logger.logDebug("Thread is waiting");
                unPaused.await();
            }
            set(DriversEnum.valueOf(Config.getProperty(Config.BROWSER).toUpperCase()).getDriver());
            Logger.logInfo("Set browser");
            poolSize.getAndIncrement();
            Logger.logDebug("get increment");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pauseLock.unlock();
        }
    }

    @Override
    public void remove() {
        pauseLock.lock();
        try {
            super.remove();
            poolSize.decrementAndGet();
            unPaused.signalAll();
            Logger.logDebug("WebDriver is removed from the pool: " + poolSize.get());
        } finally {
            pauseLock.unlock();
        }
    }
}
