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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.Constants;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Ball;
import es.danirod.rectball.actors.Board;
import es.danirod.rectball.actors.ScoreActor;
import es.danirod.rectball.actors.Timer;
import es.danirod.rectball.actors.Timer.TimerCallback;
import es.danirod.rectball.dialogs.PauseDialog;
import es.danirod.rectball.listeners.BallInputListener;
import es.danirod.rectball.model.BallColor;
import es.danirod.rectball.model.Bounds;
import es.danirod.rectball.model.CombinationFinder;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;
import es.danirod.rectball.utils.StyleFactory;

import static es.danirod.rectball.Constants.VIEWPORT_WIDTH;

public class GameScreen extends AbstractScreen implements TimerCallback {

    public Board board;

    public Timer timer;

    private ScoreActor scoreLabel;

    private int scoreValue;

    private boolean paused, started;

    private PauseDialog pauseDialog;

    public GameScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void load() {
        // Set up the board.
        String file = game.settings.isColorblind() ? "colorblind" : "normal";
        Texture sheet = game.manager.get("board/" + file + ".png");
        board = new Board(this, sheet, 7, game.player);

        // Set up the listeners for the board.
        Ball[][] allBalls = board.getBoard();
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                Ball ball = allBalls[x][y];
                ball.addListener(new BallInputListener(ball, board));
            }
        }

        timer = new Timer(30, game.getSkin());
        timer.addSubscriber(this);

        scoreLabel = new ScoreActor(game.getSkin());

        // Create the pause dialog and their listeners.
        pauseDialog = new PauseDialog(game.getSkin());
        pauseDialog.addYesButtonCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // make dialog invisible
                PauseDialog dialog = (PauseDialog)actor.getParent();
                dialog.setVisible(false);
                timer.setRunning(false);
                onTimeOut();
            }
        });
        pauseDialog.addNoButtonCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // force uncheck
                TextButton button = (TextButton)actor;
                button.setChecked(false);
                setPaused(false);
            }
        });

        super.load();

        pauseDialog.setVisible(false);
        pauseDialog.setSize(Constants.VIEWPORT_WIDTH * 0.8f, 200);
        pauseDialog.setX(Constants.VIEWPORT_WIDTH / 2 - pauseDialog.getWidth() / 2);
        pauseDialog.setY(Constants.VIEWPORT_HEIGHT / 2 - pauseDialog.getHeight() / 2);
        getStage().addActor(pauseDialog);
    }

    @Override
    public int getID() {
        return Screens.GAME;
    }

    @Override
    public void show() {
        super.show();

        // Capture Back button so that the game doesn't minimize on Android.
        Gdx.input.setCatchBackKey(true);

        // Reset data
        scoreValue = 0;
        game.aliveTime = 0;
        paused = false;
        started = false;

        // Reset data
        board.setTouchable(Touchable.disabled);
        timer.setRunning(false);

        Ball[][] allBalls = board.getBoard();
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                allBalls[x][y].addAction(Actions.sequence(
                        Actions.scaleTo(0, 0),
                        Actions.delay(MathUtils.random(0.1f, 2.5f)),
                        Actions.scaleTo(1, 1, 0.5f)
                ));
            }
        }

        board.addAction(Actions.sequence(
                Actions.delay(3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        started = true;
                        board.randomize();
                        if (!paused) {
                            board.setTouchable(Touchable.enabled);
                            timer.setRunning(true);
                        }
                    }
                })
        ));
    }

    @Override
    public void setUpInterface(Table table) {
        table.add(timer).fillX().height(50).padTop(20).padBottom(10).row();
        table.add(scoreLabel).width(VIEWPORT_WIDTH / 2).height(65).padBottom(60).row();
        table.add(board).expand().fill().row();
    }

    @Override
    public void hide() {
        // Restore original back button functionality.
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (timer.isRunning()) {
            game.aliveTime += delta;
        }

        // Pause the game when you press BACK or ESCAPE.
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            setPaused(!paused);
        }
    }

    @Override
    public void onTimeOut() {
        // Update the scoreLabel... and the record.
        game.scores.addScore(scoreValue);

        timer.setRunning(false);
        board.setTouchable(Touchable.disabled);

        game.player.playSound(SoundCode.GAME_OVER);

        // Get a combination that the user didn't find.
        CombinationFinder finder = new CombinationFinder(board.getBoard());
        Bounds combination = finder.getCombination();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        Ball[][] allBalls = board.getBoard();
        BallColor refColor = allBalls[combination.minX][combination.minY].getBallColor();
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                final Ball currentBall = allBalls[x][y];

                if ((x >= combination.minX && x <= combination.maxX) &&
                        (y >= combination.minY && y <= combination.maxY)) {
                    currentBall.setBallColor(refColor);
                    continue;
                }

                float desplX = MathUtils.random(-width / 2, width / 2);
                float desplY = -height - MathUtils.random(0, height / 4);
                float scaling = MathUtils.random(0.3f, 0.7f);
                float desplTime = MathUtils.random(0.5f, 1.5f);
                currentBall.addAction(Actions.sequence(
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                currentBall.setBallColor(BallColor.GRAY);
                            }
                        }),
                        Actions.parallel(
                                Actions.moveBy(desplX, desplY, desplTime),
                                Actions.scaleBy(scaling, scaling, desplTime)
                        )));
            }
        }

        getStage().addAction(Actions.sequence(
                Actions.delay(2f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        game.setScreen(Screens.GAME_OVER);
                    }
                })
        ));
    }

    private String buildScore(int value, int digits) {
        String strValue = Integer.toString(value);
        while (strValue.length() < digits) {
            strValue = "0" + strValue;
        }
        return strValue;
    }

    public void score(int score, BallColor color, int rows, int cols) {
        // Store this information in the statistics.
        String size = Math.max(rows, cols) + "x" + Math.min(rows, cols);
        game.statistics.getTotalData().incrementValue("balls", rows * cols);
        game.statistics.getTotalData().incrementValue("scoreLabel", score);
        game.statistics.getTotalData().incrementValue("combinations");
        game.statistics.getColorData().incrementValue(color.toString().toLowerCase());
        game.statistics.getSizesData().incrementValue(size);

        // Increment the score.
        scoreValue += score;
        scoreLabel.setValue(scoreValue);

        BitmapFont font = game.manager.get("fonts/scores.fnt");
        LabelStyle style = new LabelStyle(font, Color.WHITE);
        final Label scoreLabel = new Label(Integer.toString(score), style);
        scoreLabel.setAlignment(Align.center);

        float ballSize = board.getWidth() / board.getSize();
        scoreLabel.setSize(ballSize, ballSize);
        scoreLabel.setX(Gdx.graphics.getWidth() / 2 - scoreLabel.getWidth() / 2);
        scoreLabel.setY(Gdx.graphics.getHeight() / 2 - scoreLabel.getHeight() / 2);
        scoreLabel.setFontScale(5);
        scoreLabel.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, 100, 1),
                        Actions.fadeOut(1)
                ),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        scoreLabel.remove();
                    }
                })
        ));
        getStage().addActor(scoreLabel);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        pauseDialog.setVisible(paused);
        if (started) {
            timer.setRunning(!paused);
            board.setMasked(paused);
            board.setTouchable(paused ? Touchable.disabled : Touchable.enabled);
        }
    }

    @Override
    public void pause() {
        setPaused(true);
    }
}
