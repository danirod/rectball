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
package es.danirod.rectball.settings;

import java.util.ArrayList;
import java.util.List;

public class Scores {

    private final List<Long> scores;

    private long lastScore;

    private long highestScore;

    private long totalScore;

    public Scores() {
        this.scores = new ArrayList<>();
        lastScore = highestScore = 0;
    }

    public Scores(List<Long> scores, long lastScore, long highestScore) {
        this.scores = scores;
        this.lastScore = lastScore;
        this.highestScore = highestScore;
    }

    public List<Long> getScores() {
        return scores;
    }

    public long getLastScore() {
        return lastScore;
    }

    public void setLastScore(long lastScore) {
        this.lastScore = lastScore;
    }

    public long getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(long highestScore) {
        this.highestScore = highestScore;
    }
}
