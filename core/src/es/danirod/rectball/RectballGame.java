package es.danirod.rectball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

import es.danirod.rectball.screens.*;
import es.danirod.rectball.screens.debug.CombinationDebugScreen;
import es.danirod.rectball.screens.debug.DebugScreen;
import es.danirod.rectball.settings.Scores;
import es.danirod.rectball.settings.Settings;
import es.danirod.rectball.utils.SoundPlayer;

/**
 * Main class for the game.
 */
public class RectballGame extends Game {

    public static final String VERSION = "Rectball v0.0.7";

    private Map<Integer, AbstractScreen> screens;

    public Settings settings;

    public Scores scores;

    public AssetManager manager;

    public SoundPlayer player;

    @Override
    public void create() {
        this.screens = new HashMap<>();
        this.addScreen(new GameScreen(this));
        this.addScreen(new GameOverScreen(this));
        this.addScreen(new WelcomeScreen(this));
        this.addScreen(new SettingsScreen(this));
        this.addScreen(new LoadingScreen(this));
        this.addScreen(new DebugScreen(this));
        this.addScreen(new CombinationDebugScreen(this));

        manager = new AssetManager();
        manager.load("board/normal.png", Texture.class);
        manager.load("board/colorblind.png", Texture.class);
        manager.load("fonts/scores.fnt", BitmapFont.class);
        manager.load("sound/fail.ogg", Sound.class);
        manager.load("sound/gameover.ogg", Sound.class);
        manager.load("sound/select.ogg", Sound.class);
        manager.load("sound/success.ogg", Sound.class);
        manager.load("sound/unselect.ogg", Sound.class);
        manager.load("ui/switch.png", Texture.class);
        manager.load("ui/button.png", Texture.class);
        manager.load("scores.png", Texture.class);
        manager.load("timer.png", Texture.class);

        for (Map.Entry<Integer, AbstractScreen> screen : screens.entrySet()) {
            screen.getValue().load();
        }

        settings = new Settings(Gdx.app.getPreferences("rectball"));
        scores = new Scores();
        player = new SoundPlayer(this);

        setScreen(5);
    }

    @Override
    public void dispose() {
        manager.dispose();
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
