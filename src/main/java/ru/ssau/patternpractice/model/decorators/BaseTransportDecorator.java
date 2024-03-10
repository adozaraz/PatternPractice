package ru.ssau.patternpractice.model.decorators;

import ru.ssau.patternpractice.model.Transport;

public abstract class BaseTransportDecorator implements Transport {
    protected final Transport wrapped;

    protected BaseTransportDecorator(Transport wrapped) { this.wrapped = wrapped; }
}
