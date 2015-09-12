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
package es.danirod.rectball.screens.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.screens.MenuScreen;
import es.danirod.rectball.utils.SoundPlayer;

public class DebugScreen extends MenuScreen {

    public DebugScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        // Prepara la UI

        Label label = newLabel("Debug Menu");

        TextButton combinationDebug = newButton("Combination Test");
        combinationDebug.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
                game.setScreen(11);
            }
        });

        TextButton backButton = newButton("Back");
        backButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.player.playSound(SoundPlayer.SoundCode.FAIL);
                game.setScreen(3);
            }
        });

        table.add(label).fillX().row();
        table.add(combinationDebug).expandX().pad(30).row();
        table.add(backButton).expandX().pad(30).row();
    }

    @Override
    public int getID() {
        return 10;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }
}
