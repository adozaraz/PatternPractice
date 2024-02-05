package ru.ssau.patternpractice;

import ru.ssau.patternpractice.config.AppConfig;

public class Main {
    public static void main(String[] args) {
        AppConfig config = AppConfig.getInstance();
        String fuelProperty = config.getProperty("fuel");
        String modelProperty = config.getProperty("model");
        System.out.printf("Fuel: %s Model: %s%n", fuelProperty, modelProperty);
    }
}
