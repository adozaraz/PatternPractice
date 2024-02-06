package ru.ssau.patternpractice.exception;

import lombok.Getter;

@Getter
public class ModelPriceOutOfBoundsException extends RuntimeException {
    private static final String msg = "Неправльно задана цена модели";
}
