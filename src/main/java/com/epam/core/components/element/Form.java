package com.epam.core.components.element;

import com.epam.core.components.AbstractPageElement;
import org.openqa.selenium.WebElement;

@Deprecated
public class Form extends AbstractPageElement {

    public Form(WebElement wrappedElement, String name, String page) {
        super(wrappedElement, name, page);
    }

    // todo add possibility to chose locator type - xpath / name / css
    public Form set(String fieldId, String value) {
        findById(fieldId).sendKeys(value);
        return this;
    }

    public String getFieldValue(String fieldName) {
        return findByName(fieldName).getAttribute("value");
    }

    public void submit() {
        context.submit();
    }
}
