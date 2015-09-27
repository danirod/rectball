package es.danirod.rectball.statistics;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import es.danirod.rectball.utils.FileUtils;

public class Statistics {

    public static Statistics loadStats() {
        try {
            // Read stats from file and decode them.
            FileHandle handle;
            if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                handle = Gdx.files.absolute(FileUtils.getApplicationFolder() + "/stats");
            } else {
                handle = Gdx.files.local("stats");
            }
            String encodedJson = handle.readString();
            String decodedJson = Base64Coder.decodeString(encodedJson);

            // Convert JSON to statistics
            Json json = new Json();
            return json.fromJson(Statistics.class, decodedJson);
        } catch (Exception ex) {
            return new Statistics();
        }
    }

    public static void saveStats(Statistics stats) {
        // Convert statistcs to JSON.
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonData = json.toJson(stats);

        // Encode statistics in Base64 and save it to a file.
        String encodedJson = Base64Coder.encodeString(jsonData);
        FileHandle handle;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            handle = Gdx.files.absolute(FileUtils.getApplicationFolder() + "/stats");
        } else {
            handle = Gdx.files.local("stats");
        }
        handle.writeString(encodedJson, false);
    }

    private StatisticSet total, colors, sizes;

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

}
