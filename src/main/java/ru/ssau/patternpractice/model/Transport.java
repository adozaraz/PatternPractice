package ru.ssau.patternpractice.model;

import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;

import java.util.List;

public interface Transport {
    List<String> getAllModelsNames();
    List<Double> getAllModelsCost();
    Double getModelCost(String modelName) throws NoSuchModelNameException;
    void setModelCost(String modelName, Double modelCost) throws NoSuchModelNameException;
    void addNewModel(String name, Double cost) throws DuplicateModelNameException;
    void deleteModel(String name, Double cost) throws NoSuchModelNameException;
    int getModelsAmount();
}
