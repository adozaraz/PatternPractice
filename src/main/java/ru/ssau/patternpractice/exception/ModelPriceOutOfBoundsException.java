package ru.ssau.patternpractice.exception;

public class ModelPriceOutOfBoundsException extends RuntimeException {
    private static final String msg = "Неправльно задана цена модели";

    public static String getMsg() {
        return msg;
    }
}
