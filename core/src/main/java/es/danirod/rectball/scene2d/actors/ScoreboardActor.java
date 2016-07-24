package es.danirod.rectball.scene2d.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import es.danirod.rectball.RectballGame;

public class ScoreboardActor extends Actor {

    final RectballGame game;

    final Label score;

    public ScoreboardActor(RectballGame game) {
        this.game = game;

        score = new Label(Integer.toString(game.getState().getScore()), game.getSkin(), "bold");
        score.setAlignment(Align.center);
        score.setFillParent(true);
        score.setFontScale(2f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Set up the alpha for this item.
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        // Render background
        score.setBounds(getX(), getY(), getWidth(), getHeight());
        score.setText(updateScore());
        score.draw(batch, 1.0f);

        // Restore original color for the batch.
        batch.setColor(color.r, color.g, color.b, color.a);
    }

    String updateScore() {
        return String.format("%05d", game.getState().getScore());
    }

    @Override
    public void act(float delta) {

    }
}
