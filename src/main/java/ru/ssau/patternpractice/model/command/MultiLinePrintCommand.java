package ru.ssau.patternpractice.model.command;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class MultiLinePrintCommand implements PrintCommand {
    @Override
    public void print(List<String> models, OutputStream outputStream) throws IOException {
        for (String model : models) {
            outputStream.write((model + "\n").getBytes());
        }
    }
}
