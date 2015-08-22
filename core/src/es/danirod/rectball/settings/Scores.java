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
