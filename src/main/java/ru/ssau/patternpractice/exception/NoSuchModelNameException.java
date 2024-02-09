package ru.ssau.patternpractice.exception;

public class NoSuchModelNameException extends Exception {
    private static String msg = "Такой модели не существует";

    public static String getMsg() {
        return msg;
    }
}
