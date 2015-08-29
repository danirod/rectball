/*
 * This file is part of Rectball.
 * Copyright (C) 2015 Dani Rodríguez
 * All rights reserved.
 */
package es.danirod.rectball.actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import es.danirod.rectball.model.BallColor;
import es.danirod.rectball.model.Bounds;
import es.danirod.rectball.model.Selection;
import es.danirod.rectball.screens.GameScreen;
import es.danirod.rectball.utils.SoundPlayer;

import java.util.*;

/**
 * The board is a group of balls. The board is created when the game starts
 * using some given size and it is where all the game play actually happens.
 * The board also owns the logic for handling rectangle events, such as
 * when a ball is pressed.
 *
 * @author danirod
 */
public class Board extends Group {

    /**
     * The game this board is attached to. This is used to access other
     * entities from the game such as the timer or the score board when
     * notifying about events.
     */
    private GameScreen screen;

    /**
     * The sheet that is used in the board to render the balls. The sheet
     * is provided as a parameter, which is a nice solution to handle
     * multiple themes or at least colorblind mode.
     */
    private Texture sheet;

    /**
     * The size of the board. The bigger the board is, the more balls there
     * are. Size of the board is related to the difficulty. More balls make
     * the game more difficult.
     */
    private final int size;

    /**
     * Matrix of balls. These are all the balls that are part of this board.
     * The user presses these balls and the balls react to events notifying
     * the board about that.
     */
    private Ball[][] board;

    private SoundPlayer player;

    /**
     * Set up a new board.
     *
     * @param screen  the screen this board belongs to.
     * @param size  the size of the board, always square.
     */
    public Board(GameScreen screen, Texture sheet, int size, SoundPlayer player) {
        this.screen = screen;
        this.sheet = sheet;
        this.size = size;

        this.player = player;

        board = setUpBoard();
        updateBoardBounds();
    }

    /**
     * Set up a new random matrix of balls using the given size. The matrix
     * that this method returns already contains a set of balls using random
     * colors. The balls don't have initial bounds. They have to be set.
     *
     * @return a new matrix with balls
     */
    private Ball[][] setUpBoard() {
        Ball[][] board = new Ball[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Ball ball = new Ball(BallColor.BLUE, this, sheet);
                board[x][y] = ball;
                addActor(ball);
            }
        }
        return board;
    }

    /**
     * Update the bounds of every ball form this ball using current width and
     * height, and position from the board itself. This method has to be
     * called after the bounds of the board change.
     */
    private void updateBoardBounds() {
        // Calculate the size of the balls so that they fit in the actor
        // without neither looking distorting, neither going out of bounds.
        float size = Math.min(getWidth() / this.size, getHeight() / this.size);

        // Now just calculate offset for vertical and horizontal centering.
        // Note that because this is a group, children actors use their own
        // coordinate system, so we don't add getX() or getY().
        float totalSize = size * this.size;
        float baseX = (getWidth() - totalSize) / 2;
        float baseY = (getHeight() - totalSize) / 2;

        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                board[x][y].setPosition(baseX + size * x, baseY + size * y);
                board[x][y].setSize(size, size);
            }
        }
    }

    public int getSize() {
        return size;
    }

    @Override
    protected void sizeChanged() {
        updateBoardBounds();
    }

    @Override
    protected void positionChanged() {
        updateBoardBounds();
    }

    private List<Ball> selection = new ArrayList<>();

    public void ballSelected(Ball me) {
        // The user unselected the ball.
        if (!me.isSelected()) {
            player.playUnselect();
            selection.remove(me);
            return;
        }

        // Otherwise, the user selected the ball.
        selection.add(me);
        if (selection.size() == 4) {
            // If there are four balls selected, test for valid selection.
            Selection sel = new Selection(this, selection);
            if (!sel.checkSameColor()) {
                player.playFail();
            } else if (!sel.checkSquare()) {
                player.playFail();
            } else {
                Bounds bounds = sel.getBounds();

                int cols = bounds.maxX - bounds.minX + 1;
                int rows = bounds.maxY - bounds.minY + 1;
                int score = cols * rows;

                screen.score.setValue(screen.score.getValue() + score);
                screen.timer.setSeconds(screen.timer.getSeconds() + 5);

                player.playSuccess();

                randomize(bounds.minX, bounds.minY, bounds.maxX, bounds.maxY);
            }

            for (Ball selectedBall : selection) {
                selectedBall.addAction(Actions.scaleTo(1f, 1f, 0.1f));
                selectedBall.setSelected(false);
            }
            selection.clear();
        } else {
            player.playSelect();
        }
    }

    /**
     * Return the board matrix.
     * @return  board matrix
     */
    public Ball[][] getBoard() {
        return board;
    }

    /**
     * Randomly swap the colors of the balls in the entire board. All the
     * balls will have their color changed to a new and random color.
     */
    public void randomize() {
        randomize(0, 0, board.length - 1, board.length - 1);
    }

    /**
     * Randomly swap the colors of the balls in a region inside the board.
     * The area to be swapped in enclosed in a rectangle whose corner points
     * are (x1,y1), (x2,y1), (x2,y2), (x1,y2).
     *
     * @param x1  left column
     * @param y1  top row
     * @param x2  right column
     * @param y2  bottom row
     */
    public void randomize(int x1, int y1, int x2, int y2) {
        BallColor[] allColors = BallColor.values();
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                int index = MathUtils.random(allColors.length - 1);
                board[x][y].setBallColor(allColors[index]);
            }
        }
    }

    public void setBoardColor(BallColor color) {
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                board[x][y].setBallColor(color);
    }
}
