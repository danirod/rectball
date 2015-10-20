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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
     * The skin instance used to style this actor.
     */
    private final Skin skin;
    /**
     * Whether to pad with zeroes or not.
     */
    private final boolean zeroPad;
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

    private float fontScale;

    public ScoreActor(Skin skin) {
        this.skin = skin;
        this.value = 0;
        this.zeroPad = true;

        background = skin.newDrawable("pixel", Color.BLACK);
        label = new Label(getScore(), skin, "monospace");
        label.setAlignment(Align.bottom);
        label.setFillParent(true);
        label.setFontScale(calculateFontScale());
        addActor(label);
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
        if (zeroPad) {
            while (score.length() < 4) {
                score = "0" + score;
            }
        }
        return score;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        label.setText(getScore());
        label.setFontScale(calculateFontScale());
    }

    @Override
    protected void sizeChanged() {
        label.setFontScale(calculateFontScale());
    }

    private float calculateFontScale() {
        GlyphLayout layout = new GlyphLayout(label.getStyle().font, label.getText());
        int requiredWidth = Math.round(layout.width);
        int requiredHeight = Math.round(layout.height);

        int maxHorizontalScale = Math.round((getWidth() - 10) / requiredWidth);
        int maxVerticalScale = Math.round((getHeight() - 10) / requiredHeight);
        return Math.min(maxHorizontalScale, maxVerticalScale) - 1;
    }
}
