package net.lelyak.model.dao.modules;

import net.lelyak.core.logging.Logger;
import net.lelyak.model.dao.XlsMapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class XlsHelper {

    public static synchronized void fillObject(Object object, Map<String, String> map) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(XlsMapping.class)) {
                try {
                    field.setAccessible(true);
                    String value = map.get(field.getAnnotation(XlsMapping.class).header());

                    if (value != null
                            && !value.isEmpty()
                            && !value.trim().equalsIgnoreCase("<null>")) {

                        field.set(object, value);
                    } else {
                        field.set(object, "");
                    }
                } catch (IllegalAccessException e) {
                    Logger.logError(e.getMessage());
                }
            }
        }
    }

    @Deprecated // todo for usage should be updated => set value to field directly
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
