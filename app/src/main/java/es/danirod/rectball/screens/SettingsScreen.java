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
import android.widget.Toast;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.android.settings.SettingsManager;
import es.danirod.rectball.SoundPlayer.SoundCode;
import es.danirod.rectball.android.BuildConfig;
import es.danirod.rectball.scene2d.listeners.ScreenJumper;
import es.danirod.rectball.scene2d.listeners.ScreenPopper;
import es.danirod.rectball.scene2d.ui.SwitchActor;

public class SettingsScreen extends AbstractScreen {

    private TextButton googlePlayLogin;

    public SettingsScreen(RectballGame game) {
        super(game);
    }

    private Table settingsTable = null;

    private SwitchActor sound = null, color = null, fullscreen = null;

    private TextButton doTutorial = null, backButton = null;

    private ScrollPane pane = null;

    @Override
    public void setUpInterface(Table table) {
        // Sound
        if (sound == null) {
            sound = new SwitchActor(game.getLocale().get("settings.sound"), game.getSkin());
            sound.setChecked(game.getSettings().getPreferences().getBoolean(SettingsManager.TAG_ENABLE_SOUND, true));
            sound.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    SharedPreferences.Editor editor = game.getSettings().getPreferences().edit();
                    editor.putBoolean(SettingsManager.TAG_ENABLE_SOUND, sound.isChecked());
                    editor.apply();
                    game.player.playSound(SoundCode.SELECT);
                }
            });
        }

        // Color
        if (color == null) {
            color = new SwitchActor(game.getLocale().get("settings.colorblind"), game.getSkin());
            color.setChecked(game.getSettings().getPreferences().getBoolean(SettingsManager.TAG_ENABLE_COLORBLIND, false));
            color.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    SharedPreferences.Editor editor = game.getSettings().getPreferences().edit();
                    editor.putBoolean(SettingsManager.TAG_ENABLE_COLORBLIND, color.isChecked());
                    editor.apply();
                    game.updateBallAtlas();
                    game.player.playSound(SoundCode.SELECT);
                }
            });
        }

        // Fullscreen
        if (fullscreen == null) {
            fullscreen = new SwitchActor(game.getLocale().get("settings.fullscreen"), game.getSkin());
            fullscreen.setChecked(game.getSettings().getPreferences().getBoolean(SettingsManager.TAG_ENABLE_FULLSCREEN, false));
            fullscreen.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    SharedPreferences.Editor editor = game.getSettings().getPreferences().edit();
                    editor.putBoolean(SettingsManager.TAG_ENABLE_FULLSCREEN, fullscreen.isChecked());
                    editor.apply();
                    game.getContext().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(game.getContext(), game.getLocale().get("settings.fullscreenReset"), Toast.LENGTH_LONG).show();
                        }
                    });
                    game.player.playSound(SoundCode.SELECT);
                }
            });
        }

        // Do tutorial button.
        if (doTutorial == null) {
            doTutorial = new TextButton(game.getLocale().get("settings.playTutorial"), game.getSkin());
            doTutorial.addListener(new ScreenJumper(game, Screens.TUTORIAL));
        }

        if (settingsTable == null) {
            settingsTable = new Table();
            settingsTable.setFillParent(true);
            settingsTable.defaults().align(Align.top);
            settingsTable.add(sound).fillX().expandX().row();
            settingsTable.add(color).fillX().expandX().row();
            if (Gdx.app.getType() == Application.ApplicationType.Android) {
                settingsTable.add(fullscreen).fillX().expandX().row();
            }
            settingsTable.add(doTutorial).padTop(50).height(60).fillX().expandX().row();
        }

        // Log out button
        if (BuildConfig.FLAVOR.equals("gpe")) {
            String logoutText = game.getContext().getGameServices().isSignedIn() ?
                    game.getLocale().get("gplay.logout") :
                    game.getLocale().get("gplay.login");
            if (googlePlayLogin == null) {
                googlePlayLogin = new TextButton(logoutText, game.getSkin());
                googlePlayLogin.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        // TODO: This code will probably need to be made async due to how Google works.
                        if (game.getContext().getGameServices().isSignedIn()) {
                            // Proceed to sign out the user.
                            game.getContext().getAnalytics().sendEvent("UX", "Clicked", "Sign out from Google Play");
                            game.getContext().getGameServices().signOut();
                        } else {
                            // Send signed out event.
                            game.getContext().getAnalytics().sendEvent("UX", "Clicked", "Sign in to Google Play");
                            game.getContext().getGameServices().signIn();
                        }
                        game.player.playSound(SoundCode.SELECT);
                        googlePlayLogin.setText(game.getLocale().get("gplay.updating"));
                        googlePlayLogin.setDisabled(true);
                        Timer.schedule(new Timer.Task() {

                            @Override
                            public void run() {
                                String logoutText = game.getContext().getGameServices().isSignedIn() ?
                                        game.getLocale().get("gplay.logout") :
                                        game.getLocale().get("gplay.login");
                                googlePlayLogin.setText(logoutText);
                                googlePlayLogin.setDisabled(false);
                            }
                        }, 2f);
                        event.cancel();
                    }
                });
                settingsTable.add(googlePlayLogin).height(60).padTop(50).fillX().expandX().row();
            } else {
                googlePlayLogin.setText(logoutText);
            }
        }

        // Settings pane.
        if (pane == null) {
            ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
            pane = new ScrollPane(settingsTable, style);
        }

        // Back button
        if (backButton == null) {
            backButton = new TextButton(game.getLocale().get("core.back"), game.getSkin());
            backButton.addListener(new ScreenPopper(game));
        }

        table.add(pane).align(Align.top).expand().fill().row();
        table.add(backButton).fillX().height(80).padTop(20).align(Align.bottom).row();
    }

    @Override
    public void dispose() {
        settingsTable = null;
        sound = color = null;
        doTutorial = backButton = null;
        pane = null;
    }

    @Override
    public int getID() {
        return Screens.SETTINGS;
    }
}
