package es.danirod.rectball.settings;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

/**
 * Load and save scores.
 */
public class ScoreIO {

    public static void save(Scores scores) {
        FileHandle handle = getFile();
        String data = encodeData(scores);
        handle.writeString(data, false);
    }

    public static Scores load() {
        try {
            FileHandle handle = getFile();
            String encodedData = handle.readString();
            return decodeData(encodedData);
        } catch (RuntimeException e) {
            return new Scores();
        }
    }

    private static String encodeData(Scores scores) {
        Json json = new Json();
        json.setOutputType(OutputType.javascript);
        String rawJson = json.toJson(scores);
        return Base64Coder.encodeString(rawJson);
    }

    private static Scores decodeData(String data) {
        String rawJson = Base64Coder.decodeString(data);
        Json json = new Json();
        return json.fromJson(Scores.class, rawJson);
    }

    /**
     * Get the path for the file where the scores should be saved.
     * @return  path for saving the file.
     */
    private static FileHandle getFile() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            String base, os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win"))
                base = System.getenv("AppData") + "/rectball";
            else if (os.contains("mac"))
                base = System.getProperty("user.home") + "/Library/Application Support/rectball";
            else
                base = System.getProperty("user.home") + "/.rectball";
            return Gdx.files.absolute(base + "/scores");
        } else {
            return Gdx.files.local("scores");
        }
    }

}
