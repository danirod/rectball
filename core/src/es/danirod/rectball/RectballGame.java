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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import es.danirod.rectball.screens.*;
import es.danirod.rectball.settings.ScoreIO;
import es.danirod.rectball.settings.Scores;
import es.danirod.rectball.settings.Settings;
import es.danirod.rectball.statistics.Statistics;
import es.danirod.rectball.utils.SoundPlayer;
import es.danirod.rectball.utils.StyleFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class for the game.
 */
public class RectballGame extends Game {

    public static final String VERSION = "Rectball 0.1.2-SNAPSHOT";

    private Map<Integer, AbstractScreen> screens = new HashMap<>();

    public Settings settings;

    public Scores scores;

    public Statistics statistics;

    public AssetManager manager;

    public SoundPlayer player;

    public float aliveTime;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("RectballGame", "Welcome to " + VERSION);

        Gdx.app.debug("RectballGame", "Adding Game Screens...");
        this.addScreen(new GameScreen(this));
        this.addScreen(new GameOverScreen(this));
        this.addScreen(new MainMenuScreen(this));
        this.addScreen(new SettingsScreen(this));
        this.addScreen(new LoadingScreen(this));
        this.addScreen(new StatisticsScreen(this));

        Gdx.app.debug("RectballGame", "Adding assets to the manager...");
        manager = createManager();

        Gdx.app.debug("RectballGame", "Loading screens...");
        for (Map.Entry<Integer, AbstractScreen> screen : screens.entrySet()) {
            Gdx.app.debug("RectballGame", "Loading screen " + screen.getValue().getID() + "...");
            screen.getValue().load();
        }

        Gdx.app.debug("RectballGame", "Reading settings...");
        settings = new Settings(Gdx.app.getPreferences("rectball"));
        scores = ScoreIO.load();
        statistics = Statistics.loadStats();
        player = new SoundPlayer(this);

        Gdx.app.debug("RectballGame", "Loading assets...");
        setScreen(5);
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
        manager.load("ui/switch.png", Texture.class, linearParameters);
        manager.load("ui/button.png", Texture.class, linearParameters);
        manager.load("scores.png", Texture.class);
        manager.load("fonts/scores.fnt", BitmapFont.class);
        manager.load("timer.png", Texture.class);
        manager.load("ui/leave.png", Texture.class);

        // Load TTF font for normal text
        FreeTypeFontLoaderParameter normalFont = new FreeTypeFontLoaderParameter();
        normalFont.fontFileName = "fonts/Play-Regular.ttf";
        normalFont.fontParameters.size = 64;
        normalFont.fontParameters.minFilter = TextureFilter.Linear;
        normalFont.fontParameters.magFilter = TextureFilter.Linear;
        normalFont.fontParameters.shadowOffsetX = 2;
        normalFont.fontParameters.shadowOffsetY = 2;
        manager.load("normalFont.ttf", BitmapFont.class, normalFont);

        // Load TTF font for bold text
        FreeTypeFontLoaderParameter boldFont = new FreeTypeFontLoaderParameter();
        boldFont.fontFileName = "fonts/Play-Bold.ttf";
        boldFont.fontParameters.size = 64;
        boldFont.fontParameters.minFilter = TextureFilter.Linear;
        boldFont.fontParameters.magFilter = TextureFilter.Linear;
        boldFont.fontParameters.shadowOffsetX = 2;
        boldFont.fontParameters.shadowOffsetY = 2;
        manager.load("boldFont.ttf", BitmapFont.class, boldFont);

        // Load TTF font for big text
        FreeTypeFontLoaderParameter bigFont = new FreeTypeFontLoaderParameter();
        bigFont.fontFileName = "fonts/Play-Regular.ttf";
        bigFont.fontParameters.size = 96;
        bigFont.fontParameters.minFilter = TextureFilter.Linear;
        bigFont.fontParameters.magFilter = TextureFilter.Linear;
        bigFont.fontParameters.borderWidth = 2;
        bigFont.fontParameters.shadowOffsetX = 4;
        bigFont.fontParameters.shadowOffsetY = 4;
        manager.load("bigFont.ttf", BitmapFont.class, bigFont);

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
