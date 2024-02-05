package ru.ssau.patternpractice.model;

import java.util.List;

public class Motorcycle implements Transport {

    @Override
    public List<String> getAllModelsNames() {
        return null;
    }

    @Override
    public List<Double> getAllModelsCost() {
        return null;
    }

    @Override
    public Double getModelCost(String modelName) {
        return null;
    }

    @Override
    public void setModelCost(String modelName, Double modelCost) {

    }

    @Override
    public void addNewModel(String name, Double cost) {

    }

    @Override
    public void deleteModel(String name, Double cost) {

    }

    @Override
    public int getModelsAmount() {
        return 0;
    }
}
