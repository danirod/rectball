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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Ball;
import es.danirod.rectball.actors.Board;
import es.danirod.rectball.actors.Timer;
import es.danirod.rectball.actors.Value;
import es.danirod.rectball.listeners.BallInputListener;
import es.danirod.rectball.model.BallColor;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

public class GameScreen extends AbstractScreen {

    private Stage stage;

    public Board board;

    public Label score;

    public Timer timer;

    private Value countdown;

    private int valueScore;

    private boolean paused;

    public GameScreen(RectballGame game) {
        super(game);
    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void show() {
        // Capture Back button so that the game doesn't minimize on Android.
        Gdx.input.setCatchBackKey(true);

        // Reset score
        valueScore = 0;

        stage = new Stage(new ScreenViewport());
        
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
                ball.addListener(new BallInputListener(ball, board));
            }
        }

        // Set up the score
        Texture numbers = game.manager.get("scores.png");
        score = buildScoreLabel();
        score.setText(buildScore(valueScore, 6));
        stage.addActor(score);

        // Set up the timer
        Texture timerTexture = game.manager.get("timer.png");
        timer = new Timer(this, 10, timerTexture);
        timer.setRunning(false);
        stage.addActor(timer);

        board.setTouchable(Touchable.disabled);

        // Set up the countdown
        countdown = new Value(numbers, 1, 3);
        stage.addActor(countdown);
        countdown.addAction(Actions.sequence(
                Actions.scaleTo(0, 0, 1f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        countdown.setValue(2);
                    }
                }),
                Actions.scaleTo(1, 1),
                Actions.scaleTo(0, 0, 1f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        countdown.setValue(1);
                    }
                }),
                Actions.scaleTo(1, 1),
                Actions.scaleTo(0, 0, 1f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        timer.setRunning(true);
                        countdown.remove();
                        board.randomize();
                        board.setTouchable(Touchable.enabled);
                    }
                })
        ));
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                allBalls[x][y].addAction(Actions.sequence(
                        Actions.scaleTo(0, 0),
                        Actions.delay(MathUtils.random(0.1f, 2.5f)),
                        Actions.scaleTo(1, 1, 0.5f)
                ));
            }
        }
        resizeScene(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        // Restore original back button functionality.
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float delta) {
        if (game.settings.isColorblind()) {
            Gdx.gl.glClearColor(0.3f, 0.6f, 0.8f, 1f);
        } else {
            Gdx.gl.glClearColor(0.8f, 0.5f, 0.6f, 1f);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cheto
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            board.markCombination();
        }

        // Pause the game when you press BACK or ESCAPE.
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            setPaused(!paused);
        }

        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    public void gameOver() {
        // Update the score... and the record.
        long lastScore = Long.parseLong(score.getText().toString());
        game.scores.setLastScore(lastScore);
        if (lastScore > game.scores.getHighestScore()) {
            game.scores.setHighestScore(lastScore);
        }

        timer.setRunning(false);
        board.setTouchable(Touchable.disabled);

        game.player.playSound(SoundCode.GAME_OVER);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        Ball[][] allBalls = board.getBoard();
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                Ball currentBall = allBalls[x][y];
                float desplX = MathUtils.random(-width / 2, width / 2);
                float desplY = -height - MathUtils.random(0, height / 4);
                float scaling = MathUtils.random(0.3f, 0.7f);
                float desplTime = MathUtils.random(0.5f, 1.5f);
                currentBall.addAction(Actions.sequence(
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                board.setBoardColor(BallColor.GRAY);
                            }
                        }),
                        Actions.parallel(
                                Actions.moveBy(desplX, desplY, desplTime),
                                Actions.scaleBy(scaling, scaling, desplTime)
                        )));
            }
        }

        stage.addAction(Actions.sequence(
                Actions.delay(2f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        game.setScreen(2);
                    }
                })
        ));
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        resizeScene(width, height);
    }

    private void resizeScene(int width, int height) {
        if ((float) width / height >= 1.6f) {
            horizontalResize(width, height);
        } else {
            verticalResize(width, height);
        }

        int minSize = Math.min(width, height);
        countdown.setSize(0.5f * minSize, 0.5f * minSize);
        countdown.setX((width - countdown.getWidth()) / 2);
        countdown.setY((height - countdown.getHeight()) / 2);

        // Calculate the height that this has to be rendered at.
        int fontScale = 1;
        score.setFontScale(fontScale);
        while (score.getPrefWidth() <= score.getWidth() * 0.9
                && score.getPrefHeight() <= score.getHeight() * 0.9) {
            score.setFontScale(++fontScale);
        }
        score.setFontScale(Math.max(1, fontScale - 1));
    }

    private void horizontalResize(int width, int height) {
        // Put the board on the right.
        board.setSize(width * 0.5f, height * 0.9f);
        board.setPosition(width * 0.45f, height * 0.05f);

        // Put the score on the left.
        score.setSize(width * 0.35f, height / 8);
        score.setPosition(width * 0.05f, height * 0.6f);

        // Put the timer below the score.
        timer.setSize(width * 0.35f, height / 16);
        timer.setPosition(width * 0.05f, score.getY() - timer.getHeight());
    }

    private void verticalResize(int width, int height) {
        float scoreWidth = width * 0.9f;
        float scoreHeight = height / 8 * 0.9f;
        score.setSize(scoreWidth, scoreHeight);
        score.setPosition(width * 0.05f, height * 7 / 8);

        float timerWidth = width * 0.9f;
        float timerHeight = height / 16 * 0.9f;
        timer.setSize(timerWidth, timerHeight);
        timer.setPosition(width * 0.05f, score.getY() - timer.getHeight());

        float boardWidth = width * 0.9f;
        float boardHeight = timer.getY() * 0.9f;
        board.setSize(boardWidth, boardHeight);
        board.setPosition(width * 0.05f, height * 0.05f);
    }

    private Label buildScoreLabel() {
        BitmapFont font = game.manager.get("fonts/scores.fnt");
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        Label label = new Label("0", style);
        label.setAlignment(Align.center);
        return label;
    }

    private String buildScore(int value, int digits) {
        String strValue = Integer.toString(value);
        while (strValue.length() < digits) {
            strValue = "0" + strValue;
        }
        return strValue;
    }

    public void score(int score) {
        valueScore += score;
        this.score.setText(buildScore(valueScore, 6));
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        timer.setRunning(!paused);
        board.setMasked(paused);
        board.setTouchable(paused ? Touchable.disabled : Touchable.enabled);
    }

    @Override
    public void pause() {
        setPaused(true);
    }
}
