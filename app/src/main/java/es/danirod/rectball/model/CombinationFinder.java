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

public class CombinationFinder {

    private static CombinationFinder instance = null;

    private Board board;

    private int width, height;

    private final List<Bounds> bounds;

    private CombinationFinder() {
        bounds = new ArrayList<>();
    }

    public static CombinationFinder create(Board board) {
        if (instance == null)
            instance = new CombinationFinder();
        instance.board = board;
        instance.width = instance.height = board.getSize();
        instance.calculateCombinations();
        return instance;
    }

    private void calculateCombinations() {
        if (!bounds.isEmpty())
            bounds.clear();
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                BallColor refColor = board.getBall(x, y).getColor();

                // Iterate through all the balls in the same row
                for (int row = x + 1; row < width; row++) {
                    // Skip through this ball if it's not of the same color.
                    if (board.getBall(row, y).getColor() != refColor)
                        continue;
                    // Ok, it is. Let's see if we can find two balls in the
                    // same columns as our ref and the one we just found with
                    // the same color.
                    for (int col = y + 1; col < height; col++) {
                        if (board.getBall(x, col).getColor() == refColor
                            && board.getBall(row, col).getColor() == refColor)
                        {
                            int bx = Math.min(x, row);
                            int by = Math.min(y, col);
                            int BX = Math.max(x, row);
                            int BY = Math.max(y, col);
                            bounds.add(new Bounds(bx, by, BX, BY));
                        }
                    }
                }
            }
        }
    }

    public List<Bounds> getPossibleBounds() {
        return Collections.unmodifiableList(bounds);
    }

    public Bounds getCombination() {
        return bounds.isEmpty() ? null : bounds.get(0);
    }

    public Bounds getBestCombination() {
        if (bounds.isEmpty())
            return null;
        Bounds maxBounds = bounds.get(0);
        for (Bounds thisBounds : bounds)
            if (getWeightForCombination(thisBounds) > getWeightForCombination(maxBounds))
                maxBounds = thisBounds;
        return maxBounds;
    }

    public Bounds getWorstCombination() {
        if (bounds.isEmpty())
            return null;
        Bounds minBounds = bounds.get(0);
        for (Bounds thisBounds : bounds)
            if (getWeightForCombination(thisBounds) < getWeightForCombination(minBounds))
                minBounds = thisBounds;
        return minBounds;
    }

    public boolean areThereCombinations() {
        return !(bounds.isEmpty());
    }

    /**
     * Calculate the weight of the provided combination. Please note that the
     * weight is not the same as the score that the user receives from this
     * combination. Weight is a local comparing system used by the
     * CombinationFinder to decide what is the greatest combination can be
     * offered to the user.
     *
     * @since 0.4
     *
     * @param bounds  the bounds whose weight we want to know.
     * @return  the weight for this combination
     */
    private static int getWeightForCombination(Bounds bounds) {
        int cols = bounds.maxX - bounds.minX + 1;
        int rows = bounds.maxY - bounds.minY + 1;
        return cols * rows;
    }
}
