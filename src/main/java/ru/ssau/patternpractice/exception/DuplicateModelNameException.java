package ru.ssau.patternpractice.exception;


public class DuplicateModelNameException extends Exception {
    private static String msg = "Такая модель уже существует";

    public static String getMsg() {
        return msg;
    }
}
