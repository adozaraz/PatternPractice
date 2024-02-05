package ru.ssau.patternpractice.model;

import java.util.List;

public interface Transport {
    List<String> getAllModelsNames();
    List<Double> getAllModelsCost();
    Double getModelCost(String modelName);
    void setModelCost(String modelName, Double modelCost);
    void addNewModel(String name, Double cost);
    void deleteModel(String name, Double cost);
    int getModelsAmount();
}
