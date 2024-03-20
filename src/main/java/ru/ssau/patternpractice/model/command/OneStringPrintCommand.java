package ru.ssau.patternpractice.model.command;

import ru.ssau.patternpractice.model.Transport;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class OneStringPrintCommand implements PrintCommand {
    @Override
    public void print(Transport transport) throws IOException {
        FileWriter fileWriter = new FileWriter("Transport models.txt");
        try (PrintWriter writer = new PrintWriter(fileWriter)) {
            for (String model : transport.getAllModelsNames()) {
                writer.print(model + " ");
            }
        }
    }
}
