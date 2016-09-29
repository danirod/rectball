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

package es.danirod.rectball.platform;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;

/**
 * @author danirod
 */
public abstract class LegacyStatistics implements Statistics {

    @Override
    public void saveStatistics(es.danirod.rectball.model.Statistics statistics) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonData = json.toJson(statistics);

        // Encode statistics in Base64 and save it to a file.
        String encodedJson = Base64Coder.encodeString(jsonData);
        FileHandle handle;
        handle = getStatistics();
        handle.writeString(encodedJson, false);
    }

    @Override
    public es.danirod.rectball.model.Statistics loadStatistics() {
        try {
            // Read stats from file and decode them.
            FileHandle handle = getStatistics();
            String encodedJson = handle.readString();
            String decodedJson = Base64Coder.decodeString(encodedJson);

            // Convert JSON to statistics
            JsonReader reader = new JsonReader();
            JsonValue rootStats = reader.parse(decodedJson);
            es.danirod.rectball.model.Statistics.StatisticSet total = parseTotal(rootStats);
            es.danirod.rectball.model.Statistics.StatisticSet colors = parseColor(rootStats);
            es.danirod.rectball.model.Statistics.StatisticSet sizes = parseSizes(rootStats);
            return new es.danirod.rectball.model.Statistics(total, colors, sizes);
        } catch (Exception ex) {
            return new es.danirod.rectball.model.Statistics();
        }
    }

    protected abstract FileHandle getStatistics();

    es.danirod.rectball.model.Statistics.StatisticSet parseTotal(JsonValue root) {
        JsonValue total = root.get("total").get("values");
        es.danirod.rectball.model.Statistics.StatisticSet stat = new es.danirod.rectball.model.Statistics.StatisticSet();
        stat.incrementValue("score", total.getInt("score"));
        stat.incrementValue("combinations", total.getInt("combinations"));
        stat.incrementValue("balls", total.getInt("balls"));
        stat.incrementValue("games", total.getInt("games"));
        stat.incrementValue("time", total.getInt("time"));
        return stat;
    }

    es.danirod.rectball.model.Statistics.StatisticSet parseColor(JsonValue root) {
        JsonValue color = root.get("colors").get("values");
        es.danirod.rectball.model.Statistics.StatisticSet stat = new es.danirod.rectball.model.Statistics.StatisticSet();
        stat.incrementValue("red", color.getInt("red"));
        stat.incrementValue("blue", color.getInt("blue"));
        stat.incrementValue("green", color.getInt("green"));
        stat.incrementValue("yellow", color.getInt("yellow"));
        return stat;
    }

    es.danirod.rectball.model.Statistics.StatisticSet parseSizes(JsonValue root) {
        JsonValue sizes = root.get("sizes").get("values");
        es.danirod.rectball.model.Statistics.StatisticSet stat = new es.danirod.rectball.model.Statistics.StatisticSet();
        JsonValue.JsonIterator size = sizes.iterator();
        while (size.hasNext()) {
            JsonValue val = size.next();
            if (val.name.equals("class"))
                continue;
            stat.incrementValue(val.name, val.asInt());
        }
        return stat;
    }
}
