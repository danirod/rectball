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
package es.danirod.rectball.model;

import java.util.*;

class Selection {

    private final List<Ball> balls;

    public Selection(List<Ball> balls) {
        this.balls = balls;
    }

    /**
     * Returns true if all the balls in the selection share the same color.
     *
     * @return true unless there is a single ball having a different color.
     */
    public boolean checkSameColor() {
        BallColor reference = balls.get(0).getColor();
        for (Ball ball : balls) {
            if (reference != ball.getColor()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check that the selection forms a square.
     *
     * @return true if the selection forms a square.
     */
    public boolean checkSquare() {
        // Put the coordinate values into two sets, one for rows and another
        // one for columns. To be a valid rectangle there should be two rows
        // (upper row and bottom row) and two columns (left and right col).
        Set<Integer> rows = new HashSet<>();
        Set<Integer> cols = new HashSet<>();
        for (Ball ball : balls) {
            rows.add(ball.getY());
            cols.add(ball.getX());
        }
        return rows.size() == 2 && cols.size() == 2;
    }
}
