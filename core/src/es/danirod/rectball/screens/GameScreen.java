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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.Constants;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.board.*;
import es.danirod.rectball.actors.ScoreActor;
import es.danirod.rectball.actors.TimerActor;
import es.danirod.rectball.actors.TimerActor.TimerCallback;
import es.danirod.rectball.dialogs.ConfirmDialog;
import es.danirod.rectball.model.*;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

import java.util.ArrayList;
import java.util.List;

import static es.danirod.rectball.Constants.VIEWPORT_WIDTH;

public class GameScreen extends AbstractScreen implements TimerCallback, BallSelectionListener {

    /** Display the remaining time. */
    private TimerActor timer;

    /** Display the current score. */
    private ScoreActor score;

    /** Display the board representation. */
    private BoardActor board;

    /** Is the game paused? */
    private boolean paused;

    /** Is the game running? True unless is on countdown or game over. */
    private boolean running;

    /** Has the countdown already finished? */
    private boolean countdownFinished;

    /** True if the user is asking to leave. */
    private boolean askingLeave;

    /** True if the game has finished. */
    private boolean timeout;

    public GameScreen(RectballGame game) {
        super(game, false);
    }

    /**
     * This method will add to the stage a confirmation dialog asking the user
     * whether to end the game or not. If the user answers YES to that dialog,
     * the game will end.
     */
    private void showLeaveDialog() {
        ConfirmDialog dialog = new ConfirmDialog(game.getSkin(),
                game.getLocale().get("game.leave"),
                game.getLocale().get("core.yes"),
                game.getLocale().get("core.no"));
        dialog.setCallback(new ConfirmDialog.ConfirmCallback() {
            @Override
            public void ok() {
                // The user wants to leave the game.
                game.player.playSound(SoundCode.SUCCESS);
                askingLeave = false;
                onTimeOut();
            }

            @Override
            public void cancel() {
                // The user wants to resume the game.
                game.player.playSound(SoundCode.FAIL);
                askingLeave = false;
                resume();
            }
        });

        dialog.show(getStage());
        askingLeave = true;
    }

    private void showPreLeaveDialog() {
        ConfirmDialog dialog = new ConfirmDialog(game.getSkin(),
                game.getLocale().get("game.paused"),
                game.getLocale().get("game.continue"),
                game.getLocale().get("game.leaveGame"));
        dialog.setCallback(new ConfirmDialog.ConfirmCallback() {
            @Override
            public void ok() {
                // Continue
                game.player.playSound(SoundCode.SELECT);
                askingLeave = false;
                resume();
            }

            @Override
            public void cancel() {
                // Leave
                game.player.playSound(SoundCode.SELECT);
                showLeaveDialog();
            }
        });

        dialog.show(getStage());
        askingLeave = true;
    }

    @Override
    public int getID() {
        return Screens.GAME;
    }

    @Override
    public void show() {
        super.show();

        // Reset data
        game.getState().reset();
        paused = running = countdownFinished = askingLeave = timeout = false;
        countdown(2, new Runnable() {

            @Override
            public void run() {
                countdownFinished = true;

                // Start the game unless the user is leaving or is paused.
                if (!paused && !askingLeave) {
                    running = true;
                    board.setColoured(true);
                    timer.setRunning(true);
                    board.setTouchable(Touchable.enabled);
                    game.player.playSound(SoundCode.SUCCESS);
                }
            }
        });
    }

