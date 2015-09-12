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
package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

// TODO: Two different kind of values: values and fixed-length values.

/**
 * Values are used to represent a string of numbers in the screen using
 * Sprites. They are basically a custom implementation of something like
 * bitmap fonts without actually using bitmap fonts.
 *
 * @author danirod
 */
public class Value extends Actor {

    /**
     * How many digits can hold this value. If the value has more digits
     * than the limit, then the value is trimmed when represented on the
     * screen. If the value has less digits than the limit, it is padded
     * with zeroes.
     */
    private final int length;

    /**
     * The sheet texture holding that is used to build the regions and
     * sprites. This is provided as an argument and it is the font used
     * to render the value.
     */
    private final Texture sheet;

    /**
     * Numbers array. Each value from this array is a texture region
     * representing a digit. There should be 10 items in this array, every
     * item having the same number as the index (number 0 on index 0).
     */
    private final TextureRegion[] numbers;

    /**
     * The digit sprites that are rendered to the screen when this actor
     * is drawn. Sprites contain the bounds so that I don't have to care
     * about it. Just update the region when updating the value and move
     * the sprites when moving the actor.
     */
    private Sprite[] digits;

    /**
     * The value the number contains. This is the number that is used
     * to build and update the sprites that are rendered to screen later.
     */
    private long value;

    /**
     * Build a new value actor.
     *
     * @param sheet sheet image used to render the value.
     * @param length number of digits for fixed value length.
     * @param value initial value for this actor.
     */
    public Value(Texture sheet, int length, long value) {
        this.sheet = sheet;
        this.length = length;
        this.value = value;

        numbers = setUpDigits();
        digits = new Sprite[length];
        for (int i = 0; i < digits.length; i++) {
            digits[i] = new Sprite();
        }

        // Update the digits using the initial value.
        updateDigits();
    }

    /**
     * Split the numbers sheet into the texture regions for each digit.
     * The array that this method returns has 10 positions. Each position
     * holds the region for a digit. The digit is the index of the array,
     * so in index 0 is the region for digit 0.
     *
     * @return region array with the digits of this sheet.
     */
    private TextureRegion[] setUpDigits() {
        // Character size. Characters form the sheet are expected to be square
        // so the height and the width of the character has the same value.
        int size = sheet.getHeight();

        TextureRegion[] regions = new TextureRegion[10];
        for (int i = 0; i < 10; i++) {
            regions[i] = new TextureRegion(sheet, size * i, 0, size, size);
        }
        return regions;
    }

    /**
     * Get the value.
     * @return  value
     */
    public long getValue() {
        return value;
    }

    /**
     * Set the value.
     * @param value  value
     */
    public void setValue(long value) {
        this.value = value;
        updateDigits();
    }

    /**
     * Return the value as a string. This method doesn't just convert
     * the number into a String. If the value has less digits than
     * expected it pads the number with zeroes. If the value has more
     * digits than possible, it removes the most significant digits.
     *
     * @return  string representation of the value.
     */
    private String valueToString() {
        String scoreString = Long.toString(value);
        if (scoreString.length() > length) {
            // The value is overflowed. Remove the first digits.
            return scoreString.substring(scoreString.length() - length);
        } else {
            // Does the value has to be padded?
            while (scoreString.length() < length) {
                scoreString = "0" + scoreString;
            }
            return scoreString;
        }
    }

    /**
     * Update the digits so that the sprites reflect the value. This method
     * should be invoked after the value changes. The number displayed on
     * the screen won't change until this method is invoked.
     */
    private void updateDigits() {
        String scoreString = valueToString();
        for (int ch = 0; ch < scoreString.length(); ch++) {
            // Unsurprisingly Java supports character arithmetic. By
            // subtracting '0' to this digit character it is possible
            // to know the integer value for that character.
            int index = scoreString.charAt(ch) - '0';
            digits[ch].setRegion(numbers[index]);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < length; i++) {
            digits[i].setScale(getScaleX(), getScaleY());
            digits[i].setOriginCenter();
            digits[i].draw(batch, parentAlpha);
            digits[i].setScale(1, 1);
        }
    }

    /**
     * Update the bounds of the sprites representing the digits.
     * This method should be invoked after moving or resizing this actor
     * in order to update the size and position of the digits so that
     * they look where they are supposed to look.
     */
    private void setDigitsBounds() {
        // Calculate the maximum dimensions for each digit. If the actor has
        // a size of w * h, and there are N digits, the maximum size for
        // every single digit is (w / N) * h.
        float charMaxWidth = getWidth() / length;
        float charMaxHeight = getHeight();

        // Now calculate the scale. I want to make an integer scale so that
        // the pixels don't get distorted, so I calculate the maximum scale
        // that I can apply to the digits before the image goes out of the
        // bounds.
        int charSize = sheet.getHeight();
        int scale = (int) Math.min(charMaxWidth, charMaxHeight) / charSize;
        int spriteSize = scale * charSize;

        // We have the size for every digit, spriteSize * spriteSize.
        // Moving on to position, I want to center the digits both vertically
        // and horizontally. Calculate the offsets given total size.
        float baseX = getX() + (getWidth() - spriteSize * length) / 2;
        float baseY = getY() + (getHeight() - spriteSize) / 2;

        // And set the bounds.
        for (int i = 0; i < length; i++) {
            digits[i].setPosition(baseX + i * spriteSize, baseY);
            digits[i].setSize(spriteSize, spriteSize);
        }
    }

    @Override
    protected void sizeChanged() {
        setDigitsBounds();
    }

    @Override
    protected void positionChanged() {
        setDigitsBounds();
    }
}
