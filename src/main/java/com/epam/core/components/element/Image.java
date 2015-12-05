package com.epam.core.components.element;

import com.epam.core.components.AbstractPageElement;
import org.openqa.selenium.WebElement;

public class Image extends AbstractPageElement {

    public Image(WebElement wrappedElement, String name, String page) {
        super(wrappedElement, name, page);
    }

    public String getSource() {
        return getWrappedElement().getAttribute("src");
    }

    public String getAlt() {
        return getWrappedElement().getAttribute("alt");
    }

    public void click() {
        getWrappedElement().click();
    }
}
