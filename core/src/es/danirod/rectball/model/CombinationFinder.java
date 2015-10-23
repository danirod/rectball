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

package es.danirod.rectball.model;

import java.util.*;

public class CombinationFinder {

    private final Board board;

    private final int width, height;

    private final List<Bounds> bounds;

    public CombinationFinder(Board board) {
        this.board = board;
        width = height = board.getSize();
        bounds = calculateCombinations();
    }

    private List<Bounds> calculateCombinations() {
        List<Bounds> possibleBounds = new ArrayList<>();
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                Coordinate coordinate = new Coordinate(x, y);
                BallColor reference = board.getBall(x, y).getColor();
                List<Coordinate> inRow = sameColorInRow(coordinate);
                List<Coordinate> inCol = sameColorInCol(coordinate);

                for (Coordinate rowCandidate : inRow) {
                    for (Coordinate colCandidate : inCol) {
                        int cx = rowCandidate.x;
                        int cy = colCandidate.y;
                        if (board.getBall(cx, cy).getColor() == reference) {
                            int bx = Math.min(cx, x);
                            int by = Math.min(cy, y);
                            int BX = Math.max(cx, x);
                            int BY = Math.max(cy, y);
                            possibleBounds.add(new Bounds(bx, by, BX, BY));
                        }
                    }
                }
            }
        }
        return possibleBounds;
    }

    private List<Coordinate> sameColorInRow(Coordinate ref) {
        List<Coordinate> candidates = new ArrayList<>();
        BallColor refColor = board.getBall(ref.x, ref.y).getColor();
        for (int i = ref.x + 1; i < width; i++) {
            if (board.getBall(i, ref.y).getColor() == refColor) {
                candidates.add(new Coordinate(i, ref.y));
            }
        }
        return candidates;
    }

    private List<Coordinate> sameColorInCol(Coordinate ref) {
        List<Coordinate> candidates = new ArrayList<>();
        BallColor refColor = board.getBall(ref.x, ref.y).getColor();
        for (int j = ref.y + 1; j < height; j++) {
            if (board.getBall(ref.x, j).getColor() == refColor) {
                candidates.add(new Coordinate(ref.x, j));
            }
        }
        return candidates;
    }

    public List<Bounds> getPossibleBounds() {
        return Collections.unmodifiableList(bounds);
    }

    public Bounds getCombination() {
        return bounds.isEmpty() ? null : bounds.get(0);
    }

    public Bounds getBestCombination() {
        if (bounds.isEmpty()) {
            return null;
        }

        // Imagine this with lambdas and Java 8. Android PLEASE.
        Bounds maxBounds = bounds.get(0);
        for (Bounds thisBounds : bounds) {
            if (getWeightForCombination(thisBounds) > getWeightForCombination(maxBounds)) {
                maxBounds = thisBounds;
            }
        }
        return maxBounds;
    }

    public Bounds getWorstCombination() {
        if (bounds.isEmpty()) {
            return null;
        }

        Bounds minBounds = bounds.get(0);
        for (Bounds thisBounds : bounds) {
            if (getWeightForCombination(thisBounds) < getWeightForCombination(minBounds)) {
                minBounds = thisBounds;
            }
        }

        return minBounds;
    }

    public boolean areThereCombinations() {
        return !(bounds.isEmpty());
    }

    /**
     * Calculate the weight of the provided combination. Please note that the
     * weight is not the same as the score that the user receives from this
     * combination. Weight is a local comparation system used by the
     * CombinationFinder to decide what is the greatest combination can be
     * offered to the user.
     *
     * @since 0.4
     *
     * @param bounds  the bounds whose weight we want to know.
     * @return  the weight for this combination
     */
    private int getWeightForCombination(Bounds bounds) {
        /*
         * For now let's just use the number of balls in the combination. This
         * is an experimental formula that might be tweaked in future releases.
         */
        int cols = bounds.maxX - bounds.minX + 1;
        int rows = bounds.maxY - bounds.minY + 1;
        return cols * rows;
    }
}
