package ru.ssau.patternpractice.model.command;

import ru.ssau.patternpractice.model.Transport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface PrintCommand {
    void print(Transport transport) throws IOException;
}
