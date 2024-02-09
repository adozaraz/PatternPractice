package ru.ssau.patternpractice.exception;

public class PropertyNotFoundException extends RuntimeException {
    private static String msg = "Такого свойства не существует";

    public static String getMsg() {
        return msg;
    }
}
