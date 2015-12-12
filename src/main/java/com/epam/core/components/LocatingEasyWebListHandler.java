package com.epam.core.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LocatingEasyWebListHandler implements InvocationHandler {

    private final ElementLocator locator;
    private Class<? extends AbstractPageElement> type;
    private String name;
    private String pageName;

    public LocatingEasyWebListHandler(Class<? extends AbstractPageElement> type, ElementLocator locator,
            String name, String pageName) {
        this.locator = locator;
        this.type = type;
        this.name = name;
        this.pageName = pageName;
    }


    public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        List<WebElement> elements = locator.findElements();

        if (AbstractPageElement.class.isAssignableFrom(type)) {
            List<AbstractPageElement> frameworkElements = new ArrayList<AbstractPageElement>();

            for (WebElement element : elements) {
                frameworkElements.add(type.getConstructor(WebElement.class, String.class, String.class)
                        .newInstance(element, name, pageName));
            }

            return method.invoke(frameworkElements, objects);
        }

        return method.invoke(elements, objects);
    }

}
