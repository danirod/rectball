package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Switch extends Actor {

    private Texture sheet;

    private TextureRegion on, off, broken;

    private Sprite sprite;

    private boolean enabled, locked;

    public Switch(Texture sheet, boolean enabled, boolean locked) {
        this.sheet = sheet;
        this.enabled = enabled;
        this.locked = locked;

        int width = sheet.getWidth();
        int height = sheet.getHeight() / 3;
        broken = new TextureRegion(sheet, 0, 0, width, height);
        on = new TextureRegion(sheet, 0, height, width, height);
        off = new TextureRegion(sheet, 0, 2 * height, width, height);
        this.sprite = new Sprite(locked ? broken : (enabled ? on : off));
        setSize(width, height);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!isLocked()) {
                    setEnabled(!isEnabled());
                }
            }
        });
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        sprite.setRegion(locked ? broken : (enabled ? on : off));
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        sprite.setRegion(locked ? broken : (enabled ? on : off));
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
