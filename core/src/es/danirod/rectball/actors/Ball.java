/*
 * This file is part of Rectball.
 * Copyright (C) 2015 Dani Rodríguez
 * All rights reserved.
 */

package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

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
        // FIXME: Listener should be independent of this structure.

        this.ballColor = ballColor;
        this.sheet = sheet;

        // Set up look and feel.
        sprite = new Sprite(ballColor.getRegion(this.sheet));
        addListener(new BallInputListener(this, board));
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
        sprite.setRegion(ballColor.getRegion(sheet));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO: Implement a shader to make this look better.

        if (selected) {
            // If selected make it smaller to make it clear.
            // Thanks to madtriangle for the suggestion.
            sprite.setScale(0.8f);
            sprite.setOriginCenter();
            sprite.draw(batch, parentAlpha);
            sprite.setScale(1f);
        } else {
            sprite.draw(batch, parentAlpha);
        }
    }

    @Override
    protected void sizeChanged() {
        sprite.setSize(getWidth(), getHeight());
    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(), getY());
    }

    // TODO: Move this outside this class.
    private class BallInputListener extends InputListener {
        private final Board board;

        private final Ball ball;

        public BallInputListener(Ball ball, Board board) {
            this.ball = ball;
            this.board = board;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            selected = !selected;
            board.ballSelected(ball);
            return true;
        }
    }
}
