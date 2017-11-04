package net.lelyak.pages;

import net.lelyak.core.components.WebFieldDecorator;
import net.lelyak.core.components.element.Button;
import net.lelyak.core.components.element.TextBox;
import net.lelyak.core.driver.Driver;
import net.lelyak.core.exceptions.NavigationPathNotFoundException;
import net.lelyak.core.exceptions.PageNotFoundException;
import net.lelyak.core.logging.Logger;
import net.lelyak.pages.abibas.WelcomePage;
import com.google.common.base.Function;
import org.apache.commons.lang.SerializationUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PageObject implements INavigationPage {

    protected Class<? extends PageObject> startPage = WelcomePage.class;
    private RemoteWebDriver driver = Driver.getDefault();
    private Map<Class<? extends PageObject>, Set<Class<? extends PageObject>>> siteMap = SiteMap.getSiteMap();
    public static Map<List<Class<? extends PageObject>>, AtomicInteger> pathReport = new HashMap<List<Class<? extends PageObject>>, AtomicInteger>();
    protected Map<Class<? extends PageObject>, Button> successor = null;
    protected static Map<String, LinkedList<Class<? extends PageObject>>> wayCache = new HashMap<String, LinkedList<Class<? extends PageObject>>>();

    public boolean invoke() {
        if (!this.exist()) {
            try {
                if (startPage == null || !startPage.newInstance().exist()) {
                    startPage = getOpenedPage(siteMap);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            Logger.logDebug("StartPage==" + startPage);
            LinkedList<Class<? extends PageObject>> way = getShortWay(
                    startPage, this.getClass());
            if (way != null) {
                startPage = way.getLast();
                passWay(way, null);
            } else {
                if (!startPage.getSimpleName().equals(
                        getClass().getSimpleName())) {
                    throw new NavigationPathNotFoundException(
                            MessageFormat
                                    .format("The following path is not found: from {0} to {1}",
                                            startPage.getSimpleName(),
                                            getClass().getSimpleName()));
                }
            }
        } else {
            return true;
        }
        return false;

    }

    public boolean invoke(Class<? extends PageObject> maibyStartPage) {
        startPage = maibyStartPage;
        return invoke();
    }

    private void passWay(LinkedList<Class<? extends PageObject>> way, Map<String, String> businessData) {
        do {
            try {
                PageObject page = way.getFirst().newInstance();
                if (page.successor.containsKey(way.get(1))) {
                    page.successor.get(way.get(1)).click();
                } else {
                    siteMap = SiteMap.resetSiteMap();
                    way = getShortWay(getOpenedPage(siteMap), this.getClass());
                    way.getFirst().newInstance().successor.get(way.get(1)).click();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            way.removeFirst();
        } while (way.size() > 1);
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @SuppressWarnings("unchecked")
    private LinkedList<Class<? extends PageObject>> getShortWay(
            Class<? extends PageObject> pageStart,
            Class<? extends PageObject> pageEnd) {
        if (pageStart.equals(pageEnd)) {
            return null;
        }
        String wayKey = startPage.getSimpleName() + pageEnd.getSimpleName();
        if (wayCache.containsKey(wayKey)) {
            updateStatistic(wayKey);
            showStatistic();
            return (LinkedList<Class<? extends PageObject>>) SerializationUtils
                    .clone(wayCache.get(wayKey));
        }
        List<Class<? extends PageObject>> way;
        // list for pages which was visited
        List<Class<? extends PageObject>> used = new ArrayList<Class<? extends PageObject>>();
        // list of pages that are currently browsing
        LinkedList<Class<? extends PageObject>> currentlyPages = new LinkedList<Class<? extends PageObject>>();
        // Map for to save a previous page
        HashMap<Class<? extends PageObject>, Class<? extends PageObject>> previous = new HashMap<Class<? extends PageObject>, Class<? extends PageObject>>();
        used.add(pageStart);
        previous.put(pageStart, null);
        currentlyPages.add(pageStart);
        while (!currentlyPages.isEmpty()) {
            Class<? extends PageObject> page = currentlyPages.getFirst();
            currentlyPages.removeFirst();
            Set<Class<? extends PageObject>> listPages = siteMap.get(page); // may
            // we
            // need
            // to
            // remove
            // first
            for (Class<? extends PageObject> pageTo : listPages) {
                if (!used.contains(pageTo)) {
                    used.add(pageTo);
                    currentlyPages.add(pageTo);
                    previous.put(pageTo, page);
                    if (pageTo.equals(pageEnd)) {
                        // algorithm for constructing the way
                        way = new LinkedList<Class<? extends PageObject>>();
                        way.add(pageTo);
                        do {
                            Class<? extends PageObject> previousPage = previous
                                    .get(pageTo);
                            way.add(previousPage);
                            pageTo = previousPage;
                        } while (previous.get(pageTo) != null);
                        Collections.reverse(way);
                        wayCache.put(wayKey,
                                (LinkedList<Class<? extends PageObject>>) way);
                        pathReport.put(way, new AtomicInteger(1));
                        showStatistic();
                        return (LinkedList<Class<? extends PageObject>>) SerializationUtils.clone(wayCache.get(wayKey));
                    }
                }
            }
        }
        return null;
    }

    private void updateStatistic(String wayKey) {
        LinkedList<Class<? extends PageObject>> path = wayCache.get(wayKey);
        if (path != null && pathReport.containsKey(path)) {
            pathReport.get(path).incrementAndGet();
        }
    }

    private void showStatistic() {
        for (Map.Entry<List<Class<? extends PageObject>>, AtomicInteger> entry : pathReport.entrySet()) {
            Logger.logDebug(entry.getKey().toString() + " - " + entry.getValue());
        }
    }

    private Class<? extends PageObject> getOpenedPage(
            Map<Class<? extends PageObject>, Set<Class<? extends PageObject>>> siteMap) {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);
        for (Class<? extends PageObject> startPage : siteMap.keySet()) {
            try {
                if (startPage.newInstance().exist()) {
                    driver.manage().timeouts()
                            .implicitlyWait(15, TimeUnit.SECONDS);
                    return startPage;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new PageNotFoundException("Current page is not found");
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }

    public abstract boolean exist();

    public void invokeSuccessor(HashMap<String, String> businessData,
                                Class<? extends PageObject> page) {

    }

    public void invoke(String url) {
        getDriver().get(url);
    }

    public boolean invoke(List<Class<? extends PageObject>> necessaryPages,
                          Map<String, String> businessData) {
        if (this.exist())
            return true;
        // HashMap<Class<? extends PageObject>, List<Class<? extends
        // PageObject>>> siteMap = getSiteMap();
        Class<? extends PageObject> startPage = getOpenedPage(siteMap);

        LinkedList<Class<? extends PageObject>> way = null;
        if (necessaryPages != null) {
            do {
                if (way == null) {
                    way = getShortWay(
                            startPage,
                            ((LinkedList<Class<? extends PageObject>>) necessaryPages)
                                    .getFirst());
                } else {
                    way.addAll(getShortWay(
                            startPage,
                            ((LinkedList<Class<? extends PageObject>>) necessaryPages)
                                    .getFirst()));
                }
                way.removeLast();
                startPage = ((LinkedList<Class<? extends PageObject>>) necessaryPages)
                        .getFirst();
                ((LinkedList<Class<? extends PageObject>>) necessaryPages)
                        .removeFirst();
            } while (necessaryPages.size() > 0);
            way.addAll(getShortWay(startPage, this.getClass()));
        } else if (necessaryPages == null) {
            way = getShortWay(startPage, this.getClass());
        }
        if (way != null)
            passWay(way, businessData);
        return true;
    }

    public boolean isPageNameEquals(TextBox e, String namePage) {
        if (e != null && e.isPresent()) {
            return e.getText().equals(namePage);
        }
        return false;
    }

    public boolean waitPageLoad() {
        Wait<WebDriver> wait;
        wait = new FluentWait<WebDriver>(getDriver())
                .withTimeout(80, TimeUnit.SECONDS)
                .pollingEvery(2, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        try {
            return wait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {

                    return exist();
                }
            });
        } catch (TimeoutException e) {
            Logger.logDebug("page not load; class = " + this.getClass().getName());
            Logger.logWarning("page not load; class = " + this.getClass().getName());
            Logger.makeScreenshot();
            return false;
        } catch (UnhandledAlertException e) {
            Logger.logFail("unhandled alert: " + e.getAlertText());
            return false;
        }
    }
}
