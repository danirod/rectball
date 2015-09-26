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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Ball;
import es.danirod.rectball.actors.Board;
import es.danirod.rectball.listeners.DebugBallListener;
import es.danirod.rectball.screens.GameScreen;
import es.danirod.rectball.screens.Screens;
import es.danirod.rectball.utils.SoundPlayer;

public class CombinationDebugScreen extends GameScreen {

    public CombinationDebugScreen(RectballGame game) {
        super(game);
    }

    private Stage stage;

    private Board board;

    private TextButtonStyle style;

    private BitmapFont font;

    @Override
    public void show() {
        // Set up button style.
        Texture buttonTex = game.manager.get("ui/button.png", Texture.class);
        TextureRegion normal = new TextureRegion(buttonTex, 0, 0, 128, 128);
        TextureRegion hold = new TextureRegion(buttonTex, 128, 0, 128, 128);
        NinePatchDrawable normalPatch = game.style.buildPatch(normal, 32);
        NinePatchDrawable holdPatch = game.style.buildPatch(hold, 32);
        FileHandle normalFont = Gdx.files.internal("fonts/Play-Regular.ttf");
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(normalFont);
        FreeTypeFontParameter fpar = new FreeTypeFontParameter();
        fpar.size = 48;
        fpar.borderWidth = 2;
        fpar.shadowOffsetX = fpar.shadowOffsetY = 1;
        font = fontGenerator.generateFont(fpar);
        fontGenerator.dispose();
        style = new TextButtonStyle(normalPatch, holdPatch, holdPatch, font);

        // Set up stage.
        stage = new Stage(new FitViewport(480, 600));
        Gdx.input.setInputProcessor(stage);

        // Add a back button.
        TextButton backButton = new TextButton("Back", style);
        TextButton testButton = new TextButton("Test", style);
        backButton.setBounds(10, 480, 220, 100);
        testButton.setBounds(250, 480, 220, 100);
        backButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.player.playSound(SoundPlayer.SoundCode.UNSELECT);
                game.setScreen(Screens.DEBUG);
            }
        });
        testButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                test();
            }
        });
        stage.addActor(backButton);
        stage.addActor(testButton);

        // Set up the board.
        String file = game.settings.isColorblind() ? "colorblind" : "normal";
        Texture sheet = game.manager.get("board/" + file + ".png");
        board = new Board(this, sheet, 7, game.player);
        stage.addActor(board);

        // Set up the listeners for the board.
        Ball[][] allBalls = board.getBoard();
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                Ball ball = allBalls[x][y];
                ball.addListener(new DebugBallListener(ball));
            }
        }
        board.setTouchable(Touchable.enabled);
    }

    protected void test() {
        board.markCombination();
    }

    @Override
    public void hide() {
        font.dispose();
    }

    @Override
    public int getID() {
        return Screens.COMBINATION_TEST;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            board.randomize();
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);

        float boardSize = Math.min(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        board.setSize(boardSize * 0.9f, boardSize * 0.9f);
        board.setPosition(boardSize * 0.05f, boardSize * 0.05f);

        stage.getViewport().apply();
    }
}

