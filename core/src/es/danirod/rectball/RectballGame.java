/*
 * This file is part of Rectball.
 * Copyright (C) 2015 Dani Rodríguez.
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
package es.danirod.rectball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.utils.I18NBundle;
import es.danirod.rectball.model.GameState;
import es.danirod.rectball.screens.*;
import es.danirod.rectball.settings.ScoreIO;
import es.danirod.rectball.settings.Scores;
import es.danirod.rectball.settings.Settings;
import es.danirod.rectball.statistics.Statistics;
import es.danirod.rectball.utils.RectballSkin;
import es.danirod.rectball.utils.SoundPlayer;

import java.util.*;

/**
 * Main class for the game.
 */
public class RectballGame extends Game {

    public static final String VERSION = "Rectball 0.3.0-SNAPSHOT";

    /* FIXME: Privatize this. */

    private Map<Integer, AbstractScreen> screens = new HashMap<>();

    public Settings settings;

    public Scores scores;

    public Statistics statistics;

    public AssetManager manager;

    public SoundPlayer player;

    private RectballSkin uiSkin;

    private GameState currentGame = new GameState();

    private TextureAtlas ballAtlas;

    private I18NBundle locale;

    private Deque<AbstractScreen> screenStack = new ArrayDeque<>();

    @Override
    public void create() {
        // Add the screens.
        addScreen(new GameScreen(this));
        addScreen(new GameOverScreen(this));
        addScreen(new MainMenuScreen(this));
        addScreen(new SettingsScreen(this));
        addScreen(new LoadingScreen(this));
        addScreen(new StatisticsScreen(this));
        addScreen(new AboutScreen(this));

        // Load the resources.
        manager = createManager();
        screens.get(Screens.LOADING).load();
        setScreen(screens.get(Screens.LOADING));
    }

    public void finishLoading() {
        // Load the remaining data.
        settings = new Settings(Gdx.app.getPreferences("rectball"));
        scores = ScoreIO.load();
        statistics = Statistics.loadStats();
        player = new SoundPlayer(this);
        uiSkin = new RectballSkin(this);
        updateBallAtlas();
        locale = setUpLocalization();

        // Load the screens.
        for (Map.Entry<Integer, AbstractScreen> screen : screens.entrySet()) {
            screen.getValue().load();
        }

        // Enter main menu.
        pushScreen(Screens.MAIN_MENU);
    }

    private I18NBundle setUpLocalization() {
        FileHandle baseFileHandle = Gdx.files.internal("locale/rectball");
        return I18NBundle.createBundle(baseFileHandle);
    }

    public I18NBundle getLocale() {
        return locale;
    }

