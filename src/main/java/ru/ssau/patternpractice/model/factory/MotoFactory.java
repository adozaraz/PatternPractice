package ru.ssau.patternpractice.model.factory;

import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.model.Motorcycle;
import ru.ssau.patternpractice.model.Transport;

public class MotoFactory implements TransportFactory {
    @Override
    public Transport createInstance(String brand, int modelAmount) throws DuplicateModelNameException {
        return new Motorcycle(brand, modelAmount);
    }
}
