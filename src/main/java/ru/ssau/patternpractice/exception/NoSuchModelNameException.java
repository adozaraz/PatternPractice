package ru.ssau.patternpractice.exception;

import lombok.Getter;

@Getter
public class NoSuchModelNameException extends RuntimeException {
    private static String msg = "Такой модели не существует";
}
