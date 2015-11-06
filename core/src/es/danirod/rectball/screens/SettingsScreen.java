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

package es.danirod.rectball.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer.SoundCode;
import es.danirod.rectball.scene2d.listeners.ScreenJumper;
import es.danirod.rectball.scene2d.listeners.ScreenPopper;
import es.danirod.rectball.scene2d.ui.SwitchActor;

public class SettingsScreen extends AbstractScreen {



    public SettingsScreen(RectballGame game) {
        super(game);
    }

    private Table settingsTable = null;

    private SwitchActor sound = null, color = null;

    private TextButton doTutorial = null, backButton = null;

    private ScrollPane pane = null;

    @Override
    public void setUpInterface(Table table) {
        // Sound
        if (sound == null) {
            sound = new SwitchActor(game.getLocale().get("settings.sound"), game.getSkin());
            sound.setChecked(game.getPlatform().preferences().getBoolean("sound", true));
            sound.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    game.getPlatform().preferences().putBoolean("sound", sound.isChecked());
                    game.getPlatform().preferences().flush();
                    game.player.playSound(SoundCode.SELECT);
                }
            });
        }

        // Color
        if (color == null) {
            color = new SwitchActor(game.getLocale().get("settings.colorblind"), game.getSkin());
            color.setChecked(game.getPlatform().preferences().getBoolean("colorblind"));
            color.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    game.getPlatform().preferences().putBoolean("colorblind", color.isChecked());
                    game.getPlatform().preferences().flush();
                    game.updateBallAtlas();
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
            settingsTable.add(doTutorial).padTop(50).height(60).fillX().expandX().row();
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
