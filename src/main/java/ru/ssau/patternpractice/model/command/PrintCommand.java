package ru.ssau.patternpractice.model.command;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface PrintCommand {
    void print(List<String> models, OutputStream outputStream) throws IOException;
}
