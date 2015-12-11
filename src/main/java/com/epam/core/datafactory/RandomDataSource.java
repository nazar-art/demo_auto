package com.epam.core.datafactory;


import com.epam.core.annotations.InjectRandomData;
import com.epam.core.logging.Logger;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * usage of random generator:
 * <p>
 * CatalogueManagementDTO userDTO = GeneratorDP.getCatalogueDTO();
 * Logger.logInfo("HERE IS DESCRIPTION - " + userDTO.getDescription());
 * <p>
 * or explicitly:
 * <p>
 * RandomDataSource randomDataSource = new RandomDataSource();
 * randomDataSource.fillEntity(adminDTO);
 * <p>
 * The best usage is at filling dao with data:
 * <p>
 * public List<CatalogueManagementDTO> findListById(String id) {
 * xls = new XlsReader("AdminInputData.xlsx", "CatalogueManagement");
 * List<Map<String, String>> testData = xls.getDataListById(id);
 * <p>
 * if (testData != null && !testData.isEmpty()) {
 * List<CatalogueManagementDTO> catalogueData = new ArrayList<CatalogueManagementDTO>();
 * for (Map<String, String> dataItem : testData) {
 * CatalogueManagementDTO managementDTO = new CatalogueManagementDTO();
 * XlsHelper.fillObject(managementDTO, dataItem);
 * <p>
 * data.fillEntity(managementDTO);
 */
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

                    try {
                        switch (data.type()) {
                            case NUMERIC:
                                field.set(entity, getNumeric(data.min(), data.max()));
                                break;
                            case STRING:
                                if (data.join() != null
                                        && !data.join().isEmpty()) {

                                    field.set(entity, join(data.join(), getString(data.min(), data.max())));
                                    break;
                                }
                                field.set(entity, getString(data.min(), data.max()));
                                break;
                            case ADDRESS:
                                field.set(entity, getAddress());
                                break;
                            case NAME:
                                field.set(entity, getName());
                                break;
                            case FIRST_NAME:
                                field.set(entity, getFirstName());
                                break;
                            case LAST_NAME:
                                field.set(entity, getLastName());
                                break;
                            case BIRTH_DATE:
                                field.set(entity, getBirthDate());
                                break;
                            case BUSINESS_NAME:
                                field.set(entity, getBusinessName());
                                break;
                            case EMAIL:
//                                field.set(entity, getEmail());
                                // check spring utilities
                                ReflectionUtils.setField(field, entity, getEmail());
                                break;
                            case CITY:
                                field.set(entity, getCity());
                                break;
                            case STREET:
                                field.set(entity, getStreet());
                                break;
                            case TEXT:
                                field.set(entity, getText(data.min(), data.max()));
                                break;
                            case WORD:
                                field.set(entity, getWord(data.min(), data.max()));
                                break;
                            case CHARS:
                                field.set(entity, getChars(data.min(), data.max()));
                                break;
                            case BOOLEAN:
                                field.set(entity, getBoolean());
                                break;
                            case GENDER:
                                field.set(entity, getGender());
                                break;
                        }
                    } catch (IllegalAccessException e) {
                        Logger.logError(e.getMessage());
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
