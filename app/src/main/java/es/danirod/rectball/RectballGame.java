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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.File;
import java.nio.ByteBuffer;

import es.danirod.rectball.android.AndroidLauncher;
import es.danirod.rectball.android.BuildConfig;
import es.danirod.rectball.android.R;
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
import es.danirod.rectball.screens.Screens;
import es.danirod.rectball.screens.SettingsScreen;
import es.danirod.rectball.screens.StatisticsScreen;
import es.danirod.rectball.screens.TutorialScreen;

/**
 * Main class for the game.
 */
public class RectballGame extends StateBasedGame {

    private final AndroidLauncher context;

    private final GameState currentGame;

    public AssetManager manager;
    public SoundPlayer player;
    private RectballSkin uiSkin;
    private TextureAtlas ballAtlas;

    /** Whether the game is restoring state from an Android kill or not. */
    private boolean restoredState;

    /** Batch instance in use by the game. */
    Batch batch;

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

    @Override
    public void create() {
        if (BuildConfig.FINE_DEBUG) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        Gdx.gl.glDisable(GL20.GL_DITHER);

        // Set up SpriteBatch
        batch = new SpriteBatch();

        // Add the screens.
        addScreen(new GameScreen(this));
        addScreen(new GameOverScreen(this));
        addScreen(new MainMenuScreen(this));
        addScreen(new SettingsScreen(this));
        addScreen(new LoadingScreen(this));
        addScreen(new LoadingBackScreen(this));
        addScreen(new StatisticsScreen(this));
        addScreen(new AboutScreen(this));
        addScreen(new TutorialScreen(this));

        // Load the resources.
        manager = createManager();
        getScreen(Screens.LOADING).load();
        if (!restoredState) {
            setScreen(getScreen(Screens.LOADING));
        } else {
            setScreen(getScreen(Screens.LOADING_BACK));
        }
    }

    public Batch getBatch() {
        return batch;
    }

    public void finishLoading() {
        player = new SoundPlayer(this);
        uiSkin = new RectballSkin(this);
        updateBallAtlas();

        // Load the screens.
        for (AbstractScreen screen : getAllScreens()) {
            screen.load();
        }

        // Enter main menu.
        pushScreen(Screens.MAIN_MENU);

        // If we are restoring the game, push also the game screen.
        // Keep the main menu screen in the stack, we are going to need it
        // when we finish the game.
        if (restoredState) {
            pushScreen(Screens.GAME);
        }
    }

    public boolean isRestoredState() {
        return restoredState;
    }

