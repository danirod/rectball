package es.danirod.rectball.statistics;

import java.util.*;

/**
 * Group of related statistics. This class groups some related stats so that
 * they can be stored together in the serialized file.
 */
public class StatisticSet {

    private String name;

    private Map<String, Integer> values;

    public StatisticSet() {
        values = new HashMap<>();
    }

    public int getValue(String id) {
        return values.containsKey(id) ? values.get(id) : 0;
    }

    public void incrementValue(String id) {
        if (values.containsKey(id)) {
            int combinations = values.get(id);
            values.put(id, combinations + 1);
        } else {
            values.put(id, 1);
        }
    }

    public void incrementValue(String id, int count) {
        if (values.containsKey(id)) {
            int combinations = values.get(id);
            values.put(id, combinations + count);
        } else {
            values.put(id, count);
        }
    }

    public void setValue(String id, int value) {
        values.put(id, value);
    }

    public Map<String, Integer> getStats() {
        return Collections.unmodifiableMap(values);
    }

}
