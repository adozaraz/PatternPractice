package ru.ssau.patternpractice.utility;

import lombok.experimental.UtilityClass;
import ru.ssau.patternpractice.model.Transport;

import java.util.List;

@UtilityClass
public class TransportAnalytic {
    public static Double modelsMean(Transport transport) {
        List<Double> modelsCost = transport.getAllModelsCost();
        return modelsCost.stream().mapToDouble(d -> d).average().orElse(Double.NaN);
    }

    public static void displayAllModels(Transport transport) {
        List<String> modelNames = transport.getAllModelsNames();
        List<Double> modelCosts = transport.getAllModelsCost();
        for (int i = 0; i < transport.getModelsAmount(); ++i) {
            System.out.printf("Model: %s, Cost: %f", modelNames.get(i), modelCosts.get(i));
        }
    }
}
