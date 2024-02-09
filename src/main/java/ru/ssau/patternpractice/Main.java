package ru.ssau.patternpractice;

import ru.ssau.patternpractice.config.AppConfig;
import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;
import ru.ssau.patternpractice.model.Automobile;
import ru.ssau.patternpractice.model.Motorcycle;
import ru.ssau.patternpractice.model.Transport;
import ru.ssau.patternpractice.model.factory.MotoFactory;
import ru.ssau.patternpractice.utility.TransportAnalytic;

public class Main {
    public static void main(String[] args) throws DuplicateModelNameException, NoSuchModelNameException {
        System.out.println("Проверка загрузки AppConfig");
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
        testMotorcycleClone((Motorcycle) transport, secondModel);
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
}
