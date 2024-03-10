package ru.ssau.patternpractice.model.factory;

import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.model.Transport;

public interface TransportFactory {
    Transport createInstance(String brand, int modelAmount) throws DuplicateModelNameException;
    Transport createSynchronizedInstance(String brand, int modelAmount) throws DuplicateModelNameException;
}
