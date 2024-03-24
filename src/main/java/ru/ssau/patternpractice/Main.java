package ru.ssau.patternpractice;

import ru.ssau.patternpractice.config.AppConfig;
import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;
import ru.ssau.patternpractice.model.Automobile;
import ru.ssau.patternpractice.model.Motorcycle;
import ru.ssau.patternpractice.model.Transport;
import ru.ssau.patternpractice.model.chain_of_responsibility.MultiLineStringChainWriter;
import ru.ssau.patternpractice.model.chain_of_responsibility.OneStringChainWriter;
import ru.ssau.patternpractice.model.chain_of_responsibility.TransportChain;
import ru.ssau.patternpractice.model.command.MultiLinePrintCommand;
import ru.ssau.patternpractice.model.command.PrintCommand;
import ru.ssau.patternpractice.model.dao.*;
import ru.ssau.patternpractice.model.strategy.CountStrategy;
import ru.ssau.patternpractice.model.strategy.FrequencyCountStrategy;
import ru.ssau.patternpractice.model.visitor.PrintVisitor;
import ru.ssau.patternpractice.model.visitor.Visitor;
import ru.ssau.patternpractice.utility.TransportAnalytic;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws DuplicateModelNameException, NoSuchModelNameException, IOException, SQLException {
/*        System.out.println("Проверка загрузки AppConfig");
       AppConfig config = AppConfig.getInstance();
       String firstModel = config.getProperty("firstModel");
       String secondModel = config.getProperty("secondModel");
       System.out.printf("First model: %s, Second model: %s%n", firstModel, secondModel);

       Transport transport = TransportAnalytic.createInstance(firstModel, 10);

       doTestsOnTransport(transport, firstModel);
       testAutomobileClone((Automobile) transport, firstModel);

       System.out.println("Проверим мотоциклы");
       TransportAnalytic.setTransportFactory(new MotoFactory());

       transport = TransportAnalytic.createInstance(secondModel, 4);

        doTestsOnTransport(transport, secondModel);
        testMotorcycleClone((Motorcycle) transport, secondModel);*/


        AppConfig config = AppConfig.getInstance();
        String firstModel = config.getProperty("firstModel");
        Automobile transport = (Automobile) TransportAnalytic.createInstance(firstModel, 10);
        System.out.println("------------CHAIN OF RESPONSIBILITIES------------");
        TransportChain chain = new OneStringChainWriter(new MultiLineStringChainWriter());
        chain.handleTransportModels(transport);
        System.out.println("------------COMMAND------------");
        PrintCommand command = new MultiLinePrintCommand();
        command.print(transport);
        System.out.println("------------Iterator-----------");

        System.out.println("------------MEMENTO------------");
        transport.createMemento();
        transport.addNewModel("aaa", 123.0);
        System.out.println("ДО:");
        Iterator ait = transport.iterator();
        while (ait.hasNext()) {
            System.out.println(ait.next());
        }
        transport.setMemento();
        System.out.println("После:");
        ait = transport.iterator();
        while (ait.hasNext()) {
            System.out.println(ait.next());
        }
        System.out.println(transport.getModelsAmount());
        System.out.println("------------STRATEGY------------");
        generateFile(args[0], 30);
        List<Integer> integerList = getSerializedFile(args[0]);
        CountStrategy strategy = new FrequencyCountStrategy();
        Map<Integer, Integer> counter = strategy.count(integerList);
        counter.forEach((integer, integer2) -> System.out.println(integer + ": " + integer2));
        System.out.println("------------VISITOR------------");
        Visitor visitor = new PrintVisitor();
        visitor.visit(transport);
        System.out.println("---------DAO----------");
        AutomobileDAO auto = AutomobileDAOPostgresqlImpl.getInstance("dbConfig.properties", "mysql.properties");
        transport.setId(UUID.randomUUID());
        auto.create(transport);
        Optional<Transport> potentialAuto = auto.read(transport.getId());
        potentialAuto.ifPresent(value -> System.out.println(value.getBrand()));
        TransportModelDAO models = TransportModelDAOMySQLImpl.getInstance();
        List<ModelDAO> potent = models.getModelsForConcreteTransport(transport.getId());
        for (ModelDAO mod : potent) {
            System.out.println("Name: " + mod.getName() + " Price: " + mod.getCost());
        }
        auto.closeConnection();
    }

    public static void doTestsOnTransport(Transport transport, String transportName) throws DuplicateModelNameException, NoSuchModelNameException {
        System.out.println("Осмотрим авто");
        //Проверка класса
        System.out.println(transport.getClass());
        //Проверка получение бренда
        System.out.println("Узнаем бренд");
        System.out.println(transport.getBrand());
        //Проверка выставления бренда
        System.out.println("изменим бренд");
        transport.setBrand("Бумер");
        //Проверка получения всех моделей
        System.out.println("Узнаем все модели бренда");
        System.out.println(transport.getAllModelsNames());
        //Проверка изменения названия модели
        System.out.println("Поменяем модели бренда");
        transport.setModelName(transportName + 1, "Бумер1");
        System.out.println(transport.getAllModelsNames());
        try {
            transport.setModelName(transportName + 1, "Бумер2");
        } catch (NoSuchModelNameException e) {
            System.out.println("Такой марки нету");
        }
        try {
            transport.setModelName(transportName + 2, "Бумер1");
        } catch (DuplicateModelNameException e) {
            System.out.println("Такая марка уже существует");
        }
        //Проверка получения стоимости
        System.out.println("Узнаем стоимость модели");
        System.out.println(transport.getModelCost("Бумер1"));
        //Проверка изменения стоимости модели
        System.out.println("изменим стоимость модели");
        transport.setModelCost("Бумер1", 300.0);
        System.out.println(transport.getModelCost("Бумер1"));
        //Проверка удаления модели
        System.out.println("Удалим модель");
        transport.deleteModel("Бумер1", 300.0);
        System.out.println(transport.getAllModelsNames());
        //Проверка добавления модели
        System.out.println("Добавим модель");
        transport.addNewModel("Бумер1", 300.0);
        System.out.println(transport.getAllModelsNames());
        System.out.println(transport.getModelsAmount());
        //Проверка отображения всех моделей
        System.out.println("Отобразим все модели");
        TransportAnalytic.displayAllModels(transport);
        //Проверка получения средней стоимости
        System.out.println("Узнаем среднюю стоимость моделей");
        System.out.println(TransportAnalytic.modelsMean(transport));
    }

    public static void testMotorcycleClone(Motorcycle orig, String modelName) throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle clone = orig.clone();
        clone.addNewModel("Бумер300bucks", 300.0);
        System.out.println("Неповторимый оригинал");
        orig.getAllModelsNames().forEach(System.out::println);
        orig.setModelName(modelName + 3, "Армяне в нарды играют228");
        System.out.println("Неповторимый оригинал 2");
        orig.getAllModelsNames().forEach(System.out::println);
        System.out.println("Дешёвая копия");
        clone.getAllModelsNames().forEach(System.out::println);
    }

    public static void testAutomobileClone(Automobile orig, String modelName) throws DuplicateModelNameException, NoSuchModelNameException {
        Automobile clone = orig.clone();
        clone.addNewModel("Бумер300bucks", 300.0);
        System.out.println("Неповторимый оригинал");
        orig.getAllModelsNames().forEach(System.out::println);
        orig.setModelName(modelName + 3, "Армяне в нарды играют228");
        System.out.println("Неповторимый оригинал 2");
        orig.getAllModelsNames().forEach(System.out::println);
        System.out.println("Дешёвая копия");
        clone.getAllModelsNames().forEach(System.out::println);
    }

    public static void generateFile(String fileName, int size) {
        List<Integer> arr = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < size; ++i) {
            arr.add(r.nextInt(10) + 1);
        }
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            ObjectOutputStream objectWriter = new ObjectOutputStream(outputStream);
            objectWriter.writeObject(arr);
            objectWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> getSerializedFile(String fileName) {
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            List<Integer> result = (List<Integer>) in.readObject();
            in.close();
            return result;
        } catch (FileNotFoundException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
