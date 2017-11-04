package net.lelyak.core.datafactory;


import net.lelyak.core.annotations.InjectRandomData;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomDataSource {

    private DataFactory dataFactory = new DataFactory();
    private Map<String, String> storedData = new HashMap<String, String>();
    private Random random = new Random();
    private String characters = "qwertyuiopasdfghjklzxcvbnm";

    public void fillEntity(Object entity) {
        if (entity != null) {
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectRandomData.class)) {

                    InjectRandomData data = field.getAnnotation(InjectRandomData.class);
                    field.setAccessible(true);

                    switch (data.type()) {
                        case NUMERIC:
                            ReflectionUtils
                                    .setField(field, entity, getNumeric(data.min(), data.max()));
                            break;

                        case STRING:
                            if (!data.join().isEmpty()) {

                                ReflectionUtils.setField(field, entity,
                                                join(data.join(), getString(data.min(), data.max())));
                                break;
                            }
                            ReflectionUtils.setField(field, entity, getString(data.min(), data.max()));
                            break;

                        case ADDRESS:
                            ReflectionUtils.setField(field, entity, getAddress());
                            break;
                        case NAME:
                            ReflectionUtils.setField(field, entity, getName());
                            break;
                        case FIRST_NAME:
                            ReflectionUtils.setField(field, entity, getFirstName());
                            break;
                        case LAST_NAME:
                            ReflectionUtils.setField(field, entity, getLastName());
                            break;
                        case BIRTH_DATE:
                            ReflectionUtils.setField(field, entity, getBirthDate());
                            break;
                        case BUSINESS_NAME:
                            ReflectionUtils.setField(field, entity, getBusinessName());
                            break;
                        case EMAIL:
                            ReflectionUtils.setField(field, entity, getEmail());
                            break;
                        case CITY:
                            ReflectionUtils.setField(field, entity, getCity());
                            break;
                        case STREET:
                            ReflectionUtils.setField(field, entity, getStreet());
                            break;
                        case TEXT:
                            ReflectionUtils.setField(field, entity, getText(data.min(), data.max()));
                            break;
                        case WORD:
                            ReflectionUtils.setField(field, entity, getWord(data.min(), data.max()));
                            break;
                        case CHARS:
                            ReflectionUtils.setField(field, entity, getChars(data.min(), data.max()));
                            break;
                        case BOOLEAN:
                            ReflectionUtils.setField(field, entity, getBoolean());
                            break;
                        case GENDER:
                            ReflectionUtils.setField(field, entity, getGender());
                            break;
                    }
                }
            }
        }
    }

    private String join(String id, String value) {
        if (storedData.containsKey(id)) {
            return storedData.get(id);
        }
        storedData.put(id, value);
        return value;
    }

    private char rand() {
        return characters.charAt(random.nextInt(characters.length()));
    }

    private String rand(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return builder.toString();
    }

    public String getNumeric(int min, int max) {
        return String.valueOf(dataFactory.getNumberBetween(min, max));
    }

    public String getString(int min, int max) {
        return dataFactory.getRandomChars(min, max);
    }

    public String getAddress() {
        return dataFactory.getAddress();
    }

    public String getName() {
        return dataFactory.getName();
    }

    public String getFirstName() {
        return dataFactory.getFirstName() + rand(3);
    }

    public String getLastName() {
        return dataFactory.getLastName() + rand(3);
    }

    public String getBirthDate() {
        return dataFactory.getBirthDate().toString();
    }

    public String getBusinessName() {
        return dataFactory.getBusinessName();
    }

    public String getEmail() {
        return dataFactory.getEmailAddress();
    }

    public String getCity() {
        return dataFactory.getCity();
    }

    public String getStreet() {
        return dataFactory.getStreetName();
    }

    public String getText(int min, int max) {
        return dataFactory.getRandomText(min, max);
    }

    public String getWord(int min, int max) {
        return dataFactory.getRandomWord(min, max);
    }

    public String getChars(int min, int max) {
        return dataFactory.getRandomChars(min, max);
    }

    public String getBoolean() {
        return String.valueOf(random.nextBoolean());
    }

    public String getGender() {
        return random.nextBoolean() ? "Male" : "Female";
    }
}
