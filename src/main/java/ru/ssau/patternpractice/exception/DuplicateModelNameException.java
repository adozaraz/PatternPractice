package ru.ssau.patternpractice.exception;

import lombok.Getter;

@Getter
public class DuplicateModelNameException extends Exception {
    private static String msg = "Такая модель уже существует";
}
