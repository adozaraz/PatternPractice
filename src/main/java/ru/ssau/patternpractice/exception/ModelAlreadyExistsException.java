package ru.ssau.patternpractice.exception;

import lombok.Getter;

@Getter
public class ModelAlreadyExistsException extends RuntimeException {
    private static String msg = "Такая модель уже существует";
}