package ru.ssau.patternpractice.config;

import ru.ssau.patternpractice.exception.PropertyNotFoundException;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static AppConfig INSTANCE = null;
    private Properties properties;

    private AppConfig() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AppConfig getInstance() {
        if (INSTANCE == null) {
            System.out.println("Creating instance");
            INSTANCE = new AppConfig();
        }
        System.out.println("Getting instance");
        return INSTANCE;
    }

    public String getProperty(String property) {
        String resultProperty = properties.getProperty(property);
        if (resultProperty == null) {
            throw new PropertyNotFoundException();
        }
        return resultProperty;
    }
}
