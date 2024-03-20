package ru.ssau.patternpractice.model;


import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;
import ru.ssau.patternpractice.model.command.PrintCommand;
import ru.ssau.patternpractice.model.visitor.Visitor;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class Automobile implements Transport, Cloneable, Serializable {
    private static class Model implements Cloneable, Serializable {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return Objects.equals(name, model.name) && Objects.equals(cost, model.cost);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, cost);
        }

        public String toString() {
            return name + ": " + cost;
        }
    }

    private static class Memento {
        byte[] savedState;

        public void setAuto(Automobile automobile) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ObjectOutputStream objectWriter = new ObjectOutputStream(outputStream);
                objectWriter.writeObject(automobile);
                objectWriter.close();
                savedState = outputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public Automobile getAuto() {
            try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(savedState))) {
                return (Automobile) stream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class ModelIterator implements Iterator<Model> {
        private Model[] models;

        private int currentModel;

        public ModelIterator(Model[] models) {
            this.models = models;
        }

        @Override
        public boolean hasNext() {
            return currentModel < models.length;
        }

        @Override
        public Model next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Model result = models[currentModel];
            ++currentModel;
            return result;
        }
    }

    private String brand;
    private Model[] models;
    private int size;

    private int arraySizeIncrease;

    private transient PrintCommand printCommand;

    private transient Memento savedState;

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Automobile(String brand, Integer size) {
        this(brand, size, 10000.0, 20000.0, 10);
    }

    public Automobile(String brand, Integer size, int arraySizeIncrease) {
        this(brand, size, 10000.0, 20000.0, arraySizeIncrease);
    }

    public Automobile(String brand, int size, double minCost, double maxCost, int arraySizeIncrease) {
        Random r = new Random();
        this.brand = brand;
        this.size = size;
        this.arraySizeIncrease = arraySizeIncrease;
        this.models = new Model[size+arraySizeIncrease];
        for (int i = 0; i < size; ++i) {
            models[i] = new Model(brand + i, minCost + (maxCost - minCost) * r.nextDouble());
        }
        this.savedState = new Memento();
    }

    @Override
    public Automobile clone() {
        try {
            Automobile copy = (Automobile) super.clone();
            copy.models = models.clone();
            for (int i = 0; i < size; ++i) {
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

    public List<String> getAllModelsNames() {
        return Arrays.stream(models).filter(Objects::nonNull).map(Model::getName).toList();
    }

    public List<Double> getAllModelsCost() {
        return Arrays.stream(models).filter(Objects::nonNull).map(Model::getCost).toList();
    }

    public Double getModelCost(String modelName) throws NoSuchModelNameException {
        Optional<Model> result = Arrays.stream(models).filter(Objects::nonNull).filter(model -> Objects.equals(model.getName(), modelName)).findFirst();
        if (result.isPresent()) {
            return result.get().getCost();
        } else {
            throw new NoSuchModelNameException();
        }
    }

    public void setModelCost(String modelName, Double cost) throws NoSuchModelNameException {
        for (Model model : models) {
            if (model != null && model.getName().equals(modelName)) {
                model.setCost(cost);
                return;
            }
        }
        throw new NoSuchModelNameException();
    }

    public void addNewModel(String name, Double cost) throws DuplicateModelNameException {
        Model newModel = new Model(name, cost);
        boolean isPresent = Arrays.stream(models).filter(Objects::nonNull).anyMatch(model -> model.equals(newModel));
        if (isPresent) {
            throw new DuplicateModelNameException();
        }
        if (size >= models.length) {
            resizeArray();
        }
        models[size++] = newModel;
    }

    public void deleteModel(String name, Double cost) throws NoSuchModelNameException {
        Model targetModel = new Model(name, cost);
        int index = IntStream.range(0, models.length).filter(i -> models[i].equals(targetModel)).findFirst().orElse(-1);
        if (index == -1) {
            throw new NoSuchModelNameException();
        }
        if (index == size - 1) {
            models[index] = null;
        } else if (index == 0) {
            System.arraycopy(models, 1, models, 0, size-1);
        } else {
            Model[] modelsCopy = Arrays.copyOf(models, models.length);
            models = new Model[models.length - 1];
            System.arraycopy(modelsCopy, 0, models, 0, index);
            System.arraycopy(modelsCopy, index + 1, models, index, modelsCopy.length - index - 1);
        }
        --size;
    }

    public int getModelsAmount() {
        return size;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void clearAllModels() {
        models = new Model[size+arraySizeIncrease];
    }

    private void resizeArray() {
        models = Arrays.copyOf(models, size+arraySizeIncrease);
    }

    @Override
    public Iterator<Model> iterator() {
        return new ModelIterator(models);
    }

    public void print() throws IOException {
        this.printCommand.print(this);
    }

    public void setPrintCommand(PrintCommand printCommand) {
        this.printCommand = printCommand;
    }

    public void createMemento() {
        this.savedState.setAuto(this);
    }

    public void setMemento() {
        Automobile savedAuto = this.savedState.getAuto();
        this.brand = savedAuto.brand;
        this.size = savedAuto.size;
        this.models = savedAuto.models;
        this.arraySizeIncrease = savedAuto.arraySizeIncrease;
    }
}
