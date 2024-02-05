package ru.ssau.patternpractice.model;


import lombok.*;
import ru.ssau.patternpractice.exception.ModelAlreadyExistsException;
import ru.ssau.patternpractice.exception.ModelNotFoundException;

import java.util.*;
import java.util.stream.IntStream;

@Getter
@Setter
public class Automobile implements Transport {
    private String brand;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Model[] models;
    @Getter
    @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class Model {
        private String name;
        private Double cost;
    }

    public List<String> getAllModelsNames() {
        return Arrays.stream(models).map(Model::getName).toList();
    }

    public List<Double> getAllModelsCost() {
        return Arrays.stream(models).map(Model::getCost).toList();
    }

    public Double getModelCost(String modelName) {
        Optional<Model> result = Arrays.stream(models).filter(model -> Objects.equals(model.getName(), modelName)).findFirst();
        if (result.isPresent()) {
            return result.get().getCost();
        } else {
            throw new ModelNotFoundException();
        }
    }

    public void setModelCost(String modelName, Double cost) {
        for (Model model : models) {
            if (model.getName().equals(modelName)) {
                model.setCost(cost);
                return;
            }
        }
        throw new ModelNotFoundException();
    }

    public void addNewModel(String name, Double cost) {
        Model newModel = new Model(name, cost);
        boolean isPresent = Arrays.stream(models).anyMatch(model -> model.equals(newModel));
        if (isPresent) {
            throw new ModelAlreadyExistsException();
        }
        models = Arrays.copyOf(models, models.length+1);
        models[models.length-1] = newModel;
    }

    public void deleteModel(String name, Double cost) {
        Model targetModel = new Model(name, cost);
        int index = IntStream.range(0, models.length).filter(i -> models[i].equals(targetModel)).findFirst().orElse(-1);
        if (index == -1) {
            throw new ModelNotFoundException();
        }
        Model[] modelsCopy = Arrays.copyOf(models, models.length);
        models = new Model[models.length-1];
        System.arraycopy(modelsCopy, 0, models, 0, index);
        if (index != modelsCopy.length-1) {
            System.arraycopy(modelsCopy, index+1, models, index+1, modelsCopy.length-index-1);
        }
    }

    public int getModelsAmount() {
        return models.length;
    }


}