    private AssetManager createManager() {
        AssetManager manager = new AssetManager();

        // Set up the loaders required to load TTF files using gdx-freetype.
        FileHandleResolver freetypeResolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(freetypeResolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(freetypeResolver));

        // Set up the parameters for loading linear textures. Linear textures
        // use a linear filter to not have artifacts when they are scaled.
        TextureParameter linearParameters = new TextureParameter();
        linearParameters.minFilter = linearParameters.magFilter = TextureFilter.Linear;

        // Load game assets.
        manager.load("logo.png", Texture.class, linearParameters);
        manager.load("board/normal.png", Texture.class, linearParameters);
        manager.load("board/colorblind.png", Texture.class, linearParameters);

        // Load UI resources.
        manager.load("ui/progress.png", Texture.class, linearParameters);
        manager.load("ui/icons.png", Texture.class, linearParameters);
        manager.load("ui/yellowpatch.png", Texture.class);
        manager.load("ui/switch.png", Texture.class, linearParameters);

        // Load TTF font for normal text
        FreeTypeFontLoaderParameter normalFont = new FreeTypeFontLoaderParameter();
        normalFont.fontFileName = "fonts/Play-Regular.ttf";
        normalFont.fontParameters.size = 48;
        normalFont.fontParameters.minFilter = TextureFilter.Linear;
        normalFont.fontParameters.magFilter = TextureFilter.Linear;
        normalFont.fontParameters.shadowOffsetX = 2;
        normalFont.fontParameters.shadowOffsetY = 2;
        manager.load("normalFont.ttf", BitmapFont.class, normalFont);

        // Load TTF font for bold text
        FreeTypeFontLoaderParameter boldFont = new FreeTypeFontLoaderParameter();
        boldFont.fontFileName = "fonts/Play-Bold.ttf";
        boldFont.fontParameters.size = 48;
        boldFont.fontParameters.minFilter = TextureFilter.Linear;
        boldFont.fontParameters.magFilter = TextureFilter.Linear;
        boldFont.fontParameters.shadowOffsetX = 2;
        boldFont.fontParameters.shadowOffsetY = 2;
        manager.load("boldFont.ttf", BitmapFont.class, boldFont);

        // Load TTF font for big text
        FreeTypeFontLoaderParameter bigFont = new FreeTypeFontLoaderParameter();
        bigFont.fontFileName = "fonts/Play-Regular.ttf";
        bigFont.fontParameters.size = 64;
        bigFont.fontParameters.minFilter = TextureFilter.Linear;
        bigFont.fontParameters.magFilter = TextureFilter.Linear;
        bigFont.fontParameters.borderWidth = 2;
        bigFont.fontParameters.shadowOffsetX = 4;
        bigFont.fontParameters.shadowOffsetY = 4;
        manager.load("bigFont.ttf", BitmapFont.class, bigFont);

        // Load TTF font for small text
        FreeTypeFontLoaderParameter smallFont = new FreeTypeFontLoaderParameter();
        smallFont.fontFileName = "fonts/Play-Regular.ttf";
        smallFont.fontParameters.size = 36;
        smallFont.fontParameters.minFilter = TextureFilter.Linear;
        smallFont.fontParameters.magFilter = TextureFilter.Linear;
        manager.load("smallFont.ttf", BitmapFont.class, smallFont);

        // Load TTF font for Press Start.
        FreeTypeFontLoaderParameter monospace = new FreeTypeFontLoaderParameter();
        monospace.fontFileName = "fonts/PressStart2P-Regular.ttf";
        monospace.fontParameters.size = 8;
        monospace.fontParameters.minFilter = TextureFilter.Nearest;
        monospace.fontParameters.magFilter = TextureFilter.Nearest;
        manager.load("monospace.ttf", BitmapFont.class, monospace);

        // Load sounds
        manager.load("sound/fail.ogg", Sound.class);
        manager.load("sound/gameover.ogg", Sound.class);
        manager.load("sound/select.ogg", Sound.class);
        manager.load("sound/success.ogg", Sound.class);
        manager.load("sound/unselect.ogg", Sound.class);

        return manager;
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

    /**
     * Pushes the provided screen into the stack and sets it as the current screen.
     * The screen that has been previously on screen can be retrieved later using
     * popScreen.
     *
     * @since 0.3.0
     * @param id  the screen that should be visible now.
     */
    public void pushScreen(int id) {
        screenStack.push(screens.get(id));
        setScreen(screenStack.peek());
    }

    /**
     * Pops the current screen from the stack. The screen that was visible before
     * pushing the current screen is the one that would be visible. If no screens
     * are in the stack, the main menu screen will be visible.
     *
     * @since 0.3.0
     */
    public void popScreen() {
        screenStack.pop();
        if (screenStack.isEmpty()) {
            setScreen(screens.get(Screens.MAIN_MENU));
        } else {
            setScreen(screenStack.peek());
        }
    }

    /**
     * Clears the stack of screens. Every screen in the stack is removed and
     * the main menu gets as the current screen visible.
     *
     * @since 0.3.0
     */
    public void clearStack() {
        screenStack.clear();
        setScreen(screens.get(Screens.MAIN_MENU));
    }

    /**
     * Add a screen to the map of Strings.
     * @param screen
     */
    public void addScreen(AbstractScreen screen) {
        screens.put(screen.getID(), screen);
    }

    /**
     * Get the skin used by Scene2D UI to display things.
     * @return  the skin the game should use.
     */
    public RectballSkin getSkin() {
        return uiSkin;
    }

    public GameState getState() {
        return currentGame;
    }

    public void updateBallAtlas() {
        Texture balls = manager.get(settings.isColorblind() ? "board/colorblind.png" : "board/normal.png");
        TextureRegion[][] regions = TextureRegion.split(balls, 256, 256);
        ballAtlas = new TextureAtlas();
        ballAtlas.addRegion("ball_red", regions[0][0]);
        ballAtlas.addRegion("ball_yellow", regions[0][1]);
        ballAtlas.addRegion("ball_blue", regions[1][0]);
        ballAtlas.addRegion("ball_green", regions[1][1]);
        ballAtlas.addRegion("ball_gray", regions[1][2]);
    }

    public TextureAtlas getBallAtlas() {
        return ballAtlas;
    }
}
