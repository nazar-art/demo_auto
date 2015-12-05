package com.epam.model.dao.impl;

import com.epam.model.dao.CsvMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class CsvHelper {
    public static synchronized void fillObject(Object object, Map<String, String> map) {
        for (Method method : object.getClass().getMethods()) {
            if (method.getName().startsWith("set")) {
                if (method.isAnnotationPresent(CsvMapping.class)) {
                    try {
                        method.invoke(object, map.get(method.getAnnotation(CsvMapping.class).header()));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
