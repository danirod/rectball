package es.danirod.rectball.scene2d.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import es.danirod.rectball.Constants;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.model.GameState;

/**
 * New timer actor. Attempts to fix every other mess up made in the old timer actor.
 */
public class TimeActor extends Actor {

    final RectballGame game;

    public TimeActor(RectballGame game) {
        this.game = game;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Needed for color blending.
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        // Background
        Texture progress = game.getSkin().get("progress", Texture.class);
        batch.setColor(0.5f, 0.5f, 0.5f, color.a * parentAlpha);
        batch.draw(progress, getX(), getY(), getWidth(), getHeight());

        // Calculate the remaining percentage of time.
        float percentage = game.getState().getRemainingTime() / Constants.SECONDS;
        float remainingSize = getWidth() * percentage;

        // Render the remaining time using the appropriate color.
        if (percentage < Constants.WARNING) {
            batch.setColor(color.r, 0, 0, color.a * parentAlpha);
        } else {
            batch.setColor(0, color.g, 0, color.a * parentAlpha);
        }
        batch.draw(progress, getX(), getY(), remainingSize, getHeight());

        batch.setColor(color.r, color.g, color.b, color.a);
    }

    @Override
    public void act(float delta) {

    }
}
