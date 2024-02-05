package ru.ssau.patternpractice.exception;

import lombok.Getter;

@Getter
public class ModelNotFoundException extends RuntimeException {
    private static String msg = "Такой модели не существует";
}
