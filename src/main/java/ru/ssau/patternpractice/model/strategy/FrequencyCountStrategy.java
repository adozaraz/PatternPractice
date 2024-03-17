package ru.ssau.patternpractice.model.strategy;

import java.util.*;

public class FrequencyCountStrategy implements CountStrategy {
    @Override
    public Map<Integer, Integer> count(List<Integer> integerList) {
        HashSet<Integer> uniqueIntegers = new HashSet<>(integerList);
        Map<Integer, Integer> counter = new HashMap<>();
        for (Integer i : uniqueIntegers) {
            counter.put(i, Collections.frequency(integerList, i));
        }
        return counter;
    }
}
