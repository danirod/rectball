package es.danirod.rectball.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.danirod.rectball.AssetLoader;

/**
 * Ball data structure.
 *
 * @author danirod
 */
public class Ball extends Actor {

    /** Whether this ball is selected or not. */
    private boolean selected;

    /** The ball ballColor. */
    private BallColor ballColor;

    /** Selected texture. */
    private Texture selectedTexture;

    private Match match;

    private int row, col;

    public Ball(BallColor ballColor, final Match match, int row, int col) {
        this.ballColor = ballColor;
        this.match = match;
        this.selected = false;
        this.row = row;
        this.col = col;
        addListener(new BallInputListener(this, match));
        selectedTexture = AssetLoader.get().get("balls/basic/selected.png");
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public BallColor getBallColor() {
        return ballColor;
    }

    public void setBallColor(BallColor ballColor) {
        this.ballColor = ballColor;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Texture texture = AssetLoader.get().get("balls/basic/" + ballColor.getPath());
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        if (selected) {
            batch.draw(selectedTexture, getX(), getY(), getWidth(), getHeight());
        }
    }

    private class BallInputListener extends InputListener {
        private final Match match;

        private final Ball ball;

        public BallInputListener(Ball ball, Match match) {
            this.ball = ball;
            this.match = match;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            selected = !selected;
            match.ballSelected(ball);
            return true;
        }
    }
}
