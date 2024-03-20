package ru.ssau.patternpractice.model.decorators;

import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;
import ru.ssau.patternpractice.model.Transport;
import ru.ssau.patternpractice.model.visitor.Visitor;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.UUID;
import java.util.function.Consumer;

public class SynchronizedTransport extends BaseTransportDecorator {
    public SynchronizedTransport(Transport wrapped) {
        super(wrapped);
    }

    @Override
    public String getBrand() {
        synchronized (wrapped) {
            return wrapped.getBrand();
        }
    }

    @Override
    public void setBrand(String brand) {
        synchronized (wrapped) {
            wrapped.setBrand(brand);
        }
    }

    @Override
    public void setModelName(String oldModel, String newModel) throws NoSuchModelNameException, DuplicateModelNameException {
        synchronized (wrapped) {
            wrapped.setModelName(oldModel, newModel);
        }
    }

    @Override
    public List<String> getAllModelsNames() {
        synchronized (wrapped) {
            return wrapped.getAllModelsNames();
        }
    }

    @Override
    public Double getModelCost(String modelName) throws NoSuchModelNameException {
        synchronized (wrapped) {
            return wrapped.getModelCost(modelName);
        }
    }

    @Override
    public void setModelCost(String modelName, Double modelCost) throws NoSuchModelNameException {
        synchronized (wrapped) {
            wrapped.setModelCost(modelName, modelCost);
        }
    }

    @Override
    public List<Double> getAllModelsCost() {
        synchronized (wrapped) {
            return wrapped.getAllModelsCost();
        }
    }

    @Override
    public void addNewModel(String name, Double cost) throws DuplicateModelNameException {
        synchronized (wrapped) {
            wrapped.addNewModel(name, cost);
        }
    }

    @Override
    public void deleteModel(String name, Double cost) throws NoSuchModelNameException {
        synchronized (wrapped) {
            wrapped.deleteModel(name, cost);
        }
    }

    @Override
    public int getModelsAmount() {
        synchronized (wrapped) {
            return wrapped.getModelsAmount();
        }
    }

    @Override
    public void accept(Visitor visitor) {
        synchronized (wrapped) {
            wrapped.accept(visitor);
        }
    }

    @Override
    public void clearAllModels() {

    }

    @Override
    public void setId(UUID id) {

    }

    @Override
    public UUID getId() {
        return null;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer action) {
        super.forEach(action);
    }

    @Override
    public Spliterator spliterator() {
        return super.spliterator();
    }
}
