package ru.ssau.patternpractice.model.chain_of_responsibility;

import ru.ssau.patternpractice.model.Transport;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MultiLineStringChainWriter implements TransportChain {
    private TransportChain nextChain;

    public MultiLineStringChainWriter() {
        this.nextChain = null;
    }

    public MultiLineStringChainWriter(TransportChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void handleTransportModels(Transport transport) throws IOException {
        FileWriter fileWriter = new FileWriter("transportModels.txt");
        try (PrintWriter writer = new PrintWriter(fileWriter)) {
            for (String model : transport.getAllModelsNames()) {
                writer.println(model);
            }
        }
    }

    @Override
    public void setNextChain(TransportChain nextChain) {
        this.nextChain = nextChain;
    }
}
