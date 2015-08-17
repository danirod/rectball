package es.danirod.rectball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

import es.danirod.rectball.screens.AbstractScreen;
import es.danirod.rectball.screens.GameOverScreen;
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
        this.addScreen(new GameOverScreen(this));

        AssetManager manager = AssetLoader.get();
        manager.load("board/normal.png", Texture.class);
        manager.load("board/colorblind.png", Texture.class);
        manager.load("scores.png", Texture.class);
        manager.load("timer.png", Texture.class);
        manager.load("gameover.png", Texture.class);
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
}
