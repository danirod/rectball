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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

public class MainMenuScreen extends MenuScreen {

    public MainMenuScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void show() {
        super.show();

        // Build the actors.
        Image title = new Image(game.manager.get("logo.png", Texture.class));
        title.setScaling(Scaling.fit);
        TextButton play = newButton("Play");
        TextButton settings = newButton("Settings");
        TextButton statistics = newButton("Stats");

        // Position the actors in the screen.
        table.add(title).pad(40).row();
        table.add(play).pad(20).fillX().height(150).row();
        table.add(settings).pad(20).fillX().height(150).row();
        // table.add(statistics).pad(20).fillX().height(100).row();

        // Then add the capture listeners for the buttons.
        play.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.player.playSound(SoundCode.SUCCESS);
                game.setScreen(Screens.GAME);
            }
        });
        settings.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.player.playSound(SoundCode.SUCCESS);
                game.setScreen(Screens.SETTINGS);
            }
        });
        statistics.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.player.playSound(SoundCode.FAIL);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.6f, 0.6f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public int getID() {
        return Screens.MAIN_MENU;
    }
}
