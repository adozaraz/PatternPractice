package ru.ssau.patternpractice.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
@Setter
public class Motorcycle implements Transport {
    private String brand;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Model modelHead;
    private int size = 0;

    public Motorcycle(String brand, String[] models, Double[] costs) {
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

    @EqualsAndHashCode
    private static class Model implements Iterable<Model> {
        String name;
        Double cost = Double.NaN;
        Model next = null;
        Model prev = null;

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
    }

    private static class ModelIterator implements Iterator<Model> {
        private final Model head;
        private Model iterator;

        public ModelIterator(Model head) {
            this.head = head;
            this.iterator = head;
        }

        @Override
        public boolean hasNext() {
            return !iterator.next.equals(head);
        }

        @Override
        public Model next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Model result = iterator;
            iterator = iterator.next;
            return result;
        }

        @Override
        public void remove() {
            if (iterator.equals(head)) {
                iterator.next = null;
                iterator.prev = null;
                iterator = null;
            } else {
                iterator.prev.next = iterator.next;
                iterator.next.prev = iterator.prev;
                iterator.next = null;
                iterator.prev = null;
            }
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
        for (Model model : modelHead) {
            if (model.name.equals(name) && model.cost.equals(cost)) {
                throw new DuplicateModelNameException();
            }
        }
        if (modelHead == null) {
            modelHead = new Model(name, cost, null, null);
            modelHead.prev = modelHead;
            modelHead.next = modelHead;
        } else {
            Model model = new Model(name, cost, modelHead, modelHead.prev);
            modelHead.prev.next = model;
            modelHead.prev = model;
        }
        ++size;
    }

    @Override
    public void deleteModel(String name, Double cost) throws NoSuchModelNameException {
        Iterator<Model> it = modelHead.iterator();
        while (it.hasNext()) {
            Model model = it.next();
            if (model.name.equals(name) && model.cost.equals(cost)) {
                it.remove();
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