    /**
     * Create a countdown in the screen lasting for the amount of seconds given.
     * When the countdown reaches 0, the code provided in the runnable will
     * be executed as a callback.
     *
     * @param seconds how many seconds should the countdown be displayed.
     */
    private void countdown(final int seconds, final Runnable after) {
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
                Actions.parallel(Actions.fadeOut(1f), Actions.moveBy(0, 80, 1f)),

                // After the animation, decide. If the countdown hasn't finished
                // yet, run another countdown with 1 second less.
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        label.remove();
                        if (seconds > 1) {
                            countdown(seconds - 1, after);
                        } else {
                            after.run();
                        }
                    }
                })
        ));
    }

    /**
     * Generate new colors. This method is executed by the callback action
     * when the player selects a valid rectangle. The purpose of this method
     * is to regenerate the board and apply any required checks to make sure
     * that the game doesn't enter in an infinite loop.
     *
     * @param bounds  the bounds that have to be regenerated.
     */
    private void generate(Bounds bounds) {
        // Generate new balls;
        game.getState().getBoard().randomize(
                new Coordinate(bounds.minX, bounds.minY),
                new Coordinate(bounds.maxX, bounds.maxY));

        // Check the new board for valid combinations.
        CombinationFinder newFinder = new CombinationFinder(game.getState().getBoard());
        if (newFinder.getPossibleBounds().size() == 1) {
            // Only one combination? This is trouble.
            Bounds newCombinationBounds = newFinder.getCombination();
            if (newCombinationBounds.equals(bounds)) {
                // Oh, oh, in the same spot! So, they must be of the same color.
                // Therefore, we need to randomize some balls to avoid enter
                // an infinite loop.
                timer.setRunning(false);
                board.setColoured(false);
                game.getState().resetBoard();
                board.addAction(Actions.sequence(
                        board.shake(10, 5, 0.05f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                board.setColoured(true);
                                timer.setRunning(true);
                            }
                        })));
            }
        }
        board.addAction(board.showRegion(bounds));
    }

    private void showPartialScore(int score, Bounds bounds) {
        // Calculate the center of the region.
        BallActor bottomLeftBall = board.getBall(bounds.minX, bounds.minY);
        BallActor upperRightBall = board.getBall(bounds.maxX, bounds.maxY);

        float minX = bottomLeftBall.getX();
        float maxX = upperRightBall.getX() + upperRightBall.getWidth();
        float minY = bottomLeftBall.getY();
        float maxY = upperRightBall.getY() + upperRightBall.getHeight();
        float centerX = (minX + maxX) / 2;
        float centerY = (minY + maxY) / 2;

        Label label = new Label("+" + score, game.getSkin(), "monospace");
        label.setFontScale(10f);
        label.setSize(140, 70);
        label.setAlignment(Align.center);
        label.setPosition(centerX - label.getWidth() / 2, centerY - label.getHeight() / 2);
        label.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, 80, 0.5f),
                        Actions.alpha(0.5f, 0.5f)),
                Actions.removeActor()
        ));
        getStage().addActor(label);
    }

    @Override
    public void setUpInterface(Table table) {
        // Create the actors for this screen.
        timer = new TimerActor(Constants.SECONDS, game.getSkin());
        score = new ScoreActor(game.getSkin());
        board = new BoardActor(game.getBallAtlas(), game.getState().getBoard());

        // Disable game until countdown ends.
        timer.setRunning(false);
        board.setTouchable(Touchable.disabled);

        // Add subscribers.
        timer.addSubscriber(this);
        board.addSubscriber(this);

        // Fill the table.
        table.add(timer).fillX().height(50).padBottom(10).row();
        table.add(score).width(VIEWPORT_WIDTH / 2).height(65).padBottom(60).row();
        table.add(board).expand().row();
    }

    @Override
    public void hide() {
        // Just in case, remove any dialogs that might be forgotten.
        for (Actor actor : getStage().getActors()) {
            if (actor instanceof Dialog) {
                ((Dialog) actor).hide(null);
            }
        }
        // Restore original back button functionality.
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // If the timer is running, keep incrementing the timer.
        if (timer.isRunning()) {
            game.getState().addTime(delta);
        }

        // The user should be able to leave during the game.
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (!paused && !timeout) {
                pause();
            }
        }
    }

    @Override
    public void pause() {
        if (!askingLeave && !timeout) {
            showPreLeaveDialog();
        }

        paused = true;
        if (running && !timeout) {
            board.setColoured(false);
            board.setTouchable(Touchable.disabled);
            timer.setRunning(false);
        }
    }

    @Override
    public void resume() {
        if (askingLeave) {
            return;
        }
        
        paused = false;

        // If the countdown has finished but the game is not running is a
        // condition that might be triggered in one of the following cases.
        // 1) The user has paused the game during the countdown. When the
        //    countdown finishes, because the game is paused, the game does
        //    not start.
        // 2) The user was leaving the game during the countdown. Same.
        if (!running && countdownFinished) {
            running = true;
            game.player.playSound(SoundCode.SUCCESS);
        }

        if (running && !timeout) {
            board.setColoured(true);
            board.setTouchable(Touchable.enabled);
            timer.setRunning(true);
        }
    }

    @Override
    public void onTimeOut() {
        // Disable any further interactions.
        timeout = true;
        board.clearSelection();
        board.setColoured(true);
        board.setTouchable(Touchable.disabled);
        timer.setRunning(false);
        game.player.playSound(SoundCode.GAME_OVER);

        // Update the score... and the record.
        int score = game.getState().getScore();
        int time = Math.round(game.getState().getTime());
        game.getPlatform().score().registerScore(score, time);
        game.getPlatform().score().flushData();

        // Save information about this game in the statistics.
        game.statistics.getTotalData().incrementValue("score", game.getState().getScore());
        game.statistics.getTotalData().incrementValue("games");
        game.statistics.getTotalData().incrementValue("time", Math.round(game.getState().getTime()));
        game.getPlatform().statistics().saveStatistics(game.statistics);

        // Mark a combination that the user could do if he had enough time.
        CombinationFinder finder = new CombinationFinder(game.getState().getBoard());
        Bounds combination = finder.getCombination();
        for (int y = 0; y < game.getState().getBoard().getSize(); y++) {
            for (int x = 0; x < game.getState().getBoard().getSize(); x++) {
                if (combination != null && !combination.inBounds(x, y)) {
                    board.getBall(x, y).addAction(Actions.color(Color.DARK_GRAY, 0.15f));
                }
            }
        }

        // Animate the transition to game over.
        board.addAction(Actions.delay(2f, board.hideBoard()));
        this.score.addAction(Actions.delay(2f, Actions.fadeOut(0.25f)));

        // Head to the game over after all these animations have finished.
        getStage().addAction(Actions.delay(2.5f, Actions.run(new Runnable() {
            @Override
            public void run() {
                game.pushScreen(Screens.GAME_OVER);
            }
        })));
    }

    @Override
    public void onBallSelected(BallActor ball) {
        ball.addAction(Actions.scaleTo(0.8f, 0.8f, 0.15f));
        ball.addAction(Actions.color(Color.GRAY, 0.15f));
        game.player.playSound(SoundCode.SELECT);
    }

    @Override
    public void onBallUnselected(BallActor ball) {
        ball.addAction(Actions.scaleTo(1f, 1f, 0.15f));
        ball.addAction(Actions.color(Color.WHITE, 0.15f));
        game.player.playSound(SoundCode.UNSELECT);
    }

    @Override
    public void onSelectionSucceeded(final List<BallActor> selection) {
        // Extract the data from the selection.
        List<Ball> balls = new ArrayList<>();
        for (BallActor selectedBall : selection)
            balls.add(selectedBall.getBall());
        final Bounds bounds = Bounds.fromBallList(balls);

        // Change the colors of the selected region.
        board.addAction(Actions.sequence(
                board.hideRegion(bounds),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        for (BallActor selectedBall : selection) {
                            selectedBall.setColor(Color.WHITE);
                        }
                        generate(bounds);
                    }
                })
        ));

        // You deserve some score and extra time.
        int rows = bounds.maxY - bounds.minY + 1;
        int cols = bounds.maxX - bounds.minX + 1;
        game.getState().addScore(rows * cols);
        score.setValue(game.getState().getScore());
        timer.setSeconds(timer.getSeconds() + 4);

        // Put information about this combination in the stats.
        String size = Math.max(rows, cols) + "x" + Math.min(rows, cols);
        game.statistics.getSizesData().incrementValue(size);

        BallColor color = board.getBall(bounds.minX, bounds.minY).getBall().getColor();
        game.statistics.getColorData().incrementValue(color.toString().toLowerCase());

        game.statistics.getTotalData().incrementValue("balls", rows * cols);
        game.statistics.getTotalData().incrementValue("combinations");


        // Add some sound and animations.
        showPartialScore(rows * cols, bounds);
        game.player.playSound(SoundCode.SUCCESS);
    }

    @Override
    public void onSelectionFailed(List<BallActor> selection) {
        for (BallActor selected : selection) {
            selected.addAction(Actions.scaleTo(1f, 1f, 0.15f));
            selected.addAction(Actions.color(Color.WHITE, 0.15f));
        }
        game.player.playSound(SoundCode.FAIL);
    }

    @Override
    public void onSelectionCleared(List<BallActor> selection) {
        for (BallActor selected : selection) {
            selected.addAction(Actions.scaleTo(1f, 1f, 0.15f));
            selected.addAction(Actions.color(Color.WHITE, 0.15f));
        }
    }
}
