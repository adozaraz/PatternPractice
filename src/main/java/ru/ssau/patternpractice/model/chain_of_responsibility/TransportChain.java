package ru.ssau.patternpractice.model.chain_of_responsibility;

import ru.ssau.patternpractice.model.Transport;

import java.io.IOException;

public interface TransportChain {
    void handleTransportModels(Transport transport) throws IOException;

    void setNextChain(TransportChain nextChain);
}
