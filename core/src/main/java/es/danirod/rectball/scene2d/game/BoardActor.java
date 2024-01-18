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
package es.danirod.rectball.scene2d.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.danirod.rectball.model.Ball;
import es.danirod.rectball.model.Board;
import es.danirod.rectball.model.Bounds;
import es.danirod.rectball.scene2d.input.DragBoardSelectionListener;
import es.danirod.rectball.scene2d.listeners.BallSelectionListener;

public class BoardActor extends Table {

    private final BallActor[][] actors;

    private final Set<BallActor> selection = new HashSet<>();

    private BallSelectionListener selectionListener;

    private final Board board;

    private Skin skin;

    public BoardActor(TextureAtlas atlas, Skin skin, Board board) {
        this.board = board;
        this.skin = skin;
        this.actors = new BallActor[board.getSize()][board.getSize()];

        for (int y = board.getSize() - 1; y >= 0; y--) {
            for (int x = 0; x < board.getSize(); x++) {
                actors[x][y] = new BallActor(board.getBall(x, y), atlas);
                add(actors[x][y]).grow().space(10f);
            }
            row();
        }

        // Add an input handle to select items in the board.
        addListener(new DragBoardSelectionListener(this));
    }

    public void syncColors() {
        for (BallActor[] row : actors) {
            for (BallActor ball : row) {
                ball.syncColor();
            }
        }
    }

    public Label showPartialScore(int score, Bounds bounds, boolean special, boolean usedHelp) {
        // Get the graphical center of the region.
        BallActor bottomLeftBall = getBall(bounds.minX, bounds.minY);
        BallActor upperRightBall = getBall(bounds.maxX, bounds.maxY);
        float minX = bottomLeftBall.getX();
        float maxX = upperRightBall.getX() + upperRightBall.getWidth();
        float minY = bottomLeftBall.getY();
        float maxY = upperRightBall.getY() + upperRightBall.getHeight();
        float centerX = (minX + maxX) / 2;
        float centerY = (minY + maxY) / 2;

        // Build the label used to present the score.
        Label label = new Label("+" + score, skin, "mono");
        label.setFontScale(1.5f);
        label.setSize(140, 70);
        label.setAlignment(Align.center);
        label.setPosition(centerX - label.getWidth() / 2, centerY - label.getHeight() / 2);
        label.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, 80, 0.5f),
                        Actions.sequence(
                                Actions.delay(0.25f),
                                Actions.fadeOut(0.25f))
                        ),
                Actions.removeActor()
        ));
        if (usedHelp) {
            label.setColor(Color.RED);
        } else if (special) {
            label.setColor(Color.CYAN);
        }

        return label;
    }

    public BallActor getBall(int x, int y) {
        return actors[x][y];
    }

    public void setColoured(boolean coloured) {
        for (BallActor[] actor : actors) {
            for (BallActor ballActor : actor) {
                ballActor.setColoured(coloured);
            }
        }
    }

    public void select(Bounds bounds) {
        selection.clear();
        selection.addAll(Arrays.asList(
                actors[bounds.minX][bounds.minY],
                actors[bounds.minX][bounds.maxY],
                actors[bounds.maxX][bounds.minY],
                actors[bounds.maxX][bounds.maxY]
        ));
        if (selection.size() == 4) {
            finishSelection();
        } else {
            if (this.selectionListener != null)
                this.selectionListener.onSelectionFailed(new ArrayList<>(selection));
            selection.clear();
        }
    }

    private List<Ball> getModelBalls() {
        List<Ball> ballSelection = new ArrayList<>();
        for(BallActor ball : selection) {
            ballSelection.add(ball.getBall());
        }
        return ballSelection;
    }

    /**
     * This method is executed when the selection is complete. It asserts the selection is valid and notifies the game
     * so that it can trigger the proper animations depending on whether it has been successful or not.
     */
    private void finishSelection() {
        if (selection.size() != 4) throw new IllegalStateException("Select 4 balls first");

        /* Check whether the selection is valid and provide the valid message to our subscriber. */
        if (this.selectionListener != null) {
            List<BallActor> sel = new ArrayList<>(selection);
            if (board.selection(getModelBalls())) {
                this.selectionListener.onSelectionSucceeded(sel);
            } else {
                this.selectionListener.onSelectionFailed(sel);
            }
        }
        selection.clear();
    }

    /**
     * Clear the selection.
     */
    public void clearSelection() {
        List<BallActor> unselectedBalls = new ArrayList<>(selection);
        if (selectionListener != null) {
            selectionListener.onSelectionFailed(unselectedBalls);
        }
        selection.clear();
    }

    public void setSelectionListener(BallSelectionListener listener) {
        this.selectionListener = listener;
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
