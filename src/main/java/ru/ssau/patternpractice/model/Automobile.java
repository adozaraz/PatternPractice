package ru.ssau.patternpractice.model;


import lombok.*;
import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;

import java.util.*;
import java.util.stream.IntStream;

@Getter
@Setter
public class Automobile implements Transport, Cloneable {
    private String brand;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Model[] models;

    @Override
    public Automobile clone() {
        try {
            Automobile copy = (Automobile) super.clone();
            copy.models = models.clone();
            for (int i = 0; i < models.length; ++i) {
                copy.models[i] = copy.models[i].clone();
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class Model implements Cloneable {
        private String name;
        private Double cost;

        @Override
        public Model clone() {
            try {
                return (Model) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    public Automobile(String brand, String[] models, Double[] costs) {
        if (models.length != costs.length) {
            throw new IllegalArgumentException("Количество моделей должно совпадать с количеством стоимостей");
        }
        this.brand = brand;
        for (int i = 0; i < models.length; ++i) {
            try {
                this.addNewModel(models[i], costs[i]);
            } catch (DuplicateModelNameException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<String> getAllModelsNames() {
        return Arrays.stream(models).map(Model::getName).toList();
    }

    public List<Double> getAllModelsCost() {
        return Arrays.stream(models).map(Model::getCost).toList();
    }

    public Double getModelCost(String modelName) throws NoSuchModelNameException {
        Optional<Model> result = Arrays.stream(models).filter(model -> Objects.equals(model.getName(), modelName)).findFirst();
        if (result.isPresent()) {
            return result.get().getCost();
        } else {
            throw new NoSuchModelNameException();
        }
    }

    public void setModelCost(String modelName, Double cost) throws NoSuchModelNameException {
        for (Model model : models) {
            if (model.getName().equals(modelName)) {
                model.setCost(cost);
                return;
            }
        }
        throw new NoSuchModelNameException();
    }

    public void addNewModel(String name, Double cost) throws DuplicateModelNameException {
        Model newModel = new Model(name, cost);
        boolean isPresent = Arrays.stream(models).anyMatch(model -> model.equals(newModel));
        if (isPresent) {
            throw new DuplicateModelNameException();
        }
        models = Arrays.copyOf(models, models.length+1);
        models[models.length-1] = newModel;
    }

    public void deleteModel(String name, Double cost) throws NoSuchModelNameException {
        Model targetModel = new Model(name, cost);
        int index = IntStream.range(0, models.length).filter(i -> models[i].equals(targetModel)).findFirst().orElse(-1);
        if (index == -1) {
            throw new NoSuchModelNameException();
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
