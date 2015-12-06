package com.epam.model.dao.impl;

import com.epam.model.dao.XlsMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class XlsHelper {

    public static synchronized void fillObject(Object object, Map<String, String> map) {
        for (Method method : object.getClass().getMethods()) {
            if (method.getName().startsWith("set")) {
                if (method.isAnnotationPresent(XlsMapping.class)) {
                    try {
                        String value = map.get(method.getAnnotation(XlsMapping.class).header());

                        if (value != null
                                && !value.isEmpty()
                                && !value.trim().equalsIgnoreCase("<null>")) {
                            method.invoke(object, value);
                        } else {
                            method.invoke(object, "");
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static synchronized void fillObject(Map<String, String> map, Object object) {
        for (Method method : object.getClass().getMethods()) {

            if (method.getName().startsWith("get")) {
                try {
                    map.put(method.getAnnotation(XlsMapping.class).header(), method.invoke(object).toString());
                } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
