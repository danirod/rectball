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
package es.danirod.rectball.screens;

import android.content.SharedPreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;
import es.danirod.rectball.android.BuildConfig;
import es.danirod.rectball.android.R;
import es.danirod.rectball.android.settings.SettingsManager;
import es.danirod.rectball.scene2d.listeners.ScreenJumper;
import es.danirod.rectball.scene2d.ui.ConfirmDialog;
import es.danirod.rectball.scene2d.ui.MessageDialog;

public class MainMenuScreen extends AbstractScreen {

    private Image title = null;

    private ImageButton play = null, settings = null, statistics = null, about = null, quit = null;

    private ImageButton leaderboard = null, achievements = null, star = null;

    private Table extraButtons = null;

    public MainMenuScreen(RectballGame game) {
        super(game, false);
    }

    @Override
    public void dispose() {
        title = null;
        play = settings = statistics = about = quit = null;
        extraButtons = null;
    }

    @Override
    public void setUpInterface(Table table) {
        // Build the actors.
        if (title == null) {
            title = new Image(game.manager.get("logo.png", Texture.class));
            title.setScaling(Scaling.fit);
        }

        if (play == null) {
            play = new ImageButton(game.getSkin(), "greenPlay");
            play.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            game.getScreen(Screens.ABOUT).dispose();
                            game.getScreen(Screens.SETTINGS).dispose();
                            game.getScreen(Screens.STATISTICS).dispose();
                            game.player.playSound(SoundPlayer.SoundCode.SELECT);
                            game.pushScreen(Screens.GAME);
                            event.cancel();
                        }
                    });
        }
        if (settings == null) {
            settings = new ImageButton(game.getSkin(), "settings");
            settings.addListener(new ScreenJumper(game, Screens.SETTINGS));
        }
        if (statistics == null) {
            statistics = new ImageButton(game.getSkin(), "charts");
            statistics.addListener(new ScreenJumper(game, Screens.STATISTICS));
        }
        if (about == null) {
            about = new ImageButton(game.getSkin(), "info");
            about.addListener(new ScreenJumper(game, Screens.ABOUT));
        }
        if (star == null) {
            star = new ImageButton(game.getSkin(), "star");
            star.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            game.getContext().openInMarketplace();
                            event.cancel();
                        }
                    }
            );
        }
        if (BuildConfig.FLAVOR.equals("gpe")) {
            if (leaderboard == null) {
                leaderboard = new ImageButton(game.getSkin(), "leaderboard");
                leaderboard.addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                game.player.playSound(SoundPlayer.SoundCode.SELECT);
                                if (!game.getContext().getGameServices().isSignedIn()) {
                                    game.getContext().getGameServices().signIn();
                                } else {
                                    game.getContext().getGameServices().showLeaderboards();
                                }
                                event.cancel();
                            }
                        });

            }
            if (achievements == null) {
                achievements = new ImageButton(game.getSkin(), "achievements");
                achievements.addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                game.player.playSound(SoundPlayer.SoundCode.SELECT);
                                if (!game.getContext().getGameServices().isSignedIn()) {
                                    game.getContext().getGameServices().signIn();
                                } else {
                                    game.getContext().getGameServices().showAchievements();
                                }
                                event.cancel();
                            }
                        });
            }
        }
        if (quit == null) {
            quit = new ImageButton(game.getSkin(), "quit");
            quit.setSize(50, 50);
            quit.setPosition(getStage().getViewport().getWorldWidth() - 60, getStage().getViewport().getWorldHeight() - 60);
            quit.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.exit();
                }
            });
            if (game.getContext().getSettings().getPreferences().getBoolean(SettingsManager.TAG_ENABLE_FULLSCREEN, false)) {
                getStage().addActor(quit);
            }
        }

        if (extraButtons == null) {
            extraButtons = new Table();
            extraButtons.defaults().expandX().fillX().pad(20).height(100).uniformX();
            extraButtons.add(settings);
            extraButtons.add(statistics);
            if (BuildConfig.FLAVOR.equals("gpe")) {
                extraButtons.add(leaderboard);
            }
            extraButtons.row();
            extraButtons.add(star);
            extraButtons.add(about);
            if (BuildConfig.FLAVOR.equals("gpe")) {
                extraButtons.add(achievements);
            }
            extraButtons.row();
        }

        table.add(title).padLeft(40).padRight(40).align(Align.top).row();
        table.add(play).fillX().pad(70, 20, 20, 20).height(150).row();
        table.add(extraButtons).fillX().row();
    }

    @Override
    public void show() {
        super.show();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(getStage());
        multiplexer.addProcessor(new MainMenuInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);

        // On first run, show the tutorial.
        if (!game.getContext().getSettings().getPreferences().getBoolean(SettingsManager.TAG_ASKED_TUTORIAL, false)) {
            askTutorial(R.string.main_ask_tutorial).show(getStage());
        } else if (!game.getContext().getSettings().getPreferences().getBoolean(SettingsManager.TAG_NEW_SELECTION_MODE_NOTIFIED, false)) {
            askTutorial(R.string.main_ask_new_selection_method).show(getStage());
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit();
        }

        super.render(delta);
    }

    @Override
    public int getID() {
        return Screens.MAIN_MENU;
    }

    private ConfirmDialog askTutorial(int resId) {
        String message = game.getContext().getString(resId);
        String yes = game.getContext().getString(R.string.core_yes);
        String no = game.getContext().getString(R.string.core_no);
        ConfirmDialog dialog = new ConfirmDialog(game.getSkin(), message, yes, no);
        dialog.setCallback(new ConfirmDialog.ConfirmCallback() {
            @Override
            public void ok() {
                game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
                game.pushScreen(Screens.TUTORIAL);
            }

            @Override
            public void cancel() {
                game.player.playSound(SoundPlayer.SoundCode.FAIL);
                SharedPreferences.Editor editor = game.getContext().getSettings().getPreferences().edit();
                editor.putBoolean(SettingsManager.TAG_ASKED_TUTORIAL, true);
                editor.putBoolean(SettingsManager.TAG_NEW_SELECTION_MODE_NOTIFIED, true);
                editor.apply();
                tutorialCancel().show(getStage());
            }
        });
        return dialog;
    }

    private MessageDialog tutorialCancel() {
        String message = game.getContext().getString(R.string.main_dismiss_tutorial);
        MessageDialog dialog = new MessageDialog(game.getSkin(), message);
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