    private AssetManager createManager() {
        AssetManager manager = new AssetManager();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver) {
            @Override
            public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, FreeTypeFontLoaderParameter parameter) {
                BitmapFont font = super.loadSync(manager, fileName, file, parameter);
                font.getData().setScale(1f / Gdx.graphics.getDensity());
                return font;
            }
        });

        // Set up the parameters for loading linear textures. Linear textures
        // use a linear filter to not have artifacts when they are scaled.
        TextureParameter texParameters = new TextureParameter();
        BitmapFontParameter fntParameters = new BitmapFontParameter();
        texParameters.minFilter = texParameters.magFilter = TextureFilter.Linear;
        fntParameters.minFilter = texParameters.magFilter = TextureFilter.Linear;

        // Load game assets.
        manager.load("logo.png", Texture.class, texParameters);
        manager.load("board/normal.png", Texture.class, texParameters);
        manager.load("board/colorblind.png", Texture.class, texParameters);

        // Load UI resources.
        manager.load("ui/progress.png", Texture.class, texParameters);
        manager.load("ui/icons.png", Texture.class, texParameters);
        manager.load("ui/yellow_patch.png", Texture.class);
        manager.load("ui/switch.png", Texture.class, texParameters);

        // Load Google Play Games assets.
        if (BuildConfig.FLAVOR.equals("gpe")) {
            manager.load("google/gpg_achievements.png", Texture.class, texParameters);
            manager.load("google/gpg_leaderboard.png", Texture.class, texParameters);
        }

        // Load BitmapFonts
        manager.load("fonts/monospace.fnt", BitmapFont.class);
        manager.load("fonts/monospaceOutline.fnt", BitmapFont.class);

        // Load TrueType fonts
        FreeTypeFontLoaderParameter normalPar = new FreeTypeFontLoaderParameter();
        normalPar.fontFileName = "fonts/Coda-Regular.ttf";
        normalPar.fontParameters.minFilter = normalPar.fontParameters.magFilter = TextureFilter.Linear;
        normalPar.fontParameters.size = (int) Math.ceil(28 * Gdx.graphics.getDensity());
        manager.load("fonts/normal.ttf", BitmapFont.class, normalPar);
        FreeTypeFontLoaderParameter smallPar = new FreeTypeFontLoaderParameter();
        smallPar.fontFileName = "fonts/Coda-Regular.ttf";
        smallPar.fontParameters.minFilter = smallPar.fontParameters.magFilter = TextureFilter.Linear;
        smallPar.fontParameters.size = (int) Math.ceil(23 * Gdx.graphics.getDensity());
        manager.load("fonts/small.ttf", BitmapFont.class, smallPar);

        // Load sounds
        manager.load("sound/fail.ogg", Sound.class);
        manager.load("sound/game_over.ogg", Sound.class);
        manager.load("sound/perfect.ogg", Sound.class);
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
     * Get the skin used by Scene2D UI to display things.
     *
     * @return the skin the game should use.
     */
    public RectballSkin getSkin() {
        return uiSkin;
    }

    public GameState getState() {
        return currentGame;
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

    /**
     * Create a screenshot and return the generated Pixmap. Since the game
     * goes yUp, the returned screenshot is flipped so that it's correctly
     * yDown'ed.
     *
     * @return game screenshot
     */
    public Pixmap requestScreenshot(int x, int y, int width, int height) {
        Gdx.app.log("Screenshot", "Requested a new screenshot of size " + width + "x" + height);
        Pixmap screenshot = ScreenUtils.getFrameBufferPixmap(x, y, width, height);

        // Since the game runs using yDown, the screen has to be flipped.
        ByteBuffer data = screenshot.getPixels();
        byte[] flippedBuffer = new byte[4 * width * height];
        int bytesPerLine = 4 * width;
        for (int j = 0; j < height; j++) {
            data.position(bytesPerLine * (height - j - 1));
            data.get(flippedBuffer, j * bytesPerLine, bytesPerLine);
        }
        data.clear();
        data.put(flippedBuffer);
        data.clear();

        return screenshot;
    }

    public void shareScreenshot(Pixmap pixmap) {
        Gdx.app.debug("SharingServices", "Requested sharing a screenshot");
        shareScreenshotWithMessage(pixmap, "");
    }

    public void shareGameOverScreenshot(Pixmap pixmap) {
        Gdx.app.debug("SharingServices", "Requested sharing a screenshot");
        String message = context.getString(R.string.sharing_intent_text);
        message += " https://play.google.com/store/apps/details?id=es.danirod.rectball.android";
        shareScreenshotWithMessage(pixmap, message);
    }

    public void openInStore() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + AndroidLauncher.PACKAGE));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + AndroidLauncher.PACKAGE));
            context.startActivity(intent);
        }
    }

    private void shareScreenshotWithMessage(Pixmap pixmap, String text) {
        try {
            Uri screenshot = createScreenshotURI(pixmap);
            Intent sharingIntent = shareScreenshotURI(screenshot, text);
            String title = context.getString(R.string.sharing_intent_title);
            context.startActivity(Intent.createChooser(sharingIntent, title));
        } catch (Exception ex) {
            Gdx.app.error("SharingServices", "Couldn't share photo", ex);
        }
    }

    private Uri createScreenshotURI(Pixmap pixmap) {
        /*
            FIXME: THIS HACK MAKES GOD KILL KITTENS
            Should investigate on how to use the Android compatibility library.
            However, even the compatibility library seems to break compatibility
            since I cannot share anymore using SMS. Oh, well, how beautifully
            broken Android seems to be anyway.
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Use the fucking new Android permission system.
            File sharingPath = new File(context.getFilesDir(), "rectball-screenshots");
            File newScreenshot = new File(sharingPath, "screenshot.png");
            FileHandle screenshotHandle = Gdx.files.absolute(newScreenshot.getAbsolutePath());
            PixmapIO.writePNG(screenshotHandle, pixmap);
            return FileProvider.getUriForFile(context, context.getString(R.string.provider), newScreenshot);
        } else {
            // Use the fucking old Android permission system.
            FileHandle sharingPath = Gdx.files.external("rectball");
            sharingPath.mkdirs();
            FileHandle newScreenshot = Gdx.files.external("rectball/screenshot.png");
            PixmapIO.writePNG(newScreenshot, pixmap);
            return Uri.fromFile(newScreenshot.file());
        }
    }

    private Intent shareScreenshotURI(Uri uri, CharSequence message) {
        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, message);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return sharingIntent;
    }

    public AndroidLauncher getContext() {
        return context;
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
