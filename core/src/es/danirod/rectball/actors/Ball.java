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

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import es.danirod.rectball.listeners.BallInputListener;
import es.danirod.rectball.model.BallColor;

/**
 * Actor for representing the state of balls that are in the board.
 * Balls can have a color and they can be clicked. The user interacts
 * with the balls in order to make rectangles during a game.
 *
 * @author danirod
 */
public class Ball extends Actor {

    /**
     * Whether the ball is selected or not. A ball is selected by touching
     * it. If touched again, it is unselected. The point of the game is
     * to select four balls. Selected balls should render differently.
     */
    private boolean selected = false;

    /**
     * Should the ball be hidden?
     */
    private boolean masked = false;

    /**
     * The current color of the ball. The value of this field might change
     * during the game as the user makes rectangles, since making a rectangle
     * shuffles the contents of the enclosed area.
     */
    private BallColor ballColor;

    /**
     * The texture sheet that is being used. This texture is used when the
     * ball color is shuffled again and is passed to the method that builds
     * the sprite.
     */
    private Texture sheet;

    /**
     * The actual sprite representation of the ball. For the sake of making
     * the code easy, the sprite has the position and size of the image that
     * is displayed on the screen.
     */
    private Sprite sprite;

    /**
     * Build a new ball. Keep in mind that this method should not be used to
     * change the color of a ball, since it's possible to change the color
     * without instantiating new balls.
     *
     * @param ballColor  initial color of the ball.
     * @param board  the board this ball belongs to.
     */
    public Ball(BallColor ballColor, Board board, Texture sheet) {
        this.ballColor = ballColor;
        this.sheet = sheet;

        // Set up look and feel.
        sprite = new Sprite(ballColor.getRegion(this.sheet));
    }

    /**
     * Get the selection status of this ball.
     * @return whether this ball is selected or not
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Change the selected status of this ball. Set this method to true to
     * make this ball selected. Otherwise set the method to false to make
     * this ball unselected.
     *
     * @param selected  whether the ball should be selected or not.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isMasked() {
        return masked;
    }

    public void setMasked(boolean masked) {
        this.masked = masked;
        if (masked) {
            sprite.setRegion(BallColor.GRAY.getRegion(sheet));
        } else {
            sprite.setRegion(ballColor.getRegion(sheet));
        }
    }

    /**
     * Get the color that this ball has at this very moment.
     * @return the current color of this ball.
     */
    public BallColor getBallColor() {
        return ballColor;
    }

    /**
     * Update the color of this ball. This method should be invoked whenever
     * the color of the ball needs to be replaced. For instance, after using
     * this ball in a selection.
     *
     * @param ballColor
     */
    public void setBallColor(BallColor ballColor) {
        this.ballColor = ballColor;

        // Changing the color also changes the sprite used to render the ball.
        if (masked) {
            sprite.setRegion(BallColor.GRAY.getRegion(sheet));
        } else {
            sprite.setRegion(ballColor.getRegion(sheet));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color col = batch.getColor();

        batch.setColor(col.r, col.g, col.b, col.a * parentAlpha);
        batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(col);
    }

    @Override
    protected void sizeChanged() {
        sprite.setSize(getWidth(), getHeight());
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(), getY());
    }
}

