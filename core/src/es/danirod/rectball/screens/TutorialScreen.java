package es.danirod.rectball.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import es.danirod.rectball.Constants;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.ScoreActor;
import es.danirod.rectball.actors.TimerActor;
import es.danirod.rectball.actors.board.*;
import es.danirod.rectball.dialogs.MessageDialog;
import es.danirod.rectball.model.*;
import es.danirod.rectball.utils.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

import static es.danirod.rectball.Constants.VIEWPORT_WIDTH;

/**
 * This is the screen used to teach Rectball. This screen is mostly hardcoded
 * because the tutorial is always the same. The hardest part of this screen is
 * actually compositing the storyboard.
 *
 * @since 0.3.0
 */
public class TutorialScreen extends AbstractScreen implements BallSelectionListener{

    /** The board. */
    private BoardActor board;

    /** The score. */
    private ScoreActor score;

    /** The timer. */
    private TimerActor timer;

    /**
     * Every chapter in this tutorial is a state. The game passes from one
     * state to another and when finished, is back to the home screen.
     */
    private List<State> states;

    /** The current chapter. */
    private int currentState;

    private Timer.Task watchdogTask = new Timer.Task() {
        @Override
        public void run() {
            CombinationFinder finder = new CombinationFinder(game.getState().getBoard());
            for (Bounds bounds : finder.getPossibleBounds()) {
                board.addAction(board.shake(bounds, 10, 5, 0.1f));
            }
        }
    };

    public TutorialScreen(RectballGame game) {
        super(game, false);
    }

    @Override
    public void setUpInterface(Table table) {

        // Reset game state.
        game.getState().resetBoard();
        currentState = 0;

        // Fill the states information.
        states = new ArrayList<>();
        addStates();

        // Create the actors for this screen.
        timer = new TimerActor(1, game.getSkin());
        score = new ScoreActor(game.getSkin());
        board = new BoardActor(game.getBallAtlas(), game.getState().getBoard());

        // Set some subscribers.
        timer.addSubscriber(new TimerActor.TimerCallback() {
            @Override
            public void onTimeOut() {
                /*
                 * State for learning about the timer is 2. During this state,
                 * the timer is always restarting whenever it reaches zero to
                 * show how it empties.
                 */
                timer.setSeconds(1);
                timer.setRunning(currentState == 3);
            }
        });
        board.addSubscriber(this);

        // Set the initial state for the actors. Hidden and disabled.
        timer.setRunning(false);
        board.setTouchable(Touchable.disabled);
        board.setColoured(false);
        score.setVisible(false);
        board.setVisible(false);
        timer.setVisible(false);

        // Fill the table.
        table.add(timer).fillX().height(50).padBottom(10).row();
        table.add(score).width(VIEWPORT_WIDTH / 2).height(65).padBottom(60).row();
        table.add(board).expand().row();
    }

    @Override
    public void show() {
        super.show();
        nextState();
    }

    /** Start the next chapter. */
    private void nextState() {
        states.get(currentState++).start();
    }

    @Override
    public int getID() {
        return Screens.TUTORIAL;
    }

    /**
     * This is a state of the tutorial.
     */
    private class State {

        /**
         * The message dialog that should be displayed when the state starts.
         */
        private MessageDialog dialog;

        /**
         * The alignment of the message dialog. Can be top, bottom or center.
         * Usually will be center, but sometimes we need to push the dialog
         * top or down if we want to focus on something particular.
         */
        private int alignment;

        /**
         * Create a new state.
         * @param messageID  the state ID, used to retrieve the string.
         * @param alignment  Align.top, Align.center or Align.bottom.
         * @param button  the text for the button (Ok, Continue...)
         * @param dismiss  the runnable that is executed when the user dismisses.
         */
        public State(int messageID, int alignment, String button, final Runnable dismiss) {
            this.alignment = alignment;

            dialog = new MessageDialog(game.getSkin(), game.getLocale().get("tutorial.line" + messageID), game.getLocale().get(button));
            dialog.setCallback(new MessageDialog.MessageCallback() {
                @Override
                public void dismiss() {
                    if (dismiss != null) {
                        dismiss.run();
                    }
                }
            });
        }

