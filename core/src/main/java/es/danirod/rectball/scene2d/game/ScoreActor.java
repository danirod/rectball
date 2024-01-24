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
package es.danirod.rectball.scene2d.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/**
 * This actor renders the score.
 */
public class ScoreActor extends Label {

    /**
     * The current value for this score.
     */
    private int value;

    /**
     * The remaining score that still has to be added to the value.
     */
    private int remainingValue;

    private boolean goNuts = false;

    private float nutTime = 0.0f;

    private ScoreListener listener = null;

    public ScoreActor(Skin skin) {
        super("", skin, "monospace2");
        setText(getScore());
        setAlignment(Align.center, Align.center);
        setFontScale(6f);
    }

    public void setScoreListener(ScoreListener listener) {
        this.listener = listener;
    }

    /**
     * This method converts the score to a string. Why should I have a method?
     * Because it depends on whether you want to pad the number with zeroes
     * or not.
     *
     * @return the score as a string.
     */
    private String getScore() {
        String score = Integer.toString(value);
        while (score.length() < 4) {
            score = "0" + score;
        }
        return score;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        setText(getScore());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (remainingValue > 0) {
            int givenValue = Math.min(remainingValue, 2);
            remainingValue -= givenValue;
            value += givenValue;
            setText(getScore());
        }
        if (goNuts) {
            if (nutTime < 1) {
                setText(Integer.toString(MathUtils.random(1000, 9999)));
                nutTime += delta;
            } else {
                setText(getScore());
                goNuts = false;
                nutTime = 0;
            }
        }
    }

    public void giveScore(int score) {
        if (value < 10000 && (value + score) >= 10000) {
            goNuts = true;
            if (listener != null) {
                listener.onScoreGoNuts();
            }
        }
        remainingValue += score;
    }

    public interface ScoreListener {

        void onScoreGoNuts();

    }
}
