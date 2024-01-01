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

/**
 * Class whose purpose is calculate the score that will be given to the user.
 *
 * @since 0.4.0
 * @author danirod
 */
public class ScoreCalculator {

    private CombinationFinder finder;

    private Bounds bounds;

    private Board board;

    public ScoreCalculator(Board board, Bounds bounds) {
        this.bounds = bounds;
        this.board = board;
        this.finder = CombinationFinder.create(board);
    }

    public int calculate() {
        int rows = bounds.maxY - bounds.minY + 1;
        int cols = bounds.maxX - bounds.minX + 1;
        int score = rows * cols;

        // Is this the best combination of all the possible ones?
        if (finder.getBestCombination().equals(bounds)) {
            int possibleCombinations = finder.getPossibleBounds().size();
            switch (possibleCombinations) {
                case 1:
                    // For a single one, no bonus is given.
                    break;
                case 2:
                    score = Math.round((float) score * 1.1f);
                    break;
                case 3:
                    score = Math.round((float) score * 1.2f);
                    break;
                default:
                    score = Math.round((float) score * 1.3f);
                    break;
            }
        }

        // In fact, does this combination use the entire board?
        if (bounds.minX == 0 && bounds.minY == 0
                && bounds.maxX == board.getSize() - 1 && bounds.maxY == board.getSize() - 1) {
            // Yes, make that another bonus.
            score = Math.round((float) score * 1.5f);
        }

        return score;
    }

}
