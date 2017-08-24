/*
 * This file is part of Rectball
 * Copyright (C) 2015-2017 Dani Rodríguez
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

package es.danirod.rectball.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StatisticsIO {

    private static final String STATISTICS_FILE = "stats";

    private StatisticSet total = new StatisticSet();
    private StatisticSet colors = new StatisticSet();
    private StatisticSet sizes = new StatisticSet();

    public void saveStatistics() {
        Gdx.app.log("StatisticsIO", "Saving statistics file...");

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonData = json.toJson(this);
        Gdx.app.debug("StatisticsIO", "Statistics contents: " + jsonData);

        // Encode statistics in Base64 and save it to a file.
        String encodedJson = Base64Coder.encodeString(jsonData);
        FileHandle handle = Gdx.files.local(STATISTICS_FILE);
        handle.writeString(encodedJson, false);

        Gdx.app.log("StatisticsIO", "Statistics file has been saved.");
    }

    public void loadStatistics() {
        Gdx.app.log("StatisticsIO", "Loading statistics file...");
        try {
            // Read stats from file and decode them.
            FileHandle handle = Gdx.files.local(STATISTICS_FILE);
            String encodedJson = handle.readString();
            String decodedJson = Base64Coder.decodeString(encodedJson);
            Gdx.app.log("StatisticsIO", "Successfully decoded statistics file");

            // Convert JSON to statistics
            JsonReader reader = new JsonReader();
            JsonValue rootStats = reader.parse(decodedJson);
            this.total = parseTotal(rootStats);
            this.colors = parseColor(rootStats);
            this.sizes = parseSizes(rootStats);
            Gdx.app.log("StatisticsIO", "Successfully loaded statistics");
        } catch (Exception ex) {
            Gdx.app.error("StatisticsIO", "Cannot load statistics", ex);
        }
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

    private StatisticSet parseTotal(JsonValue root) {
        JsonValue total = root.get("total").get("values");
        StatisticSet stat = new StatisticSet();
        stat.incrementValue("score", total.getInt("score", 0));
        stat.incrementValue("combinations", total.getInt("combinations", 0));
        stat.incrementValue("balls", total.getInt("balls", 0));
        stat.incrementValue("games", total.getInt("games", 0));
        stat.incrementValue("time", total.getInt("time", 0));
        return stat;
    }

    private StatisticSet parseColor(JsonValue root) {
        JsonValue color = root.get("colors").get("values");
        StatisticSet stat = new StatisticSet();
        stat.incrementValue("red", color.getInt("red", 0));
        stat.incrementValue("blue", color.getInt("blue", 0));
        stat.incrementValue("green", color.getInt("green", 0));
        stat.incrementValue("yellow", color.getInt("yellow", 0));
        return stat;
    }

    private StatisticSet parseSizes(JsonValue root) {
        JsonValue sizes = root.get("sizes").get("values");
        StatisticSet stat = new StatisticSet();
        JsonValue.JsonIterator size = sizes.iterator();
        while (size.hasNext()) {
            JsonValue val = size.next();
            if (val.name.equals("class"))
                continue;
            stat.incrementValue(val.name, val.asInt());
        }
        return stat;
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
