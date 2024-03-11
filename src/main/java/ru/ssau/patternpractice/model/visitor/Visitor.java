package ru.ssau.patternpractice.model.visitor;

import ru.ssau.patternpractice.model.Automobile;
import ru.ssau.patternpractice.model.Motorcycle;

public interface Visitor {
    void visit(Automobile automobile);
    void visit(Motorcycle motorcycle);
}
