package ru.ssau.patternpractice.utility;

import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.model.factory.AutoFactory;
import ru.ssau.patternpractice.model.Transport;
import ru.ssau.patternpractice.model.factory.TransportFactory;

import java.util.List;

public class TransportAnalytic {
    private static TransportFactory factory = new AutoFactory();

    private TransportAnalytic() {}

    public static Double modelsMean(Transport transport) {
        List<Double> modelsCost = transport.getAllModelsCost();
        return modelsCost.stream().mapToDouble(d -> d).average().orElse(Double.NaN);
    }

    public static void displayAllModels(Transport transport) {
        List<String> modelNames = transport.getAllModelsNames();
        List<Double> modelCosts = transport.getAllModelsCost();
        for (int i = 0; i < transport.getModelsAmount(); ++i) {
            System.out.printf("Model: %s, Cost: %f\n", modelNames.get(i), modelCosts.get(i));
        }
    }

    public static void setTransportFactory(TransportFactory transportFactory) {
        factory = transportFactory;
    }

    public static Transport createInstance(String name, int size) throws DuplicateModelNameException {
        return factory.createInstance(name, size);
    }
}
