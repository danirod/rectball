package es.danirod.rectball;

import com.badlogic.gdx.assets.AssetManager;

public class AssetLoader {

    private static AssetManager manager;

    public static AssetManager get() {
        if (manager == null) {
            manager = new AssetManager();
        }
        return manager;
    }

}
