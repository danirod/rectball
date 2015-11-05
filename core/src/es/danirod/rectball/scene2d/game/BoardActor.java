/*
 * This file is part of Rectball
 * Copyright (C) 2015 Dani Rodríguez
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

package es.danirod.rectball.scene2d.game;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import es.danirod.rectball.model.Ball;
import es.danirod.rectball.model.BallColor;
import es.danirod.rectball.model.Board;
import es.danirod.rectball.model.Bounds;
import es.danirod.rectball.scene2d.listeners.BallSelectionListener;

import java.util.*;

public class BoardActor extends Table {

    private final BallActor[][] actors;

    private final Set<BallActor> selection = new HashSet<>();

    /**
     * Subscribers that will receive notifications about selection events.
     */
    private final List<BallSelectionListener> subscribers = new ArrayList<>();

    /**
     * The board.
     */
    private final Board board;

    /**
     * Is the board coloured? If false, all the balls will be grayed.
     */
    private boolean coloured = false;

    public BoardActor(Map<BallColor, Drawable> assets, Board board) {
        this.board = board;
        this.actors = new BallActor[board.getSize()][board.getSize()];

        for (int y = board.getSize() - 1; y >= 0; y--) {
            for (int x = 0; x < board.getSize(); x++) {
                actors[x][y] = new BallActor(this, board.getBall(x, y), assets);
                add(actors[x][y]).pad(4, 2, 4, 2);
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

    /**
     * Select a ball.
     */
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
                        subscriber.onSelectionSucceeded(new ArrayList<>(selection));
                } else {
                    // It is not a valid selection. Notify our subscribers.
                    for (BallSelectionListener subscriber : subscribers)
                        subscriber.onSelectionFailed(new ArrayList<>(selection));
                }
                // Valid or not, we clear our current selection. We don't notify
                // our subscribers about the clear event. Subscribers should
                // assume on selection events that the selection is cleared.
                for (BallActor selected : selection) {
                    selected.quietlyUnselect();
                }
                selection.clear();
            } else {
                // No we don't, just select it.
                for (BallSelectionListener subscriber : subscribers)
                    subscriber.onBallSelected(selectedBall);
            }
        }
    }

    /**
     * Unselect a ball.
     */
    public void unselect(int x, int y) {
        BallActor unselectedBall = actors[x][y];
        if (selection.remove(unselectedBall)) {
            // This ball has been unselected. Notify our subscribers about this.
            for (BallSelectionListener subscriber : subscribers)
                subscriber.onBallUnselected(unselectedBall);
        }
    }

    /**
     * Clear the selection.
     */
    public void clearSelection() {
        List<BallActor> unselectedBalls = new ArrayList<>(selection);
        for (BallSelectionListener subscriber : subscribers)
            subscriber.onSelectionCleared(unselectedBalls);
        for (BallActor selected : selection) {
            selected.quietlyUnselect();
        }
        selection.clear();
    }

    /**
     * Attach a new subscriber to this board. The subscriber will receive
     * notifications about selection events happened in this board.
     *
     * @param subscriber subscriber that wants to be notified
     */
    public void addSubscriber(BallSelectionListener subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Resize some balls from this board.
     *
     * @param bounds bounding box for the balls that will be resized.
     * @param scale  the final scale for these balls.
     * @param time   the time that the animation will last for
     * @return the action that will animate these balls
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

    public Action hideRegion(Bounds bounds) {
        return resizeBalls(bounds, 0, 0.15f);
    }

    public Action showRegion(Bounds bounds) {
        return resizeBalls(bounds, 1, 0.15f);
    }

    public Action shake(float shakiness, int times, float speed) {
        return shake(new Bounds(0, 0, board.getSize() - 1, board.getSize() - 1), shakiness, times, speed);
    }

    public Action shake(final Bounds region, final float shakiness, final int times, final float speed) {
        Action shakingAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                for (int y = region.minY; y <= region.maxY; y++)
                    for (int x = region.minX; x <= region.maxX; x++)
                        actors[x][y].addAction(Actions.repeat(times, Actions.sequence(
                                                                                             Actions.moveBy(shakiness / 2, 0, speed / 2),
                                                                                             Actions.moveBy(-shakiness, 0, speed),
                                                                                             Actions.moveBy(shakiness / 2, 0, speed / 2)
                        )));
            }
        });
        return Actions.sequence(shakingAction, Actions.delay(times * speed));
    }
}
