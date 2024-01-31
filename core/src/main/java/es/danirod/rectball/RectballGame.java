/* This file is part of Rectball
 * Copyright (C) 2015-2024  Dani Rodríguez
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package es.danirod.rectball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import es.danirod.rectball.model.GameState;
import es.danirod.rectball.scene2d.RectballSkin;
import es.danirod.rectball.screens.*;
import es.danirod.rectball.settings.AppSettings;
import es.danirod.rectball.settings.AppStatistics;

/**
 * Main class for the game.
 */
public class RectballGame extends StateBasedGame {

    public static final Color BG_COLOR = new Color(0x252b3dff);

    private int marginTop = 0, marginLeft = 0, marginRight = 0, marginBottom = 0;

    /** The Android activity that is currently displaying this game instance. */
    private final Platform context;

    /** The asset manager holds the resources required during the game. */
    public AssetManager manager;

    /** The sound player manages the sound effects during the game. */
    public SoundPlayer player;

    /** Contains the bubbles textures, depending on the application settings. */
    private TextureAtlas ballAtlas;

    /** Whether the game is restoring state from an Android kill or not. */
    private boolean restoredState;

    /** Batch instance in use by the game. */
    private Batch batch;

    private AppSettings settings;

    private AppStatistics statistics;

    private Haptics haptics;

    private Locale locale;

    public Locale getLocale() {
        return this.locale;
    }

    public RectballGame(Platform context) {
        this.context = context;
        this.restoredState = false;
    }

    public RectballGame(Platform context, GameState state) {
        this.context = context;
        this.restoredState = true;
    }

    public String getVersion() {
        String version = Gdx.files.internal("version.txt").readString().trim();
        if (!version.isEmpty() && Character.isDigit(version.charAt(0))) {
            version = "v" + version;
        }
        return version;
    }

    public void updateMargin(int top, int bottom, int left, int right) {
        marginTop = top;
        marginBottom = bottom;
        marginLeft = left;
        marginRight = right;
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

    public AppSettings getSettings() {
        return this.settings;
    }

    public AppStatistics getStatistics() {
        return this.statistics;
    }

    public Haptics getHaptics() {
        return this.haptics;
    }

    @Override
    public void create() {
        // Compatibility with web platforms.
        I18NBundle.setSimpleFormatter(true);

        // Android enables dithering by default on some phones. Disable it for higher quality.
        Gdx.gl.glDisable(GL20.GL_DITHER);

        /* Prepare the settings instance. */
        Preferences prefs = Gdx.app.getPreferences("es.danirod.rectball");
        settings = new AppSettings(prefs);
        statistics = new AppStatistics(prefs);
        haptics = new Haptics(this);

        /* Prepare the manager, and force loading the skin, as it is used for setting up the user interface. */
        manager = AssetManagerBuilder.INSTANCE.build();
        if (context.getGameServices().getSupported()) {
            AssetManagerBuilder.INSTANCE.addGameServices(manager);
        }

        manager.finishLoadingAsset("skin/rectball.json");
        manager.finishLoadingAsset("bundles/strings");
        locale = new Locale(manager.get("bundles/strings"));

        updateBallAtlas();

        batch = new SpriteBatch();

        if (!restoredState) {
            setScreen(new LoadingScreen(this));
        } else {
            setScreen(new LoadingBackScreen(this));
        }
    }

    public void finishLoading() {
        // These resources cannot be initialized until the AssetManager finishes loading.
        player = new SoundPlayer(this);
        new RectballSkin(this);
        updateBallAtlas();

        // Enter next screen.
        clearScreenStack();
        if (restoredState) {
            // If playing, enter the game screen as well. Keep MAIN_MENU in the stack.
            pushScreen(new GameScreen(this));
        }
    }

    public Skin getAppSkin() {
        return manager.get("skin/rectball.json", Skin.class);
    }

    public void updateBallAtlas() {
        boolean isColorblind = settings.getColorblindMode();
        Skin gameSkin = getAppSkin();
        String[] gems = new String[]{ "blue", "red", "green", "yellow"};
        ballAtlas = new TextureAtlas();
        for (String gem : gems) {
            String source = "gem_" + gem + (isColorblind ? "_alt" : "");
            String target = "ball_" + gem;
            ballAtlas.addRegion(target, gameSkin.getRegion(source));
        }
        ballAtlas.addRegion("ball_gray", gameSkin.getRegion("gem_gray"));
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

    public boolean isRestoredState() {
        return restoredState;
    }

    public Batch getBatch() {
        return batch;
    }

    public Platform getContext() {
        return context;
    }

    public void updateWakelock(boolean wakelock) {
        if (wakelock && settings.getKeepScreenOn()) {
            context.getWakelock().request();
        } else {
            context.getWakelock().clear();
        }
    }

    public TextureAtlas getBallAtlas() {
        return ballAtlas;
    }

    public void setRestoredState(boolean restoredState) {
        this.restoredState = restoredState;
    }

    /** @noinspection NullableProblems*/
    @Override
    public AbstractScreen getFallbackScreen() {
        return new MainMenuScreen(this);
    }
}
