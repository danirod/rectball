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

import es.danirod.rectball.model.Statistics;

/**
 * @author danirod
 */
public abstract class LegacyStats implements Stats {

    @Override
    public void saveStatistics(Statistics statistics) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonData = json.toJson(statistics);

        // Encode stats in Base64 and save it to a file.
        String encodedJson = Base64Coder.encodeString(jsonData);
        FileHandle handle;
        handle = getStatistics();
        handle.writeString(encodedJson, false);
    }

    @Override
    public Statistics loadStatistics() {
        try {
            // Read stats from file and decode them.
            FileHandle handle = getStatistics();
            String encodedJson = handle.readString();
            String decodedJson = Base64Coder.decodeString(encodedJson);

            // Convert JSON to stats
            JsonReader reader = new JsonReader();
            JsonValue rootStats = reader.parse(decodedJson);
            Statistics.StatisticSet total = parseTotal(rootStats);
            Statistics.StatisticSet colors = parseColor(rootStats);
            Statistics.StatisticSet sizes = parseSizes(rootStats);
            return new Statistics(total, colors, sizes);
        } catch (Exception ex) {
            return new Statistics();
        }
    }

    protected abstract FileHandle getStatistics();

    Statistics.StatisticSet parseTotal(JsonValue root) {
        JsonValue total = root.get("total").get("values");
        Statistics.StatisticSet stat = new Statistics.StatisticSet();
        stat.incrementValue("score", total.getInt("score"));
        stat.incrementValue("combinations", total.getInt("combinations"));
        stat.incrementValue("balls", total.getInt("balls"));
        stat.incrementValue("games", total.getInt("games"));
        stat.incrementValue("time", total.getInt("time"));
        return stat;
    }

    Statistics.StatisticSet parseColor(JsonValue root) {
        JsonValue color = root.get("colors").get("values");
        Statistics.StatisticSet stat = new Statistics.StatisticSet();
        stat.incrementValue("red", color.getInt("red"));
        stat.incrementValue("blue", color.getInt("blue"));
        stat.incrementValue("green", color.getInt("green"));
        stat.incrementValue("yellow", color.getInt("yellow"));
        return stat;
    }

    Statistics.StatisticSet parseSizes(JsonValue root) {
        JsonValue sizes = root.get("sizes").get("values");
        Statistics.StatisticSet stat = new Statistics.StatisticSet();
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
