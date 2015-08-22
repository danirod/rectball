package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Switch extends Actor {

    private Texture sheet;

    private TextureRegion on, off;

    private Sprite sprite;

    private boolean enabled;

    public Switch(Texture sheet, boolean enabled) {
        this.sheet = sheet;
        int width = sheet.getWidth();
        int height = sheet.getHeight() / 2;
        on = new TextureRegion(sheet, 0, 0, width, height);
        off = new TextureRegion(sheet, 0, height, width, height);
        this.enabled = enabled;
        this.sprite = new Sprite(enabled ? on : off);
        setSize(width, height);

        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setEnabled(!isEnabled());
            }
        });
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        sprite.setRegion(enabled ? on : off);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch, parentAlpha);
    }

    @Override
    protected void sizeChanged() {
        sprite.setSize(getWidth(), getHeight());
    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(), getY());
    }
}
