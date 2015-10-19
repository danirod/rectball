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

package es.danirod.rectball.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import es.danirod.rectball.model.Statistics;
import es.danirod.rectball.platform.statistics.StatisticsServices;

/**
 * @author danirod
 */
public class DesktopStatisticsServices implements StatisticsServices {

    @Override
    public void saveStatistics(Statistics statistics) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonData = json.toJson(statistics);

        // Encode statistics in Base64 and save it to a file.
        String encodedJson = Base64Coder.encodeString(jsonData);
        FileHandle handle;
        handle = getScoresFile();
        handle.writeString(encodedJson, false);
    }

    @Override
    public Statistics loadStatistics() {
        try {
            // Read stats from file and decode them.
            FileHandle handle = getScoresFile();
            String encodedJson = handle.readString();
            String decodedJson = Base64Coder.decodeString(encodedJson);

            // Convert JSON to statistics
            Json json = new Json();
            return json.fromJson(Statistics.class, decodedJson);
        } catch (Exception ex) {
            return new Statistics();
        }
    }

    private FileHandle getScoresFile() {
        if (SharedLibraryLoader.isWindows) {
            String location = System.getenv("AppData") + "/rectball/stats";
            return Gdx.files.absolute(location);
        } else if (SharedLibraryLoader.isLinux) {
            return Gdx.files.external(".rectball/stats");
        } else if (SharedLibraryLoader.isMac) {
            return Gdx.files.external("/Library/Application Support/rectball/stats");
        } else {
            return Gdx.files.local("scores");
        }
    }
}
