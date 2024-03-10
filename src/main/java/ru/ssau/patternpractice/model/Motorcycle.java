package ru.ssau.patternpractice.model;

import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;

import java.util.*;

public class Motorcycle implements Transport, Cloneable {
    private String brand;
    private Model modelHead;
    private int size = 0;

    public Motorcycle(String brand, int size) throws DuplicateModelNameException {
        this(brand, size, 10000.0, 20000.0);
    }

    public Motorcycle(String brand, int size, double minCost, double maxCost) throws DuplicateModelNameException {
        Random r = new Random();
        this.size = 0;
        this.brand = brand;
        for (int i = 0; i < size; ++i) {
            this.addNewModel(brand + i, minCost + (maxCost - minCost) * r.nextDouble());
        }
    }

    @Override
    public Motorcycle clone() {
        try {
            Motorcycle clone = (Motorcycle) super.clone();
            clone.modelHead = new Model(modelHead.name, modelHead.cost);
            clone.modelHead.next = clone.modelHead;
            clone.modelHead.prev = clone.modelHead;
            Iterator<Model> origIter = modelHead.iterator();
            if (origIter.hasNext()) origIter.next();
            while (origIter.hasNext()) {
                Model toAdd = origIter.next();
                try {
                    clone.addNewModel(toAdd.name, toAdd.cost);
                } catch (DuplicateModelNameException e) {
                    System.out.println(e.getMessage());
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public String getBrand() {
        return this.brand;
    }

    public int getSize() {
        return this.size;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public void setModelName(String oldModel, String newModel) throws NoSuchModelNameException, DuplicateModelNameException {
        Model target = null;
        for (Model model : modelHead) {
            if (model.name.equals(newModel)) {
                throw new DuplicateModelNameException();
            }
            if (model.name.equals(oldModel) && target == null) {
                target = model;
            }
        }
        if (target != null) {
            target.name = newModel;
            return;
        }
        throw new NoSuchModelNameException();
    }

    public void setSize(int size) {
        this.size = size;
    }

    private static class Model implements Iterable<Model> {
        String name;
        Double cost;
        Model next;
        Model prev;

        public Model(String name, Double cost) {
            this(name, cost, null, null);
        }

        public Model(String name, Double cost, Model next, Model prev) {
            this.name = name;
            this.cost = cost;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public Iterator<Model> iterator() {
            return new ModelIterator(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return Objects.equals(name, model.name) && Objects.equals(cost, model.cost) && next == model.next && prev == model.prev;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, cost);
        }

        public String toString() {
            return name + ": " + cost;
        }
    }

    private static class ModelIterator implements Iterator<Model> {
        private final Model head;
        private Model iterator;
        private boolean fullCycle = true;

        public ModelIterator(Model head) {
            this.head = head;
            this.iterator = head;
        }

        @Override
        public boolean hasNext() {
            return fullCycle;
        }

        @Override
        public Model next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Model result = iterator;
            iterator = iterator.next;
            if (iterator.equals(head)) fullCycle = false;
            return result;
        }
    }

    @Override
    public List<String> getAllModelsNames() {
        List<String> result = new ArrayList<>();
        for (Model model : modelHead) {
            result.add(model.name);
        }
        return result;
    }

    @Override
    public List<Double> getAllModelsCost() {
        List<Double> result = new ArrayList<>();
        for (Model model : modelHead) {
            result.add(model.cost);
        }
        return result;
    }

    @Override
    public Double getModelCost(String modelName) throws NoSuchModelNameException {
        for (Model model : modelHead) {
            if (model.name.equals(modelName)) {
                return model.cost;
            }
        }
        throw new NoSuchModelNameException();
    }

    @Override
    public void setModelCost(String modelName, Double modelCost) throws NoSuchModelNameException {
        for (Model model : modelHead) {
            if (model.name.equals(modelName)) {
                model.cost = modelCost;
                return;
            }
        }
        throw new NoSuchModelNameException();
    }

    @Override
    public void addNewModel(String name, Double cost) throws DuplicateModelNameException {
        if (modelHead == null) {
            modelHead = new Model(name, cost, null, null);
            modelHead.prev = modelHead;
            modelHead.next = modelHead;
        } else {
            for (Model model : modelHead) {
                if (model.name.equals(name) && model.cost.equals(cost)) {
                    throw new DuplicateModelNameException();
                }
            }
            Model model = new Model(name, cost, modelHead, modelHead.prev);
            modelHead.prev.next = model;
            modelHead.prev = model;
        }
        ++size;
    }

    @Override
    public void deleteModel(String name, Double cost) throws NoSuchModelNameException {
        for (Model model : modelHead) {
            if (model.name.equals(name) && model.cost.equals(cost)) {
                model.next.prev = model.prev;
                model.prev.next = model.next;
                model.next = null;
                model.prev = null;
                --size;
                return;
            }
        }
        throw new NoSuchModelNameException();
    }

    @Override
    public int getModelsAmount() {
        return size;
    }


}
