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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;

public class ScoreIO {

    private static final String SCORES_FILE = "scores"; /** Scores data file location. */

    private int highScore; /** The current high score. */

    private int highTime; /** The current highest time. */

    public void readData() {
        FileHandle scores = Gdx.files.local(SCORES_FILE);
        if (scores.exists()) {
            Gdx.app.log("ScoreServices", "Score file found. Attempting to read it...");

            // The legacy scores format is a Base64 encoded file with the scores.
            String rawJsonData = Base64Coder.decodeString(scores.readString());

            // Now, let's parse this JSON data to find the keys that we need.
            JsonReader jsonReader = new JsonReader();
            JsonValue jsonData = jsonReader.parse(rawJsonData);
            highScore = jsonData.has("highestScore") ? jsonData.getInt("highestScore") : 0;
            highTime = jsonData.has("highestTime") ? jsonData.getInt("highestTime") : 0;

            Gdx.app.log("ScoreServices", "Scores loaded. Score=" + highScore + " Time=" + highTime);
        }
    }

    public void flushData() {
        String jsonData = "";
        try {
            // Convert the data to JSON.
            StringWriter writer = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.setOutputType(JsonWriter.OutputType.javascript);
            jsonWriter.object();
            jsonWriter.set("highestScore", highScore);
            jsonWriter.set("highestTime", highTime);
            jsonWriter.close();
            jsonData = writer.getBuffer().toString();
        } catch (IOException io) {
            Gdx.app.error("ScoreServices", "Cannot serialize scores", io);
        }

        String encodedData = Base64Coder.encodeString(jsonData);
        FileHandle scores = Gdx.files.local(SCORES_FILE);
        scores.writeString(encodedData, false);
    }

    public void registerScore(int score, int time) {
        highScore = Math.max(highScore, score);
        highTime = Math.max(highTime, time);
    }

    public int getHighScore() {
        return highScore;
    }

    public int getHighTime() {
        return highTime;
    }
}
