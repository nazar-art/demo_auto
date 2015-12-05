package com.epam.core.components;

import com.epam.core.annotations.Description;
import com.epam.core.annotations.Page;
import com.epam.core.logging.Logger;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.*;
import java.util.List;

public class WebFieldDecorator extends DefaultFieldDecorator {

    public WebFieldDecorator(final SearchContext searchContext) {
        super(new DefaultElementLocatorFactory(searchContext));
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        Class<?> clazz = field.getType();
        Type type = null;
        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }
        if (field.getAnnotation(FindBy.class) != null) {
            if (AbstractPageElement.class.isAssignableFrom(field.getType())) {
                try {

                    WebElement proxy = proxyForLocator(loader, locator);
                    return clazz.getConstructor(WebElement.class, String.class,
                            String.class).newInstance(proxy, getName(field),
                            getPage(field));
                } catch (Exception e) {
                    Logger.logError("WebElement can't be represented as "
                            + clazz);
                    return null;
                }
            } else if (List.class.isAssignableFrom(field.getType())) {

                Type listType = field.getGenericType();

                if (listType instanceof ParameterizedType) {
                    type = ((ParameterizedType) listType)
                            .getActualTypeArguments()[0];

                    return proxyForEasWebListLocator(
                            (Class<? extends AbstractPageElement>) type,
                            loader, locator, field.getName(), getPage(field));
                }

            }
        }

        return super.decorate(loader, field);

    }

    protected List<?> proxyForEasWebListLocator(
            Class<? extends AbstractPageElement> type, ClassLoader loader,
            ElementLocator locator, String name, String pageName) {
        InvocationHandler handler = new LocatingEasyWebListHandler(type,
                locator, name, pageName);
        List<?> proxy;
        proxy = (List<?>) Proxy.newProxyInstance(loader,
                new Class[]{List.class}, handler);
        return proxy;
    }

    private String getName(Field field) {
        return field.isAnnotationPresent(Description.class) ? field
                .getAnnotation(Description.class).name() : field.getName();
    }

    private String getPage(Field field) {
        return field.getDeclaringClass().isAnnotationPresent(Page.class) ? field
                .getDeclaringClass().getAnnotation(Page.class).title()
                : "PAGE NOT DEFINED!!!";
    }

}
