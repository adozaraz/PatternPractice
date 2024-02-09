package ru.ssau.patternpractice.model.factory;

import ru.ssau.patternpractice.model.Automobile;
import ru.ssau.patternpractice.model.Transport;

public class AutoFactory implements TransportFactory {
    @Override
    public Transport createInstance(String brand, int modelAmount) {
        return new Automobile(brand, modelAmount);
    }
}
