package com.epam.model.dao.modules;

import com.epam.core.logging.Logger;
import com.epam.model.dao.CsvMapping;

import java.lang.reflect.Field;
import java.util.Map;

public class CsvHelper {

    // todo test this method before usage
    public static synchronized void fillObject(Object object, Map<String, String> map) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(CsvMapping.class)) {
                try {
                    field.setAccessible(true);
                    String value = map.get(field.getAnnotation(CsvMapping.class).header());

                    field.set(object, value);
                } catch (IllegalAccessException e) {
                    Logger.logError(e.getMessage());
                }
            }
        }
    }

}
