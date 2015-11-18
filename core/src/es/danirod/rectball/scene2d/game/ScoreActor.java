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

package es.danirod.rectball.scene2d.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

/**
 * This actor renders the score.
 */
public class ScoreActor extends Group {

    /**
     * The background.
     */
    private final Drawable background;
    /**
     * The actual value that is rendered.
     */
    private final Label label;
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
        background = skin.newDrawable("pixel", Color.BLACK);
        label = new Label(getScore(), skin, "monospace2");
        label.setAlignment(Align.center);
        label.setFillParent(true);
        label.setFontScale(2.75f);
        label.setY(label.getY() - 5);
        addActor(label);
    }

    public void setScoreListener(ScoreListener listener) {
        this.listener = listener;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        // Draw the black background.
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);

        batch.setColor(color.r, color.g, color.b, color.a);
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
        label.setText(getScore());
    }

    @Override
    public void act(float delta) {
        if (remainingValue > 0) {
            int givenValue = Math.min(remainingValue, 2);
            remainingValue -= givenValue;
            value += givenValue;
            label.setText(getScore());
        }
        if (goNuts) {
            if (nutTime < 1) {
                label.setText(Integer.toString(MathUtils.random(1000, 9999)));
                nutTime += delta;
            } else {
                label.setText(getScore());
                goNuts = false;
                nutTime = 0;
            }
        }
    }

    public void giveScore(int score) {
        if (value < 10000 && (value + score) >= 10000) {
            goNuts = true;
            value += score;
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
