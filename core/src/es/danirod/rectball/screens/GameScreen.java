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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.Constants;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.BallActor;
import es.danirod.rectball.actors.BoardActor;
import es.danirod.rectball.actors.ScoreActor;
import es.danirod.rectball.actors.TimerActor;
import es.danirod.rectball.actors.TimerActor.TimerCallback;
import es.danirod.rectball.dialogs.ConfirmDialog;
import es.danirod.rectball.model.Ball;
import es.danirod.rectball.model.Bounds;
import es.danirod.rectball.model.CombinationFinder;
import es.danirod.rectball.model.Coordinate;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

import java.util.ArrayList;
import java.util.List;

import static es.danirod.rectball.Constants.VIEWPORT_WIDTH;

public class GameScreen extends AbstractScreen implements TimerCallback {

    public TimerActor timer;

    private ScoreActor scoreLabel;

    private BoardActor board;

    private boolean paused, started;

    public GameScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void load() {
        // Set up the board.
        String file = game.settings.isColorblind() ? "colorblind" : "normal";

        timer = new TimerActor(Constants.SECONDS, game.getSkin());
        timer.addSubscriber(this);

        scoreLabel = new ScoreActor(game.getSkin());

        game.getCurrentGame().getBoard().randomize();
        board = new BoardActor(this, game.getSkin(), game.getCurrentGame().getBoard());

        super.load();
    }

