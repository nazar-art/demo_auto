package com.epam.pages;

import com.epam.core.driver.Driver;
import com.epam.core.logging.Logger;
import com.epam.pages.adidas.WelcomePage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SiteMap {

    private static Map<Class<? extends PageObject>, Set<Class<? extends PageObject>>> siteMap;

    private SiteMap() {
    }

    public static synchronized Map<Class<? extends PageObject>, Set<Class<? extends PageObject>>> resetSiteMap() {
        Class<? extends PageObject> pageStart = WelcomePage.class;
        siteMap = new HashMap<>();
        LinkedList<Class<? extends PageObject>> currentlyPages = new LinkedList<>();
        try {
            Driver.driver.get().manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);
            PageObject pageStartInst = pageStart.newInstance();
            siteMap.put(pageStart, pageStartInst.successor.keySet());
            currentlyPages.add(pageStart);
            while (!currentlyPages.isEmpty()) {
                Class<? extends PageObject> page = currentlyPages.getLast();
                currentlyPages.removeLast();
                PageObject pageInst = page.newInstance();
                if (pageInst.successor != null) {
                    Set<Class<? extends PageObject>> listPages = pageInst.successor.keySet();
                    for (Class<? extends PageObject> pageTo : listPages) {
                        if (!siteMap.containsKey(pageTo)) {
                            currentlyPages.add(pageTo);
                            PageObject pageToInst = pageTo.newInstance();
                            siteMap.put(pageTo, pageToInst.successor.keySet());
                        }
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            Logger.logError(e.getMessage());
        } finally {
            Driver.driver.get().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        }
        return siteMap;
    }

    public static Map<Class<? extends PageObject>, Set<Class<? extends PageObject>>> getSiteMap() {
        if (siteMap == null) {
            resetSiteMap();
        }
        return siteMap;
    }


}
