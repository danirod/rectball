package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Button extends Actor {

    private NinePatch patch;

    private BitmapFont font;

    private CharSequence text;

    private float textWidth, textHeight;

    public Button(NinePatch patch, BitmapFont font, CharSequence text) {
        this.patch = patch;
        this.font = font;
        this.text = text;
        calculateTextSize();
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
        calculateTextSize();
    }

    private void calculateTextSize() {
        GlyphLayout layout = new GlyphLayout(font, text);
        textWidth = layout.width;
        textHeight = layout.height;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // FIXME: Doesn't f***ing work and don't know why.

        patch.draw(batch, getX(), getY(), getWidth(), getHeight());
        float x = getX() + ((getWidth() - textWidth) / 2);
        float y = getY() + ((getHeight() - textHeight) / 2);
        font.draw(batch, text, x, y);
    }
}
