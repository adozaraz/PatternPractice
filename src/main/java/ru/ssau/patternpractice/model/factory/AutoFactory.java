package ru.ssau.patternpractice.model.factory;

import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.model.Automobile;
import ru.ssau.patternpractice.model.Transport;
import ru.ssau.patternpractice.model.decorators.SynchronizedTransport;

public class AutoFactory implements TransportFactory {
    @Override
    public Transport createInstance(String brand, int modelAmount) {
        return new Automobile(brand, modelAmount);
    }

    @Override
    public Transport createSynchronizedInstance(String brand, int modelAmount) throws DuplicateModelNameException {
        return new SynchronizedTransport(this.createInstance(brand, modelAmount));
    }
}
