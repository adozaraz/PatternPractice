package ru.ssau.patternpractice.model.adapter;

import java.io.*;

public class ArrayAdapter extends ByteArrayOutputStream {
    public synchronized void write(Iterable<String> strings) {
        for (String term : strings) {
            this.writeBytes(term.getBytes());
            this.writeBytes("\n".getBytes());
        }
    }
}
