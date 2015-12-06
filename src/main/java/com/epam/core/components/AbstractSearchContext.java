package com.epam.core.components;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AbstractSearchContext<T extends SearchContext> {

    protected T context;

    public AbstractSearchContext(T context) {
        this.context = context;
    }

    protected WebElement findByXPath(String xpathLocator) {
        return context.findElement(By.xpath(xpathLocator));
    }

    protected List<WebElement> findAllByXPath(String xpathLocator) {
        return context.findElements(By.xpath(xpathLocator));
    }

    protected WebElement findById(String id) {
        return context.findElement(By.id(id));
    }

    protected WebElement findByCss(String cssLocator) {
        return context.findElement(By.cssSelector(cssLocator));
    }

    protected WebElement findByLink(String linkText) {
        return context.findElement(By.linkText(linkText));
    }

    protected WebElement findByName(String fieldName) {
        return context.findElement(By.name(fieldName));
    }
}
