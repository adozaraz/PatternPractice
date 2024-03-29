package ru.ssau.patternpractice.model;

import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;
import ru.ssau.patternpractice.model.visitor.Visitor;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public interface Transport extends Iterable {
    String getBrand();
    void setBrand(String brand);
    void setModelName(String oldModel, String newModel) throws NoSuchModelNameException, DuplicateModelNameException;
    List<String> getAllModelsNames();
    Double getModelCost(String modelName) throws NoSuchModelNameException;
    void setModelCost(String modelName, Double modelCost) throws NoSuchModelNameException;
    List<Double> getAllModelsCost();
    void addNewModel(String name, Double cost) throws DuplicateModelNameException;
    void deleteModel(String name, Double cost) throws NoSuchModelNameException;
    int getModelsAmount();
    void accept(Visitor visitor);

    void clearAllModels();
    void setId(UUID id);
    UUID getId();
}
