package ru.ssau.patternpractice;

import ru.ssau.patternpractice.config.AppConfig;
import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.exception.NoSuchModelNameException;
import ru.ssau.patternpractice.model.Transport;
import ru.ssau.patternpractice.utility.TransportAnalytic;

public class Main {
    public static void main(String[] args) throws DuplicateModelNameException, NoSuchModelNameException {
        System.out.println("Проверка загрузки AppConfig");
        AppConfig config = AppConfig.getInstance();
        String fuel = config.getProperty("fuel");
        String model = config.getProperty("model");
        System.out.printf("Fuel: %s, Model: %s\n", fuel, model);

        Transport transport = TransportAnalytic.createInstance("Армяне за нардами", 10);

        doTestsOnTransport(transport);
    }

    public static void doTestsOnTransport(Transport transport) throws DuplicateModelNameException, NoSuchModelNameException {
        System.out.println("Осмотрим авто");
        //Проверка класса
        System.out.println(transport.getClass());
        //Проверка получение бренда
        System.out.println(transport.getBrand());
        //Проверка выставления бренда
        transport.setBrand("Бумер");
        //Проверка получения всех моделей
        System.out.println(transport.getAllModelsNames());
        //Проверка изменения названия модели
        transport.setModelName("Армяне за нардами1", "Бумер1");
        System.out.println(transport.getAllModelsNames());
        try {
            transport.setModelName("Армяне за нардами1", "Бумер2");
        } catch (NoSuchModelNameException e) {
            System.out.println("Такой марки нету");
        }
        try {
            transport.setModelName("Армяне за нардами2", "Бумер1");
        } catch (DuplicateModelNameException e) {
            System.out.println("Такая марка уже существует");
        }
        //Проверка получения стоимости
        System.out.println(transport.getModelCost("Бумер1"));
        //Проверка изменения стоимости модели
        transport.setModelCost("Бумер1", 300.0);
        System.out.println(transport.getModelCost("Бумер1"));
        //Проверка удаления модели
        transport.deleteModel("Бумер1", 300.0);
        System.out.println(transport.getAllModelsNames());
        //Проверка добавления модели
        transport.addNewModel("Бумер1", 300.0);
        System.out.println(transport.getAllModelsNames());
        System.out.println(transport.getModelsAmount());
        //Проверка отображения всех моделей
        TransportAnalytic.displayAllModels(transport);
        //Проверка получения средней стоимости
        TransportAnalytic.modelsMean(transport);
    }
}
