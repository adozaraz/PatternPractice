package ru.ssau.patternpractice.model;

public interface TransportFactory {
    Transport createInstance(String brand, int modelAmount);
}
