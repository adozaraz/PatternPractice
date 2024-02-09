package ru.ssau.patternpractice.model;


import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;

import java.util.*;
import java.util.stream.IntStream;

public class Automobile implements Transport, Cloneable {
    private String brand;
    private Model[] models;
    private int size;

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

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public void setModelName(String oldModel, String newModel) throws NoSuchModelNameException, DuplicateModelNameException {
        Model target = null;
        for (int i = 0; i < size; ++i) {
            if (models[i].name.equals(newModel)) {
                throw new DuplicateModelNameException();
            }
            if (models[i].name.equals(oldModel) && target == null) {
                target = models[i];
            }
        }
        if (target != null) {
            target.name = newModel;
            return;
        }
        throw new NoSuchModelNameException();
    }

    private static class Model implements Cloneable {
        private String name;
        private Double cost;

        public Model(String name, Double cost) {
            this.name = name;
            this.cost = cost;
        }

        @Override
        public Model clone() {
            try {
                return (Model) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        public String getName() {
            return this.name;
        }

        public Double getCost() {
            return this.cost;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCost(Double cost) {
            this.cost = cost;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Model)) return false;
            final Model other = (Model) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$name = this.getName();
            final Object other$name = other.getName();
            if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
            final Object this$cost = this.getCost();
            final Object other$cost = other.getCost();
            if (this$cost == null ? other$cost != null : !this$cost.equals(other$cost)) return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof Model;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $name = this.getName();
            result = result * PRIME + ($name == null ? 43 : $name.hashCode());
            final Object $cost = this.getCost();
            result = result * PRIME + ($cost == null ? 43 : $cost.hashCode());
            return result;
        }
    }

    public Automobile(String brand, Integer size) {
        this(brand, size, 10000.0, 20000.0);
    }

    public Automobile(String brand, Integer size, double minCost, double maxCost) {
        Random r = new Random();
        this.brand = brand;
        this.size = size;
        this.models = new Model[size];
        for (int i = 0; i < size; ++i) {
            models[i] = new Model(brand + i, minCost + (maxCost - minCost) * r.nextDouble());
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
        models = Arrays.copyOf(models, models.length + 1);
        models[models.length - 1] = newModel;
    }

    public void deleteModel(String name, Double cost) throws NoSuchModelNameException {
        Model targetModel = new Model(name, cost);
        int index = IntStream.range(0, models.length).filter(i -> models[i].equals(targetModel)).findFirst().orElse(-1);
        if (index == -1) {
            throw new NoSuchModelNameException();
        }
        Model[] modelsCopy = Arrays.copyOf(models, models.length);
        models = new Model[models.length - 1];
        System.arraycopy(modelsCopy, 0, models, 0, index);
        if (index != modelsCopy.length - 1) {
            System.arraycopy(modelsCopy, index + 1, models, index, modelsCopy.length - index - 1);
        }
    }

    public int getModelsAmount() {
        return models.length;
    }
}
