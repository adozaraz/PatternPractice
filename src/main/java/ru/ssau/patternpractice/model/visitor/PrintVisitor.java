package ru.ssau.patternpractice.model.visitor;

import ru.ssau.patternpractice.model.Automobile;
import ru.ssau.patternpractice.model.Motorcycle;

import java.util.List;

public class PrintVisitor implements Visitor {
    @Override
    public void visit(Automobile automobile) {
        List<String> models = automobile.getAllModelsNames();
        List<Double> costs = automobile.getAllModelsCost();
        for (int i = 0; i < automobile.getModelsAmount(); ++i) {
            System.out.print("Model: " + models.get(i) + " Cost: " + costs.get(i) + " ");
        }
    }

    @Override
    public void visit(Motorcycle motorcycle) {
        List<String> models = motorcycle.getAllModelsNames();
        List<Double> costs = motorcycle.getAllModelsCost();
        for (int i = 0; i < motorcycle.getModelsAmount(); ++i) {
            System.out.println("Model: " + models.get(i) + " Cost: " + costs.get(i));
        }
    }
}
