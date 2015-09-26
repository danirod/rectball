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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.utils.KonamiCodeListener;
import es.danirod.rectball.utils.KonamiCodeProcessor;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;
import es.danirod.rectball.utils.StyleFactory;

public class WelcomeScreen extends MenuScreen implements KonamiCodeListener {

    public WelcomeScreen(RectballGame game) {
        super(game);
    }

    private static final int[] DEBUG_KEYS = new int[] {
            Input.Keys.UP,
            Input.Keys.UP,
            Input.Keys.DOWN,
            Input.Keys.DOWN,
            Input.Keys.LEFT,
            Input.Keys.RIGHT,
            Input.Keys.LEFT,
            Input.Keys.RIGHT,
            Input.Keys.B,
            Input.Keys.A
    };

    private int debugStatus = 0;

    @Override
    public void show() {
        super.show();

        // Prepare Konami Code.
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new KonamiCodeProcessor(this));
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        // Build styles for title and version
        BitmapFont titleFont = boldGenerator.generateFont(StyleFactory.buildFontStyle(100, 4, 2));
        BitmapFont versionFont = fontGenerator.generateFont(StyleFactory.buildFontStyle(25, 0, 1));
        LabelStyle titleStyle = new LabelStyle(titleFont, Color.WHITE);
        LabelStyle versionStyle = new LabelStyle(versionFont, Color.WHITE);

        // Build the actors.
        Label title = new Label("Rectball", titleStyle);
        TextButton play = newButton("Play");
        TextButton settings = newButton("Settings");
        TextButton statistics = newButton("Stats");
        Label version = new Label(RectballGame.VERSION, versionStyle);

        // Position the actors in the screen.
        table.add(title).pad(20).padBottom(100).align(Align.center).row();
        table.add(play).pad(20).fillX().height(100).row();
        table.add(settings).pad(20).fillX().height(100).row();
        // table.add(statistics).pad(20).fillX().height(100).row();
        table.add(version).pad(20).align(Align.bottomRight).expandY().row();

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
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public int getID() {
        return Screens.MAIN_MENU;
    }

    @Override
    public void onKonamiCodePressed() {
        System.out.println("Código Konami Presionado.");
        game.setScreen(Screens.DEBUG);
    }
}
