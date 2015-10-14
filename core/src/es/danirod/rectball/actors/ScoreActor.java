package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

/**
 * This actor renders the score.
 */
public class ScoreActor extends Group {

    /** The skin instance used to style this actor. */
    private Skin skin;

    /** The current value for this score. */
    private int value;

    /** Whether to pad with zeroes or not. */
    private boolean zeropad;

    /** The background. */
    private final Drawable background;

    /** The actual value that is rendered. */
    private final Label label;

    public ScoreActor(Skin skin) {
        this(skin, 0, true);
    }

    public ScoreActor(Skin skin, int value) {
        this(skin, value, true);
    }

    public ScoreActor(Skin skin, int value, boolean zeropad) {
        this.skin = skin;
        this.value = value;
        this.zeropad = zeropad;

        background = skin.newDrawable("pixel", Color.BLACK);
        label = new Label(getScore(), skin, "monospace");
        label.setAlignment(Align.bottom);
        label.setFillParent(true);
        label.setFontScale(7f);
        addActor(label);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        // Draw the background.

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        NinePatch yellowpatch = skin.get("yellowpatch", NinePatch.class);
        yellowpatch.draw(batch, getX() - 10, getY() - 10, getWidth() + 20, getHeight() + 20);

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
     * @return  the score as a string.
     */
    private String getScore() {
        String score = Integer.toString(value);
        if (zeropad) {
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
    }
}
