/*
 * This file is part of Rectball
 * Copyright (C) 2015 Dani Rodríguez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.danirod.rectball.model;

import java.util.*;

public class Statistics {

    private final StatisticSet total;
    private final StatisticSet colors;
    private final StatisticSet sizes;

    public Statistics() {
        total = new StatisticSet();
        colors = new StatisticSet();
        sizes = new StatisticSet();
    }

    public StatisticSet getTotalData() {
        return total;
    }

    public StatisticSet getColorData() {
        return colors;
    }

    public StatisticSet getSizesData() {
        return sizes;
    }

    /**
     * Group of related statistics. This class groups some related stats so that
     * they can be stored together in the serialized file.
     */
    public static class StatisticSet {

        private final Map<String, Integer> values;

        public StatisticSet() {
            values = new HashMap<>();
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

        public Map<String, Integer> getStats() {
            return Collections.unmodifiableMap(values);
        }

    }
}
