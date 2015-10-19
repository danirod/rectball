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
import es.danirod.rectball.actors.ui.SwitchActor;
import es.danirod.rectball.listeners.ScreenJumper;
import es.danirod.rectball.listeners.ScreenPopper;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

public class SettingsScreen extends AbstractScreen {

    public SettingsScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        table.add(new Label(game.getLocale().get("main.settings"), game.getSkin(), "bold")).expandX().align(Align.top).height(100).row();

        Table settingsTable = new Table();
        settingsTable.setFillParent(true);
        settingsTable.defaults().align(Align.top);

        // Sound
        final SwitchActor sound = new SwitchActor(game.getLocale().get("settings.sound"), game.getSkin());
        sound.setChecked(game.getPlatform().preferences().getBoolean("sound", true));
        sound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.getPlatform().preferences().putBoolean("sound", sound.isChecked());
                game.getPlatform().preferences().flush();
                game.player.playSound(SoundCode.SELECT);
            }
        });
        settingsTable.add(sound).fillX().expandX().row();

        // Color
        final SwitchActor color = new SwitchActor(game.getLocale().get("settings.colorblind"), game.getSkin());
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
        settingsTable.add(color).fillX().expandX().row();

        // Do tutorial button.
        TextButton doTutorial = new TextButton(game.getLocale().get("settings.playTutorial"), game.getSkin());
        doTutorial.addListener(new ScreenJumper(game, Screens.TUTORIAL));
        settingsTable.add(doTutorial).padTop(50).height(60).fillX().expandX().row();

        // Settings pane.
        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        ScrollPane pane = new ScrollPane(settingsTable, style);
        table.add(pane).align(Align.top).expand().fill().row();

        // Back button
        TextButton backButton = new TextButton(game.getLocale().get("core.back"), game.getSkin());
        table.add(backButton).fillX().height(80).padTop(20).align(Align.bottom).row();
        backButton.addListener(new ScreenPopper(game));
    }

    @Override
    public int getID() {
        return Screens.SETTINGS;
    }
}