        public void start() {
            dialog.show(getStage(), Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.1f)));
            float height;
            if ((alignment & Align.top) != 0) {
                height = Constants.VIEWPORT_HEIGHT - dialog.getHeight() - 20;
            } else if ((alignment & Align.bottom) != 0) {
                height = 20;
            } else {
                height = Math.round((getStage().getHeight() - dialog.getHeight()) / 2);
            }
            dialog.setPosition(Math.round((getStage().getWidth() - dialog.getWidth()) / 2), height);
        }
    }

    private void addStates() {
        // Welcome to Rectball!
        states.add(new State(1, Align.center, "core.continue", new Runnable() {
            @Override
            public void run() {
                nextState();
                getStage().addAction(Actions.delay(0.25f, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        board.setVisible(true);
                    }
                })));
            }
        }));

        // This is the board.
        states.add(new State(2, Align.top, "core.continue", new Runnable() {
            @Override
            public void run() {
                nextState();
                getStage().addAction(Actions.delay(0.25f, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        timer.setVisible(true);
                        timer.setRunning(true);
                    }
                })));
            }
        }));

        // This is the timer
        states.add(new State(3, Align.bottom, "core.continue", new Runnable() {
            @Override
            public void run() {
                nextState();
                states.get(3).start();

                // Make the score appear going nuts.
                getStage().addAction(Actions.delay(0.25f, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        score.setVisible(true);
                        score.addAction(Actions.forever(Actions.delay(0.1f, Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                int value = score.getValue() + MathUtils.random(10000);
                                score.setValue(value % 10000);
                            }
                        }))));
                    }
                })));
            }
        }));

        // This is the score.
        states.add(new State(4, Align.bottom, "core.continue", new Runnable() {
            @Override
            public void run() {
                // Reset the score.
                score.clearActions();
                score.setValue(0);

                // Put this colors in the board.
                char[][] trainingBoard = {
                        { 'Y', 'B', 'R', 'Y', 'B', 'Y' },
                        { 'R', 'G', 'Y', 'G', 'Y', 'R' },
                        { 'Y', 'Y', 'R', 'R', 'B', 'B' },
                        { 'R', 'G', 'R', 'R', 'B', 'Y' },
                        { 'Y', 'R', 'B', 'R', 'Y', 'R' },
                        { 'B', 'R', 'R', 'Y', 'R', 'B' }
                };

                // Set the board color to the array.
                for (int y = 0; y < trainingBoard.length; y++) {
                    for (int x = 0; x < trainingBoard.length; x++) {
                        BallColor color = null;
                        switch (trainingBoard[y][x]) {
                            case 'Y': color = BallColor.YELLOW; break;
                            case 'R': color = BallColor.RED; break;
                            case 'B': color = BallColor.BLUE; break;
                            case 'G': color = BallColor.GREEN; break;
                        }
                        board.getBall(x, trainingBoard.length - y - 1).getBall().setColor(color);
                    }
                }
                board.setColoured(true);

                nextState();
            }
        }));

        // This is the point of the game.
        states.add(new State(5, Align.center, "core.continue", new Runnable() {
            @Override
            public void run() {
                nextState();
            }
        }));

        // And this is what you have to do.
        states.add(new State(6, Align.center, "core.ok", new Runnable() {
            @Override
            public void run() {
                // Start the game.
                board.setTouchable(Touchable.enabled);
                Timer.schedule(watchdogTask, 8, 4, -2);
            }
        }));

        // Yeah, you beat the easy.
        states.add(new State(7, Align.center, "core.ok", new Runnable() {
            @Override
            public void run() {
                // Start the game again.
                board.addAction(board.showRegion(new Bounds(2, 2, 3, 3)));
                Timer.schedule(watchdogTask, 8, 4, -2);
            }
        }));

        // Now you beat a harder one.
        states.add(new State(8, Align.center, "core.ok", new Runnable() {
            @Override
            public void run() {
                board.addAction(board.showRegion(new Bounds(1, 2, 3, 4)));
                Timer.schedule(watchdogTask, 8, 4, -2);
            }
        }));

        // Ok, you got this.
        states.add(new State(9, Align.center, "core.ok", new Runnable() {
            @Override
            public void run() {
                board.addAction(board.showRegion(new Bounds(1, 3, 4, 5)));
                board.setColoured(false);
                board.setTouchable(Touchable.disabled);

                // Animate the transition to game over.
                board.addAction(Actions.delay(0.5f, board.hideBoard()));
                score.addAction(Actions.delay(0.5f, Actions.fadeOut(0.25f)));

                // Head to the game over after all these animations have finished.
                getStage().addAction(Actions.delay(1.5f, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.settings.setTutorialAsked(true);
                        game.settings.save();
                        game.popScreen();
                    }
                })));
            }
        }));
    }

    @Override
    public void onBallSelected(BallActor ball) {
        // Reset the watchdog timer.
        if (watchdogTask.isScheduled()) {
            watchdogTask.cancel();
            Timer.schedule(watchdogTask, 8, 4, -2);
        }

        ball.addAction(Actions.scaleTo(0.8f, 0.8f, 0.15f));
        ball.addAction(Actions.color(Color.GRAY, 0.15f));
        game.player.playSound(SoundPlayer.SoundCode.SELECT);
    }

    @Override
    public void onBallUnselected(BallActor ball) {
        // Reset the watchdog timer.
        if (watchdogTask.isScheduled()) {
            watchdogTask.cancel();
            Timer.schedule(watchdogTask, 8, 4, -2);
        }

        ball.addAction(Actions.scaleTo(1f, 1f, 0.15f));
        ball.addAction(Actions.color(Color.WHITE, 0.15f));
        game.player.playSound(SoundPlayer.SoundCode.UNSELECT);
    }

    @Override
    public void onSelectionSucceded(final List<BallActor> selection) {
        // Reset the watchdog timer.
        if (watchdogTask.isScheduled()) {
            watchdogTask.cancel();
        }

        // Extract the data from the selection.
        List<Ball> balls = new ArrayList<>();
        for (BallActor selectedBall : selection)
            balls.add(selectedBall.getBall());
        final Bounds bounds = Bounds.fromBallList(balls);
        int rows = bounds.maxY - bounds.minY + 1;
        int cols = bounds.maxX - bounds.minX + 1;
        score.setValue(score.getValue() + rows * cols);

        // Hide the selection.
        board.addAction(Actions.sequence(
                board.hideRegion(bounds),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        for (BallActor selectedBall : selection) {
                            selectedBall.setColor(Color.WHITE);
                        }

                        if (currentState == 6) {
                            game.getState().getBoard().getBall(2, 2).setColor(BallColor.YELLOW);
                            game.getState().getBoard().getBall(3, 2).setColor(BallColor.GREEN);
                            game.getState().getBoard().getBall(3, 3).setColor(BallColor.RED);
                            game.getState().getBoard().getBall(2, 3).setColor(BallColor.BLUE);
                            nextState();
                        } else if (currentState == 7) {
                            game.getState().getBoard().getBall(1, 2).setColor(BallColor.YELLOW);
                            game.getState().getBoard().getBall(2, 2).setColor(BallColor.GREEN);
                            game.getState().getBoard().getBall(3, 2).setColor(BallColor.RED);
                            game.getState().getBoard().getBall(1, 3).setColor(BallColor.BLUE);
                            game.getState().getBoard().getBall(2, 3).setColor(BallColor.GREEN);
                            game.getState().getBoard().getBall(3, 3).setColor(BallColor.GREEN);
                            game.getState().getBoard().getBall(1, 4).setColor(BallColor.BLUE);
                            game.getState().getBoard().getBall(2, 4).setColor(BallColor.YELLOW);
                            game.getState().getBoard().getBall(3, 4).setColor(BallColor.GREEN);
                            nextState();
                        } else if (currentState == 8) {
                            // Now just generate some random balls here.
                            game.getState().getBoard().randomize(
                                    new Coordinate(bounds.minX, bounds.minY),
                                    new Coordinate(bounds.maxX, bounds.maxY));
                            nextState();
                        }
                    }
                })));

        showPartialScore(rows * cols, bounds);
        game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
    }

    @Override
    public void onSelectionFailed(List<BallActor> selection) {
        // Reset the watchdog timer.
        if (watchdogTask.isScheduled()) {
            watchdogTask.cancel();
            Timer.schedule(watchdogTask, 8, 4, -2);
        }

        for (BallActor selected : selection) {
            selected.addAction(Actions.scaleTo(1f, 1f, 0.15f));
            selected.addAction(Actions.color(Color.WHITE, 0.15f));
        }
        game.player.playSound(SoundPlayer.SoundCode.FAIL);
    }

    @Override
    public void onSelectionCleared(List<BallActor> selection) {

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
}
