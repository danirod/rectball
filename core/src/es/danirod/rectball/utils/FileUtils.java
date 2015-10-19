package es.danirod.rectball.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

@Deprecated
public class FileUtils {
    /**
     * Get the path for the file where the scores should be saved.
     * @return  path for saving the file.
     */
    @Deprecated
    public static String getApplicationFolder() {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            throw new RuntimeException("This method is only valid for Desktop.");
        }
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))
            return System.getenv("AppData") + "/rectball";
        else if (os.contains("mac"))
            return System.getProperty("user.home") + "/Library/Application Support/rectball";
        else
            return System.getProperty("user.home") + "/.rectball";
    }
}
