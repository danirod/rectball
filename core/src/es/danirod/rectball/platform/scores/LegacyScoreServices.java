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

package es.danirod.rectball.platform.scores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Class designed to emulate how the 0.4.0-pre Rectball stored scores.
 *
 * @author Dani Rodríguez
 */
public abstract class LegacyScoreServices implements ScoreServices {

    private int highScore;

    private int highTime;

    @Override
    public final void readData() {
        FileHandle scores = getScoresFile();
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

    @Override
    public final void flushData() {
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
        getScoresFile().writeString(encodedData, false);
    }

    @Override
    public final void registerScore(int score, int time) {
        highScore = Math.max(highScore, score);
        highTime = Math.max(highTime, time);
    }

    @Override
    public final int getHighScore() {
        return highScore;
    }

    @Override
    public final int getHighTime() {
        return highTime;
    }

    protected abstract FileHandle getScoresFile();

}
