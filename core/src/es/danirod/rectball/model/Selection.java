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
package es.danirod.rectball.model;

import es.danirod.rectball.actors.BallActor;
import es.danirod.rectball.actors.BoardActor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Selection {

    private BoardActor board;

    private List<BallActor> balls;

    public Selection(BoardActor board, List<BallActor> balls) {
        this.board = board;
        this.balls = balls;
    }

    /**
     * Returns true if all the balls in the selection share the same color.
     * @return true unless there is a single ball having a different color.
     */
    public boolean checkSameColor() {
        BallColor reference = balls.get(0).getBallColor();
        for (BallActor ball : balls) {
            if (reference != ball.getBallColor()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check that the selection forms a square.
     * @return  true if the selection forms a square.
     */
    public boolean checkSquare() {
        // Put the coordinate values into two sets, one for rows and another
        // one for columns. To be a valid rectangle there should be two rows
        // (upper row and bottom row) and two columns (left and right col).
        Set<Integer> rows = new HashSet<>();
        Set<Integer> cols = new HashSet<>();
        for (BallActor ball : balls) {
            Coordinate coordinate = getCoordinates(ball);
            rows.add(coordinate.y);
            cols.add(coordinate.x);
        }
        return rows.size() == 2 && cols.size() == 2;
    }

    public Bounds getBounds() {
        int minX, minY, maxX, maxY;
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;
        for (BallActor ball : balls) {
            Coordinate coordinate = getCoordinates(ball);
            minX = Math.min(minX, coordinate.x);
            minY = Math.min(minY, coordinate.y);
            maxX = Math.max(maxX, coordinate.x);
            maxY = Math.max(maxY, coordinate.y);
        }
        return new Bounds(minX, minY, maxX, maxY);
    }

    /**
     * Get the coordinates of the ball in the board. If the ball is not in the
     * board, this method will return null. The coordinates are checked using
     * the indices of the matrix.
     *
     * @param ball  ball to test
     * @return  the coordinates of the ball or null if they aren't.
     */
    private Coordinate getCoordinates(BallActor ball) {
        BallActor[][] matrix = board.getBoard();
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                if (matrix[x][y] == ball) {
                    return new Coordinate(x, y);
                }
            }
        }
        return null;
    }
}