    /**
     * This method will add to the stage a confirmation dialog asking the user
     * whether to end the game or not. If the user answers YES to that dialog,
     * the game will end.
     */
    private void showLeaveDialog() {
        ConfirmDialog dialog = new ConfirmDialog(game.getSkin(), "Leave game?", "Yes", "No");
        dialog.setCallback(new ConfirmDialog.ConfirmCallback() {
            @Override
            public void ok() {
                // The user wants to leave the game.
                timer.setRunning(false);
                onTimeOut();
            }

            @Override
            public void cancel() {
                setPaused(false);
            }
        });

        // FIXME: fadeIn action is not working because alpha handling in this
        // game is a mess at the moment. Fix that mess, then let the Dialog
        // use the default actions.
        dialog.show(getStage(), null);
        dialog.setPosition(
                Math.round((getStage().getWidth() - dialog.getWidth()) / 2),
                Math.round((getStage().getHeight() - dialog.getHeight()) / 2));
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
        game.getCurrentGame().reset();
        scoreLabel.setValue(0);
        paused = started = false;
        timer.setSeconds(Constants.SECONDS);
        timer.setRunning(false);

        board.setTouchable(Touchable.disabled);
        for (Actor child : board.getChildren()) {
            child.setColor(Color.WHITE);
        }

        // Display the countdown.
        countdown(2);

        // Wait 2 seconds, then colorize the board.
        getStage().addAction(Actions.sequence(
                Actions.delay(2f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (!paused) {
                            board.setColoured(true);
                            timer.setRunning(true);
                            board.setTouchable(Touchable.enabled);
                        }
                        started = true;
                    }
                })
        ));
    }

    /**
     * Create a countdown in the screen lasting for the amount of seconds given.
     * @param seconds how many seconds should the countdown be displayed.
     */
    private void countdown(final int seconds) {
        // Since this is a recursive function, avoid the case where you pass
        // a number of seconds that might trigger an infinite loop.
        if (seconds <= 0) {
            return;
        }

        // Create the label that will contain this number
        String number = Integer.toString(seconds);
        final Label label = new Label(number, game.getSkin(), "monospace");
        label.setFontScale(20f);
        label.setSize(150, 150);
        label.setAlignment(Align.center);
        label.setPosition(
                (getStage().getWidth() - label.getWidth()) / 2,
                (getStage().getHeight() - label.getHeight()) / 2);

        // Add the label to the stage and play a sound to notify the user.
        getStage().addActor(label);
        game.player.playSound(SoundCode.SELECT);

        label.addAction(Actions.sequence(
                // Animate it.
                Actions.parallel(
                        Actions.fadeOut(1f),
                        Actions.moveBy(0, 80, 1f)
                ),

                // After the animation, decide. If the countdown hasn't finished
                // yet, run another countdown with 1 second less.
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        label.remove();
                        if (seconds > 1) {
                            countdown(seconds - 1);
                        } else {
                            game.player.playSound(SoundCode.SUCCESS);
                        }
                    }
                })
        ));
    }

    @Override
    public void setUpInterface(Table table) {
        table.add(timer).fillX().height(50).padBottom(10).row();
        table.add(scoreLabel).width(VIEWPORT_WIDTH / 2).height(65).padBottom(60).row();
        table.add(board).expand().row();
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
            game.getCurrentGame().addTime(delta);
        }

        // Pause the game when you press BACK or ESCAPE.
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (!paused) {
                setPaused(true);
            }
        }
    }

    @Override
    public void onTimeOut() {
        board.unselectBalls();

        // Update the scoreLabel... and the record.
        game.scores.addScore(game.getCurrentGame().getScore());
        timer.setRunning(false);
        game.player.playSound(SoundCode.GAME_OVER);

        CombinationFinder finder = new CombinationFinder(game.getCurrentGame().getBoard());
        Bounds combination = finder.getCombination();
        for (int y = 0; y < game.getCurrentGame().getBoard().getSize(); y++) {
            for (int x = 0; x < game.getCurrentGame().getBoard().getSize(); x++) {
                if ((x >= combination.minX && x <= combination.maxX) &&
                        (y >= combination.minY && y <= combination.maxY)) {
                    continue;
                }
                board.getBall(x, y).addAction(Actions.color(Color.GRAY, 0.15f));
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

    public void onScore(List<BallActor> balls) {
        // Get the model
        List<Ball> ballModel = new ArrayList<>();
        for (BallActor ballActor : balls) {
            ballModel.add(ballActor.getBall());
        }

        // Get the bounds for these balls.
        final Bounds bounds = Bounds.fromBallList(ballModel);

        // Calculate the size of these bounds to calculate the score.
        int rows = bounds.maxY - bounds.minY + 1;
        int cols = bounds.maxX - bounds.minX + 1;
        int score = rows * cols;
        game.getCurrentGame().addScore(score);
        scoreLabel.setValue(game.getCurrentGame().getScore());

        // Give some extra time.
        timer.setSeconds(timer.getSeconds() + 5);

        getStage().addAction(Actions.sequence(
                hideBalls(bounds),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        boolean valid = false;
                        int tries = 0;
                        while (!valid && tries++ < 3) {
                            game.getCurrentGame().getBoard().randomize(
                                    new Coordinate(bounds.minX, bounds.minY),
                                    new Coordinate(bounds.maxX, bounds.maxY));
                            CombinationFinder finder = new CombinationFinder(game.getCurrentGame().getBoard());
                            valid = finder.areThereCombinations();
                        }

                        if (tries == 3) {
                            getStage().addAction(Actions.sequence(
                                    hideBoard(),
                                    Actions.run(new Runnable() {
                                        @Override
                                        public void run() {
                                            game.getCurrentGame().resetBoard();
                                        }
                                    }),
                                    showBoard()
                            ));
                        }
                    }
                }),
                showBalls(bounds)
        ));


    }

    public void setPaused(boolean paused) {
        this.paused = paused;

        // If the game has just been paused, show the pause dialog.
        if (paused) {
            showLeaveDialog();
        }

        // If the game has already started, pause the timer and hide the board.
        if (started) {
            timer.setRunning(!paused);
            board.setColoured(!paused);
            board.setTouchable(paused ? Touchable.disabled : Touchable.enabled);
        }
    }

    @Override
    public void pause() {
        setPaused(true);
    }

    private Action hideBalls(final Bounds bounds) {
        return Actions.sequence(
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        for (int x = bounds.minX; x <= bounds.maxX; x++) {
                            for (int y = bounds.minY; y <= bounds.maxY; y++) {
                                board.getBall(x, y).addAction(Actions.scaleTo(0, 0, 0.15f));
                            }
                        }
                    }
                }),
                Actions.delay(0.15f)
        );
    }

    private Action showBalls(final Bounds bounds) {
        return Actions.sequence(
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        for (int x = bounds.minX; x <= bounds.maxX; x++) {
                            for (int y = bounds.minY; y <= bounds.maxY; y++) {
                                board.getBall(x, y).addAction(Actions.scaleTo(1, 1, 0.15f));
                            }
                        }
                    }
                }),
                Actions.delay(0.15f)
        );
    }

    private Action hideBoard() {
        return Actions.sequence(
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        for (Actor child : board.getChildren()) {
                            child.addAction(Actions.scaleTo(0, 0, 0.15f));
                        }
                    }
                }),
                Actions.delay(0.15f)
        );
    }

    private Action showBoard() {
        return Actions.sequence(
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        for (Actor child : board.getChildren()) {
                            child.addAction(Actions.scaleTo(1, 1, 0.15f));
                        }
                    }
                }),
                Actions.delay(0.15f)
        );
    }

}
