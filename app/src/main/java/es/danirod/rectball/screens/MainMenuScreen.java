/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2024 Dani Rodríguez.
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
package es.danirod.rectball.screens;

import android.content.SharedPreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;
import es.danirod.rectball.android.BuildConfig;
import es.danirod.rectball.android.settings.SettingsManager;
import es.danirod.rectball.scene2d.game.BackgroundActor;
import es.danirod.rectball.scene2d.ui.ConfirmDialog;
import es.danirod.rectball.scene2d.ui.MainMenuGrid;
import es.danirod.rectball.scene2d.ui.MessageDialog;

public class MainMenuScreen extends AbstractScreen {

    private MainMenuGrid grid = null;

    private Label versionInformation = null;

    public MainMenuScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void dispose() {
        grid = null;
    }

    @Override
    public void setUpInterface(Table table) {
        if (grid == null) {
            grid = new MainMenuGrid(game);
        }
        if (versionInformation == null) {
            String text = BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE;
            if (Character.isDigit(text.charAt(0))) {
                text = "v" + text;
            }
            versionInformation = new Label(text, game.getAppSkin(), "small", "white");
            versionInformation.setFontScale(0.75f);
        }
        table.add(grid).pad(30f).expand().fillX().row();
        table.add(versionInformation).left().row();
    }

    private Stage backgroundLayer;
    private BackgroundActor backgroundActor;

    @Override
    public void show() {
        super.show();

        backgroundActor = new BackgroundActor(game.getBallAtlas());
        backgroundActor.setColor(1f, 1f, 1f, 0.15f);
        backgroundActor.setRotation(30f);
        backgroundActor.setOrigin(Align.center);
        backgroundLayer = new Stage(buildViewport());
        backgroundLayer.addActor(backgroundActor);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new MainMenuInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);

        // On first run, show the tutorial.
        if (!game.getContext().getSettings().getPreferences().getBoolean(SettingsManager.TAG_ASKED_TUTORIAL, false)) {
            askTutorial("main.ask_tutorial").show(stage);
        } else if (!game.getContext().getSettings().getPreferences().getBoolean(SettingsManager.TAG_NEW_SELECTION_MODE_NOTIFIED, false)) {
            askTutorial("main.ask_input_method").show(stage);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        backgroundLayer.getViewport().update(width, height);
        float larger = Math.max(width, height);
        backgroundActor.setSize(larger * 2f, larger * 2f);
        backgroundActor.setY(-larger / 2f);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit();
        }

        Gdx.gl.glClearColor(RectballGame.BG_COLOR.r, RectballGame.BG_COLOR.g, RectballGame.BG_COLOR.b, RectballGame.BG_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        backgroundLayer.act();
        backgroundLayer.draw();

        stage.act();
        stage.draw();
    }

    private ConfirmDialog askTutorial(String resId) {
        String message = game.getLocale().get(resId);
        String yes = game.getLocale().get("core.yes");
        String no = game.getLocale().get("core.no");
        ConfirmDialog dialog = new ConfirmDialog(game.getAppSkin(), message, yes, no);
        dialog.setCallback(new ConfirmDialog.ConfirmCallback() {
            @Override
            public void ok() {
                game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
                game.pushScreen(new TutorialScreen(game));
            }

            @Override
            public void cancel() {
                game.player.playSound(SoundPlayer.SoundCode.FAIL);
                SharedPreferences.Editor editor = game.getContext().getSettings().getPreferences().edit();
                editor.putBoolean(SettingsManager.TAG_ASKED_TUTORIAL, true);
                editor.putBoolean(SettingsManager.TAG_NEW_SELECTION_MODE_NOTIFIED, true);
                editor.apply();
                tutorialCancel().show(stage);
            }
        });
        return dialog;
    }

    private MessageDialog tutorialCancel() {
        String message = game.getLocale().get("main.dismiss_tutorial");
        MessageDialog dialog = new MessageDialog(game.getAppSkin(), message);
        dialog.setCallback(new MessageDialog.MessageCallback() {
            @Override
            public void dismiss() {
                game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
            }
        });
        return dialog;
    }

    private class MainMenuInputProcessor extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {
            return keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE;
        }

        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
                Gdx.app.exit();
                return true;
            }
            return false;
        }
    }
}
