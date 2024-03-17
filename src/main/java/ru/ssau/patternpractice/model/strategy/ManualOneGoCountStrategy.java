package ru.ssau.patternpractice.model.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManualOneGoCountStrategy implements CountStrategy {
    @Override
    public Map<Integer, Integer> count(List<Integer> integerList) {
        Map<Integer, Integer> counter = new HashMap<>();
        for (Integer i : integerList) {
            if (counter.containsKey(i)) {
                counter.put(i, 1);
            } else {
                counter.put(i, counter.get(i) + 1);
            }
        }
        return counter;
    }
}
