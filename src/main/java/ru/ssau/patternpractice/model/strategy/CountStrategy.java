package ru.ssau.patternpractice.model.strategy;

import java.util.List;
import java.util.Map;

public interface CountStrategy {
    Map<Integer, Integer> count(List<Integer> integerList);
}
