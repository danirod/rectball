package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import es.danirod.rectball.AssetLoader;
import es.danirod.rectball.model.Match;

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

    /** Ball texture. */
    private Texture texture;

    /** The actual sprite for this ball. */
    private Sprite sprite;

    private final int row, col;

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Ball(BallColor ballColor, final Match match, int row, int col) {
        this.ballColor = ballColor;
        this.selected = false;
        this.row = row;
        this.col = col;
        addListener(new BallInputListener(this, match));
        texture = AssetLoader.get().get("balls.png");
        sprite = new Sprite(ballColor.getRegion(texture));
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
    public void act(float delta) {
        sprite.setPosition(getX(), getY());
        sprite.setSize(getWidth(), getHeight());
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
