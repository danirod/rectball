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
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

public class SettingsScreen extends AbstractScreen {

    public SettingsScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        // Build stage entities.
        TextButton backButton = new TextButton(game.getLocale().get("core.back"), game.getSkin());
        final SwitchActor sound = new SwitchActor(game.getLocale().get("settings.sound"), game.getSkin());
        final SwitchActor color = new SwitchActor(game.getLocale().get("settings.colorblind"), game.getSkin());

        sound.setChecked(game.settings.isSoundEnabled());
        color.setChecked(game.settings.isColorblind());

        sound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.settings.setSoundEnabled(sound.isChecked());
                game.settings.save();
                game.player.playSound(SoundCode.SELECT);
            }
        });

        color.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.settings.setColorblind(color.isChecked());
                game.settings.save();
                game.updateBallAtlas();
                game.player.playSound(SoundCode.SELECT);
            }
        });

        table.add(new Label(game.getLocale().get("main.settings"), game.getSkin(), "bold")).expandX().align(Align.center).height(100).row();

        table.add(sound).fillX().row();
        table.add(color).fillX().row();

        table.add(backButton).fillX().expandY().height(80).padTop(20).align(Align.bottom).row();

        backButton.addCaptureListener(new ScreenJumper(game, Screens.MAIN_MENU));
    }

    @Override
    public int getID() {
        return Screens.SETTINGS;
    }
}
