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

/**
 * Information about a game. Usually the game we want information from is
 * the game the player is currently playing. This data structure can contain
 * information about score and time.
 *
 * @author danirod
 */
public class GameState {

    private final Board board;
    /**
     * The score. This data structure will provide methods to manually update
     * the score or to add points to the value, which is what the user will
     * usually do, actually.
     */
    private int score;
    /**
     * How many seconds has been the player playing. This is used to know how
     * many seconds has the player lasted when the game is over. Every tick
     * this value should be updated.
     */
    private float time;

    public GameState() {
        this.score = 0;
        this.time = 0;
        board = new Board(6);
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public float getTime() {
        return time;
    }

    public void addTime(float time) {
        this.time += time;
    }

    public Board getBoard() {
        return board;
    }

    public void reset() {
        time = 0;
        score = 0;
        resetBoard();
    }

    public void resetBoard() {
        boolean valid = false;
        while (!valid) {
            board.randomize();
            CombinationFinder finder = new CombinationFinder(board);
            valid = finder.areThereCombinations();
        }
    }
}
