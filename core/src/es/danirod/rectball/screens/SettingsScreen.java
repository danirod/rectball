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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Switch;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

public class SettingsScreen extends MenuScreen {

    public SettingsScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        // Build stage entities.
        Label musicLabel = newLabel("Music");
        Label soundLabel = newLabel("Sound");
        Label colorLabel = newLabel("Colorblind");
        TextButton backButton = newButton("Back");

        Texture switchTex = game.manager.get("ui/switch.png");
        final Switch musicSwitch = new Switch(switchTex, game.settings.isMusicEnabled(), true);
        final Switch soundSwitch = new Switch(switchTex, game.settings.isSoundEnabled(), false);
        final Switch colorSwitch = new Switch(switchTex, game.settings.isColorblind(), false);

        table.add(musicLabel).pad(20).fillX().height(100);
        table.add(musicSwitch).pad(25).width(150).height(50).row();
        table.add(soundLabel).pad(20).fillX().height(100);
        table.add(soundSwitch).pad(25).width(150).height(50).row();
        table.add(colorLabel).pad(20).fillX().height(100);
        table.add(colorSwitch).pad(25).width(150).height(50).row();
        table.add(backButton).pad(20).fillX().height(100).colspan(2).row();

        backButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.settings.setMusicEnabled(musicSwitch.isEnabled());
                game.settings.setSoundEnabled(soundSwitch.isEnabled());
                game.settings.setColorblind(colorSwitch.isEnabled());

                /*
                 * FAIL sound is also the BACK sound. Play it after updating
                 * the settings because we want to use the new value for sound.
                 * Play it before saving the settings because we know that
                 * saving settings is slow on Android.
                 */
                game.player.playSound(SoundCode.FAIL);

                game.settings.save();
                game.setScreen(Screens.MAIN_MENU);
            }
        });
    }

    @Override
    public int getID() {
        return Screens.SETTINGS;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.7f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }
}
