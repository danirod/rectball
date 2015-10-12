package es.danirod.rectball.actors.board;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import es.danirod.rectball.model.*;
import es.danirod.rectball.screens.GameScreen;

import java.util.*;

public class BoardActor extends Table {

    private GameScreen screen;

    private TextureAtlas atlas;

    private final BallActor[][] actors;

    private Set<BallActor> selection = new HashSet<>();

    /**
     * Subscribers that will receive notifications about selection events.
     */
    private List<BallSelectionListener> subscribers = new ArrayList<>();

    /** The board. */
    private Board board;

    /** Is the board coloured? If false, all the balls will be grayed. */
    private boolean coloured = false;

    public BoardActor(GameScreen screen, TextureAtlas atlas, Board board) {
        this.screen = screen;
        this.atlas = atlas;
        this.board = board;
        this.actors = new BallActor[board.getSize()][board.getSize()];

        for (int y = board.getSize() - 1; y >= 0; y--) {
            for (int x = 0; x < board.getSize(); x++) {
                actors[x][y] = new BallActor(this, board.getBall(x, y), atlas);
                add(actors[x][y]).pad(2).uniform();
            }
            row();
        }
    }

    public BallActor getBall(int x, int y) {
        return actors[x][y];
    }

    public boolean isColoured() {
        return coloured;
    }

    public void setColoured(boolean coloured) {
        this.coloured = coloured;
    }

    /** Select a ball. */
    public void select(int x, int y) {
        BallActor selectedBall = actors[x][y];
        if (selection.add(selectedBall)) {
            // This ball has been selected. Check if we already have four.
            if (selection.size() == 4) {
                // Yes we have, check the selection.
                List<Ball> ballSelection = new ArrayList<>();
                for (BallActor ball : selection)
                    ballSelection.add(ball.getBall());
                if (board.selection(ballSelection)) {
                    // It is a valid selection. Notify our subscribers.
                    for (BallSelectionListener subscriber : subscribers)
                        subscriber.onSelectionSucceded(new ArrayList<>(selection));
                } else {
                    // It is not a valid selection. Notify our subscribers.
                    for (BallSelectionListener subscriber : subscribers)
                        subscriber.onSelectionFailed(new ArrayList<>(selection));
                }
                // Valid or not, we clear our current selection. We don't notify
                // our subscribers about the clear event. Subscribers should
                // assume on selection events that the selection is cleared.
                for (BallActor selected : selection) {
                    selected.quietlySetSelected(false);
                }
                selection.clear();
            } else {
                // No we don't, just select it.
                for (BallSelectionListener subscriber : subscribers)
                    subscriber.onBallSelected(selectedBall);
            }
        }
    }

    /** Unselect a ball. */
    public void unselect(int x, int y) {
        BallActor unselectedBall = actors[x][y];
        if (selection.remove(unselectedBall)) {
            // This ball has been unselected. Notify our subscribers about this.
            for (BallSelectionListener subscriber : subscribers)
                subscriber.onBallUnselected(unselectedBall);
        }
    }

    /** Clear the selection. */
    public void clearSelection() {
        List<BallActor> unselectedBalls = new ArrayList<>(selection);
        for (BallSelectionListener subscriber : subscribers)
            subscriber.onSelectionCleared(unselectedBalls);
        for (BallActor selected : selection) {
            selected.quietlySetSelected(false);
        }
        selection.clear();
    }

    /**
     * Attach a new subscriber to this board. The subscriber will receive
     * notifications about selection events happened in this board.
     * @param subscriber  susbcriber that wants to be notified
     */
    public void addSubscriber(BallSelectionListener subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Remove all the subscribers attached to this board. This method should be
     * called when the board is not being used anymore to clear any references
     * and to let the virtual machine do his job disposing the board.
     */
    public void removeSubscribers() {
        subscribers.clear();
    }

    /**
     * Resize some balls from this board.
     * @param bounds  bounding box for the balls that will be resized.
     * @param scale  the final scale for these balls.
     * @param time  the time that the animation will last for
     * @return  the action that will animate these balls
     */
    private Action resizeBalls(final Bounds bounds, final float scale, final float time) {
        Action scalingAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                for (int x = bounds.minX; x <= bounds.maxX; x++) {
                    for (int y = bounds.minY; y <= bounds.maxY; y++) {
                        BallActor scaledBall = actors[x][y];
                        scaledBall.addAction(Actions.scaleTo(scale, scale, time));
                    }
                }
            }
        });
        return Actions.sequence(scalingAction, Actions.delay(time));
    }

    public Action hideBoard() {
        Bounds boardBounds = new Bounds(0, 0, board.getSize() - 1, board.getSize() - 1);
        return resizeBalls(boardBounds, 0, 0.15f);
    }

    public Action showBoard() {
        Bounds boardBounds = new Bounds(0, 0, board.getSize() - 1, board.getSize() - 1);
        return resizeBalls(boardBounds, 1, 0.15f);
    }

    public Action hideRegion(Bounds bounds) {
        return resizeBalls(bounds, 0, 0.15f);
    }

    public Action showRegion(Bounds bounds) {
        return resizeBalls(bounds, 1, 0.15f);
    }
}
