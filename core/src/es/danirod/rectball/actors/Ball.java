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

import es.danirod.rectball.AssetLoader;

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
    private Texture texture;

    /**
     * The actual sprite representation of the ball. For the sake of making
     * the code easy, the sprite has the position and size of the image that
     * is displayed on the screen.
     */
    private Sprite sprite;

    /**
     * Whether colorblind mode is enabled or not. When colorblind mode is
     * enabled, ball is rendered using a special texture that displays a
     * shape inside the ball to make it easier to see for colorblind people.
     */
    private boolean colorblind = false;

    /**
     * The row and column position of this ball in the board. I don't feel
     * comfortable having this class knowing this information but at the
     * moment I don't know a better way for doing this.
     */
    private final int row, col;

    /**
     * Build a new ball. Keep in mind that this method should not be used to
     * change the color of a ball, since it's possible to change the color
     * without instantiating new balls.
     *
     * @param ballColor  initial color of the ball.
     * @param board  the board this ball belongs to.
     * @param row  the row for this ball in the board table.
     * @param col  the column for this ball in the board table.
     */
    public Ball(BallColor ballColor, Board board, int row, int col) {
        // FIXME: Texture should be a parameter via dependency injection.
        // FIXME: Listener should be independent of this structure.

        this.ballColor = ballColor;
        this.row = row;
        this.col = col;

        // Set up look and feel.
        texture = AssetLoader.get().get("board/normal.png");
        sprite = new Sprite(ballColor.getRegion(texture));
        addListener(new BallInputListener(this, board));
    }


    /**
     * Get the row where this ball is.
     * @return row of the ball.
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the column where this ball is.
     * @return column of the ball.
     */
    public int getCol() {
        return col;
    }

    /**
     * Update the colorblindness status for this ball. If colorblind mode is
     * enabled, the ball will be rendered using a special sheet that uses
     * shapes instead of colors to make the balls different. Otherwise the
     * standard ball sheet is used.
     *
     * @param colorblind  whether colorblind mode is enabled or not.
     */
    public void setColorblind(boolean colorblind) {
        /*
         * TODO: This method makes no sense in the final release.
         *
         * ¿Por qué me quiero cargar este método? Porque el ajuste para
         * daltónicos se establece fuera de la partida en las opciones de
         * juego, por lo que es global para toda la partida. El constructor
         * de la clase debería recibir la textura apropiada en función de si
         * el modo para daltónicos está activo o no y no se debería poder
         * cambiar en toda la partida salvo que la abandone.
         */

        this.colorblind = colorblind;
        if (colorblind) {
            texture = AssetLoader.get().get("balls/colorblind.png");
        } else {
            texture = AssetLoader.get().get("balls/normal.png");
        }
        sprite.setRegion(ballColor.getRegion(texture));
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
        sprite.setRegion(ballColor.getRegion(texture));
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
