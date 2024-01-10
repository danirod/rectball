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

import com.badlogic.gdx.math.Rectangle;

import es.danirod.rectball.Constants;
import es.danirod.rectball.settings.LocalStatistics;

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
    private float elapsedTime;

    /**
     * This is the remaining time. When reaches 0.
     */
    private float remainingTime;

    public boolean isCountdownFinished() {
        return countdownFinished;
    }

    public void setCountdownFinished(boolean countdownFinished) {
        this.countdownFinished = countdownFinished;
    }

    /**
     * Whether or not the countdown has finished on the game. If this variable
     * is true, then the game has officially started. This is used when
     * restoring the game to decide whether to display the countdown again or
     * not.
     */
    private boolean countdownFinished;

    public boolean isCheatSeen() {
        return cheatSeen;
    }

    public void setCheatSeen(boolean cheatSeen) {
        this.cheatSeen = cheatSeen;
    }

    /**
     * These are the bounds selected when the user presses the Help button.
     * They are cached so that the same bounds are always used. Otherwise,
     * each time the user presses HELP without selecting a combination, a
     * different one would be used.
     */
    private Bounds wiggledBounds;

    public Bounds getWiggledBounds() {
        return wiggledBounds;
    }

    public void setWiggledBounds(Bounds wiggledBounds) {
        this.wiggledBounds = wiggledBounds;
    }

    /**
     * Whether the player has seen the next combination. When the user presses
     * the HELP button, a valid combination is displayed to help the player and
     * some time is subtracted. To prevent happening this more than once,
     * this variable will flag whether to subtract or not. It will be reset
     * every time a new combination is made.
     */
    private boolean cheatSeen;

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    private boolean playing;

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    private boolean timeout;

    private Rectangle boardBounds = new Rectangle();

    private LocalStatistics statistics;

    public GameState() {
        this.score = 0;
        this.elapsedTime = 0;
        this.remainingTime = Constants.SECONDS;
        board = new Board(6);

        statistics = new LocalStatistics();
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void addTime(float time) {
        this.elapsedTime += time;
    }

    public float getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(float remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Board getBoard() {
        return board;
    }

    public void reset() {
        elapsedTime = 0;
        remainingTime = Constants.SECONDS;
        score = 0;
        wiggledBounds = null;
        cheatSeen = false;
        playing = false;
        countdownFinished = false;
        timeout = false;
        resetBoard();
        statistics = new LocalStatistics();
    }

    public LocalStatistics getLocalStatistics() {
        return statistics;
    }

    public void incrementHints() {
        statistics = statistics.incrementHint();
    }

    public void incrementCombinations(long width, long height, BallColor color, boolean isPerfect) {
        statistics = statistics.incrementCombinations(width, height, color, isPerfect);
    }

    public void resetBoard() {
        boolean valid = false;
        while (!valid) {
            board.randomize();
            CombinationFinder finder = CombinationFinder.create(board);
            valid = finder.areThereCombinations();
        }
    }

    public void setBoardBounds(Rectangle boardBounds) {
        this.boardBounds.set(boardBounds);
    }

    public Rectangle getBoardBounds() {
        return boardBounds;
    }
}
