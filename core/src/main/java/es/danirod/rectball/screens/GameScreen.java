/* This file is part of Rectball
 * Copyright (C) 2015-2024  Dani Rodríguez
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import es.danirod.rectball.Constants;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer.SoundCode;
import es.danirod.rectball.model.Ball;
import es.danirod.rectball.model.BallColor;
import es.danirod.rectball.model.Bounds;
import es.danirod.rectball.model.CombinationFinder;
import es.danirod.rectball.model.Coordinate;
import es.danirod.rectball.model.ScoreCalculator;
import es.danirod.rectball.scene2d.game.BallActor;
import es.danirod.rectball.scene2d.game.BoardActor;
import es.danirod.rectball.scene2d.game.Hud;
import es.danirod.rectball.scene2d.game.ScoreActor.ScoreListener;
import es.danirod.rectball.scene2d.game.TimerActor.TimerCallback;
import es.danirod.rectball.scene2d.listeners.BallSelectionListener;
import es.danirod.rectball.scene2d.ui.ConfirmDialog;
import es.danirod.rectball.settings.StatSerializer;

public class GameScreen extends AbstractScreen implements TimerCallback, BallSelectionListener, ScoreListener {

    /**
     * Display the board representation.
     */
    private BoardActor board;

    /**
     * The display used to represent information about the user.
     */
    private Hud hud;

    /**
     * Is the game paused?
     */
    private boolean paused;

    /**
     * Is the game running? True unless is on countdown or game over.
     */
    private boolean running;

    /**
     * True if the user is asking to leave.
     */
    private boolean askingLeave;

    public GameScreen(RectballGame game) {
        super(game);
    }

    /**
     * This method will add to the stage a confirmation dialog asking the user
     * whether to end the game or not. If the user answers YES to that dialog,
     * the game will end.
     */
    private void showLeaveDialog() {
        ConfirmDialog dialog = new ConfirmDialog(game.getAppSkin(),
                game.getLocale().get("game.leave_game"),
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
                resumeGame();
            }
        });

        dialog.show(stage);
        askingLeave = true;
    }

    private void showPauseDialog() {
        ConfirmDialog dialog = new ConfirmDialog(game.getAppSkin(),
            game.getLocale().get("game.paused"),
            game.getLocale().get("core.continue"),
            game.getLocale().get("core.exit"));
        dialog.setCallback(new ConfirmDialog.ConfirmCallback() {
            @Override
            public void ok() {
                // Continue
                game.player.playSound(SoundCode.SELECT);
                askingLeave = false;
                resumeGame();
            }

            @Override
            public void cancel() {
                // Leave
                game.player.playSound(SoundCode.SELECT);
                showLeaveDialog();
            }
        });

        dialog.show(stage);
        askingLeave = true;
    }

    @Override
    public void show() {
        super.show();
        game.updateWakelock(true);

        // Reset data
        if (!game.isRestoredState()) {
            game.getState().reset();
        } else {
            game.setRestoredState(false);
            if (game.getState().isTimeout()) {
                game.pushScreen(new GameOverScreen(game));
            }
        }

        // The player is playing
        game.getState().setPlaying(true);
        hud.getScore().setValue(game.getState().getScore());
        hud.getTimer().setSeconds(game.getState().getRemainingTime());

        paused = running = askingLeave = false;

        if (!game.getState().isCountdownFinished()) {
            countdown(2, new Runnable() {
                @Override
                public void run() {
                    game.getState().setCountdownFinished(true);

                    // Start the game unless the user is leaving or is paused.
                    if (!paused && !askingLeave) {
                        running = true;
                        board.setColoured(true);
                        hud.getTimer().setRunning(true);
                        board.setTouchable(Touchable.enabled);
                        game.player.playSound(SoundCode.SUCCESS);
                    }
                }
            });
        } else {
            pauseGame();
        }
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
        final Label label = new Label(number, game.getAppSkin(), "mono");
        label.setFontScale(1.5f);
        label.setSize(150, 150);
        label.setAlignment(Align.center);
        label.setPosition(
                (stage.getWidth() - label.getWidth()) / 2,
                (stage.getHeight() - label.getHeight()) / 2);

        // Add the label to the stage and play a sound to notify the user.
        stage.addActor(label);
        game.player.playSound(SoundCode.SELECT);

        label.addAction(Actions.sequence(
                Actions.parallel(Actions.moveBy(0, 80, 1f)),

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
     * @param bounds the bounds that have to be regenerated.
     */
    private void generate(Bounds bounds) {
        // Generate new balls;
        game.getState().getBoard().randomize(
                new Coordinate(bounds.minX, bounds.minY),
                new Coordinate(bounds.maxX, bounds.maxY));

        // Check the new board for valid combinations.
        CombinationFinder newFinder = CombinationFinder.create(game.getState().getBoard());
        if (newFinder.getPossibleBounds().size() == 1) {
            // Only one combination? This is trouble.
            Bounds newCombinationBounds = newFinder.getCombination();
            if (newCombinationBounds.equals(bounds)) {
                // Oh, oh, in the same spot! So, they must be of the same color.
                // Therefore, we need to randomize some balls to avoid enter
                // an infinite loop.
                hud.getTimer().setRunning(false);
                board.setColoured(false);
                game.getState().resetBoard();
                board.addAction(Actions.sequence(
                        board.shake(10, 5, 0.05f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                board.setColoured(true);
                                hud.getTimer().setRunning(true);
                            }
                        })));
            }
        }
        board.addAction(board.showRegion(bounds));
    }

    @Override
    public void setUpInterface(Table table) {
        board = new BoardActor(game.getBallAtlas(), game.getAppSkin(), game.getState().getBoard());

        hud = new Hud(game);

        hud.getHelp().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Don't act if the game hasn't started yet.
                if (!hud.getTimer().isRunning() || game.getState().isTimeout()) {
                    event.cancel();
                    return;
                }

                // Don't do anything if there are less than 5 seconds.
                if (hud.getTimer().getSeconds() <= 5 && game.getState().getWiggledBounds() == null) {
                    game.player.playSound(SoundCode.FAIL);
                    event.cancel();
                    return;
                }

                // Wiggle a valid combination.
                if (game.getState().getWiggledBounds() == null) {
                    CombinationFinder finder = CombinationFinder.create(game.getState().getBoard());
                    game.getState().setWiggledBounds(finder.getPossibleBounds().get(MathUtils.random(finder.getPossibleBounds().size() - 1)));
                }
                board.addAction(board.shake(game.getState().getWiggledBounds(), 10, 5, 0.1f));

                if (!game.getState().isCheatSeen()) {
                    // Subtract some time.
                    float subtractedTime = 5f;
                    final float step = subtractedTime / 10;
                    stage.addAction(Actions.repeat(10, Actions.delay(0.01f,
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    hud.getTimer().setSeconds(hud.getTimer().getSeconds() - step);
                                }
                            }))));
                    game.getState().setCheatSeen(true);
                }

                event.cancel();
            }
        });

        hud.getPause().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.player.playSound(SoundCode.FAIL);
                pauseGame();
                event.cancel();
            }
        });

        // Disable game until countdown ends.
        board.setTouchable(Touchable.disabled);

        // Add subscribers.
        hud.getTimer().addSubscriber(this);
        hud.getScore().setScoreListener(this);
        board.addSubscriber(this);

        table.add(hud).growX().align(Align.top).row();
        table.add(board).growX().expand().height(Value.percentWidth(1f)).align(Align.center).row();
        board.pack();
        table.pack();
    }

    @Override
    public void hide() {
        game.updateWakelock(false);

        // The player is not playing anymore.
        game.getState().setPlaying(false);

        // Just in case, remove any dialogs that might be forgotten.
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Dialog) {
                ((Dialog) actor).hide(null);
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // If the timer is running, keep incrementing the timer.
        if (hud.getTimer().isRunning()) {
            game.getState().addTime(delta);
            game.getState().setRemainingTime(hud.getTimer().getSeconds());
        }
    }

    @Override
    protected void escape() {
        if (!paused && !game.getState().isTimeout()) {
            pauseGame();
        }
    }

    /**
     * This method pauses the game. It is executed in one of the following
     * situations: first, the user presses PAUSE button. Second, the user
     * presses BACK button. Third, the user pauses the game (when the game
     * is restored the game is still paused).
     */
    private void pauseGame() {
        paused = true;
        game.updateWakelock(false);

        // Show the pause dialog unless you have already stop the game.
        if (!game.getState().isTimeout()) {
            showPauseDialog();
        }

        // If the game has started, pause it.
        if (running && !game.getState().isTimeout()) {
            board.setColoured(false);
            board.setTouchable(Touchable.disabled);
            hud.getTimer().setRunning(false);
        }
    }

    /**
     * This method resumes the game. It is executed in one of the following
     * situations: first, the user presses CONTINUE button on pause screen.
     * Second, the user presses BACK on the pause screen.
     */
    private void resumeGame() {
        paused = false;
        game.updateWakelock(true);

        // If the countdown has finished but the game is not running is a
        // condition that might be triggered in one of the following cases.
        // 1) The user has paused the game during the countdown. When the
        //    countdown finishes, because the game is paused, the game does
        //    not start.
        // 2) The user was leaving the game during the countdown. Same.
        if (!running && game.getState().isCountdownFinished()) {
            running = true;
            game.player.playSound(SoundCode.SUCCESS);
        }

        if (running && !game.getState().isTimeout()) {
            board.setColoured(true);
            board.setTouchable(Touchable.enabled);
            hud.getTimer().setRunning(true);
        }
    }

    @Override
    public void pause() {
        // Put the bounds
        game.getState().setBoardBounds(new Rectangle(board.getX(), board.getY(), board.getWidth(), board.getHeight()));

        // Show the pause dialog if it is not already visible.
        if (!paused) {
            pauseGame();
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void onTimeOut() {
        // Disable any further interactions.
        board.clearSelection();
        board.setColoured(true);
        board.setTouchable(Touchable.disabled);
        hud.getTimer().setRunning(false);
        game.player.playSound(SoundCode.GAME_OVER);
        game.getHaptics().vibrateMilliseconds(200);

        // Mark a combination that the user could do if he had enough time.
        if (game.getState().getWiggledBounds() == null) {
            CombinationFinder combo = CombinationFinder.create(game.getState().getBoard());
            game.getState().setWiggledBounds(combo.getCombination());
        } else {
            game.getState().incrementHints();
        }
        for (int y = 0; y < game.getState().getBoard().getSize(); y++) {
            for (int x = 0; x < game.getState().getBoard().getSize(); x++) {
                if (game.getState().getWiggledBounds() != null && !game.getState().getWiggledBounds().inBounds(x, y)) {
                    board.getBall(x, y).addAction(Actions.color(Color.DARK_GRAY, 0.15f));
                }
            }
        }

        // Update scores and statistics.
        int score = game.getState().getScore();
        int time = Math.round(game.getState().getElapsedTime());

        StatSerializer.Companion.combine(game.getState(), game.getStatistics());

        if (game.getContext().getGameServices().isSignedIn()) {
            game.getContext().getGameServices().uploadScore(score, time * 1000);
        }

        // Animate the transition to game over.
        board.addAction(Actions.delay(2f, board.hideBoard()));
        hud.addAction(Actions.delay(2f, Actions.fadeOut(0.25f)));

        // Head to the game over after all these animations have finished.
        stage.addAction(Actions.delay(2.5f, Actions.run(new Runnable() {
            @Override
            public void run() {
                stage.getRoot().clearActions();
                game.pushScreen(new GameOverScreen(game));
            }
        })));

        // Mark the game as finished.
        game.getState().setTimeout(true);
    }

    @Override
    public void onSelectionSucceeded(final List<BallActor> selection) {
        // Extract the data from the selection.
        List<Ball> balls = new ArrayList<>();
        for (BallActor selectedBall : selection)
            balls.add(selectedBall.getBall());
        final Bounds bounds = Bounds.fromBallList(balls);
        boolean usedCheat = game.getState().getWiggledBounds() != null;

        if (usedCheat)
            game.getState().incrementHints();

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
                        board.syncColors();
                        // Reset the cheat
                        game.getState().setCheatSeen(false);
                        game.getState().setWiggledBounds(null);
                    }
                })
        ));

        // Give some score to the user.
        ScoreCalculator calculator = new ScoreCalculator(game.getState().getBoard(), bounds);
        int givenScore = calculator.calculate();
        if (usedCheat) {
            givenScore *= 0.75f;
        }
        game.getState().addScore(givenScore);
        hud.getScore().giveScore(givenScore);

        // Put information about this combination in the stats.
        int rows = bounds.maxY - bounds.minY + 1;
        int cols = bounds.maxX - bounds.minX + 1;
        BallColor color = board.getBall(bounds.minX, bounds.minY).getBall().getColor();

        // When the user selects the entire board, it is a perfect.
        int boardSize = game.getState().getBoard().getSize() - 1;
        boolean isPerfect = bounds.equals(new Bounds(0, 0, boardSize, boardSize));

        game.getState().incrementCombinations(cols, rows, color, isPerfect);

        if (isPerfect) {
            // Just display PERFECT on the screen.
            Label label = new Label("PERFECT", game.getAppSkin(), "mono");
            label.setX((stage.getViewport().getWorldWidth() - label.getWidth()) / 2);
            label.setY((stage.getViewport().getWorldHeight() - label.getHeight()) / 2);
            label.setFontScale(1f);
            label.setAlignment(Align.center);
            label.addAction(Actions.sequence(
                            Actions.moveBy(0, 80, 0.5f),
                    Actions.removeActor()
            ));
            stage.addActor(label);
            game.player.playSound(SoundCode.PERFECT);

            // Give time
            float givenTime = Constants.SECONDS - hud.getTimer().getSeconds();
            hud.getTimer().giveTime(givenTime, 4f);
        } else {
            // Was special?
            boolean special = givenScore != rows * cols;
            // Give score
            Label scoreLabel = board.showPartialScore(givenScore, bounds, special, usedCheat);
            Vector2 pos = new Vector2(scoreLabel.getX(), scoreLabel.getY());
            board.localToStageCoordinates(pos);
            scoreLabel.setPosition(pos.x, pos.y);
            stage.addActor(scoreLabel);

            game.player.playSound(SoundCode.SUCCESS);
            game.getHaptics().vibrateMilliseconds(60);

            // Give time
            float givenTime = 4f + (givenScore) / 10f;
            hud.getTimer().giveTime(givenTime, 0.5f);
        }
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

    @Override
    public void onScoreGoNuts() {
        game.player.playSound(SoundCode.PERFECT);
    }
}
