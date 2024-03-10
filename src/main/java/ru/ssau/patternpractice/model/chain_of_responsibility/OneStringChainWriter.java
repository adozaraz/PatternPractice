package ru.ssau.patternpractice.model.chain_of_responsibility;

import ru.ssau.patternpractice.model.Transport;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OneStringChainWriter implements TransportChain {
    private TransportChain nextChain;

    public OneStringChainWriter() {
        this.nextChain = null;
    }

    public OneStringChainWriter(TransportChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void handleTransportModels(Transport transport) throws IOException {
        if (transport.getModelsAmount() > 3) {
            this.nextChain.handleTransportModels(transport);
        } else {
            FileWriter fileWriter = new FileWriter("transportModels.txt");
            try (PrintWriter writer = new PrintWriter(fileWriter)) {
                for (String model : transport.getAllModelsNames()) {
                    writer.print(model + " ");
                }
            }
        }
    }

    @Override
    public void setNextChain(TransportChain nextChain) {
        this.nextChain = nextChain;
    }
}
