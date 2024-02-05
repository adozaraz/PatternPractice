package ru.ssau.patternpractice.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PropertyNotFoundException extends RuntimeException {
    private static String msg = "Такого свойства не существует";
}
