/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2017 Dani Rodríguez.
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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import es.danirod.rectball.android.AndroidLauncher;
import es.danirod.rectball.android.BuildConfig;
import es.danirod.rectball.android.settings.SettingsManager;
import es.danirod.rectball.model.GameState;
import es.danirod.rectball.scene2d.RectballSkin;
import es.danirod.rectball.screens.AboutScreen;
import es.danirod.rectball.screens.AbstractScreen;
import es.danirod.rectball.screens.GameOverScreen;
import es.danirod.rectball.screens.GameScreen;
import es.danirod.rectball.screens.LoadingBackScreen;
import es.danirod.rectball.screens.LoadingScreen;
import es.danirod.rectball.screens.MainMenuScreen;
import es.danirod.rectball.screens.TutorialScreen;
import es.danirod.rectball.screens.Screens;
import es.danirod.rectball.screens.SettingsScreen;
import es.danirod.rectball.screens.StatisticsScreen;

/**
 * Main class for the game.
 */
public class RectballGame extends StateBasedGame {

    private int marginTop = 0, marginLeft = 0, marginRight = 0, marginBottom = 0;

    /** The Android activity that is currently displaying this game instance. */
    private final AndroidLauncher context;

    /** The game state is used during a game in order to pause and serialize it. */
    private final GameState currentGame;

    /** The asset manager holds the resources required during the game. */
    public AssetManager manager;

    /** The sound player manages the sound effects during the game. */
    public SoundPlayer player;

    /** Skin used by Scene2D. */
    private RectballSkin uiSkin;

    /** Contains the bubbles textures, depending on the application settings. */
    private TextureAtlas ballAtlas;

    /** Whether the game is restoring state from an Android kill or not. */
    private boolean restoredState;

    /** Batch instance in use by the game. */
    private Batch batch;

    public RectballGame(AndroidLauncher context) {
        this.context = context;
        this.currentGame = new GameState();
        this.restoredState = false;
    }

    public RectballGame(AndroidLauncher context, GameState state) {
        this.context = context;
        this.currentGame = state;
        this.restoredState = true;
    }

    public void updateMargin(int top, int bottom, int left, int right) {
        marginTop = top;
        marginBottom = bottom;
        marginLeft = left;
        marginRight = right;
        Gdx.app.log("RectballGame", "Insets have been set to: top=" + top + " bottom=" + bottom + " left=" + left + " right=" + right);
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public float getMarginTop() {
        return marginTop;
    }

    @Override
    public void create() {
        if (BuildConfig.FINE_DEBUG) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        // Android enables dithering by default on some phones. Disable it for higher quality.
        Gdx.gl.glDisable(GL20.GL_DITHER);

        addScreen(new GameScreen(this));
        addScreen(new GameOverScreen(this));
        addScreen(new MainMenuScreen(this));
        addScreen(new SettingsScreen(this));
        addScreen(new LoadingScreen(this));
        addScreen(new LoadingBackScreen(this));
        addScreen(new StatisticsScreen(this));
        addScreen(new AboutScreen(this));
        addScreen(new TutorialScreen(this));

        batch = new SpriteBatch();
        manager = AssetManagerBuilder.INSTANCE.build();

        if (!restoredState) {
            getScreen(Screens.LOADING).load();
            setScreen(getScreen(Screens.LOADING));
        } else {
            getScreen(Screens.LOADING_BACK).load();
            setScreen(getScreen(Screens.LOADING_BACK));
        }
    }

    public void finishLoading() {
        // These resources cannot be initialized until the AssetManager finishes loading.
        player = new SoundPlayer(this);
        uiSkin = new RectballSkin(this);
        updateBallAtlas();

        for (AbstractScreen screen : getAllScreens()) {
            screen.load();
        }

        // Enter next screen.
        pushScreen(Screens.MAIN_MENU);
        if (restoredState) {
            // If playing, enter the game screen as well. Keep MAIN_MENU in the stack.
            pushScreen(Screens.GAME);
        }
    }

    public void updateBallAtlas() {
        boolean isColorblind = context.getSettings().getPreferences().getBoolean(SettingsManager.TAG_ENABLE_COLORBLIND, false);
        String ballsTexture = isColorblind ? "board/colorblind.png" : "board/normal.png";
        Texture balls = manager.get(ballsTexture);
        TextureRegion[][] regions = TextureRegion.split(balls, 256, 256);
        ballAtlas = new TextureAtlas();
        ballAtlas.addRegion("ball_red", regions[0][0]);
        ballAtlas.addRegion("ball_yellow", regions[0][1]);
        ballAtlas.addRegion("ball_blue", regions[1][0]);
        ballAtlas.addRegion("ball_green", regions[1][1]);
        ballAtlas.addRegion("ball_gray", regions[1][2]);
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

    public RectballSkin getSkin() {
        return uiSkin;
    }

    public GameState getState() {
        return currentGame;
    }

    public boolean isRestoredState() {
        return restoredState;
    }

    public Batch getBatch() {
        return batch;
    }

    public AndroidLauncher getContext() {
        return context;
    }

    public void updateWakelock(boolean wakelock) {
        if (wakelock) {
            context.requestWakelock();
        } else {
            context.clearWakelock();
        }
    }

    public TextureAtlas getBallAtlas() {
        return ballAtlas;
    }

    public void setRestoredState(boolean restoredState) {
        this.restoredState = restoredState;
    }

    @Override
    public int getEmptyStackScreen() {
        return Screens.MAIN_MENU;
    }
}
