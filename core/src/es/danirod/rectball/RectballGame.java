package es.danirod.rectball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

import es.danirod.rectball.screens.AbstractScreen;
import es.danirod.rectball.screens.GameScreen;

/**
 * Main class for the game.
 */
public class RectballGame extends Game {

	public SpriteBatch batch;

    private Map<Integer, AbstractScreen> screens;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.screens = new HashMap<>();
        this.addScreen(new GameScreen(this));

        AssetManager manager = AssetLoader.get();
        manager.load("balls/basic/red.png", Texture.class);
        manager.load("balls/basic/green.png", Texture.class);
        manager.load("balls/basic/yellow.png", Texture.class);
        manager.load("balls/basic/blue.png", Texture.class);
        manager.load("balls/basic/selected.png", Texture.class);
        manager.load("scores.png", Texture.class);
        manager.load("timer.png", Texture.class);
        manager.finishLoading();

        for (Map.Entry<Integer, AbstractScreen> screen : screens.entrySet()) {
            screen.getValue().load();
        }

        setScreen(1);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    /**
     * Show the screen with this ID.
     * @param id  the ID of the screen to use.
     */
    public void setScreen(int id) {
        this.setScreen(screens.get(id));
    }

    /**
     * Add a screen to the map of Strings.
     * @param screen
     */
    public void addScreen(AbstractScreen screen) {
        screens.put(screen.getID(), screen);
    }

    @Override
    public void resize(int width, int height) {

    }
}
