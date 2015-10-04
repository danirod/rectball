package es.danirod.rectball.settings;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import es.danirod.rectball.utils.FileUtils;

/**
 * Load and save scores.
 */
public class ScoreIO {

    public static void save(Scores scores) {
        FileHandle handle;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            handle = Gdx.files.absolute(FileUtils.getApplicationFolder() + "/scores");
        } else {
            handle = Gdx.files.local("scores");
        }
        String data = encodeData(scores);
        handle.writeString(data, false);
    }

    public static Scores load() {
        try {
            FileHandle handle;
            if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                handle = Gdx.files.absolute(FileUtils.getApplicationFolder() + "/scores");
            } else {
                handle = Gdx.files.local("scores");
            }
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

}
