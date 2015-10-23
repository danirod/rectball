package es.danirod.rectball.scene2d.game;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Container that renders a yellow border around it.
 *
 * @author danirod
 * @since 0.4.0
 */
public class BorderedContainer extends Group {

    private Image border;

    private Actor actor;

    public BorderedContainer(Skin skin, Actor actor) {
        NinePatch yellowPatch = skin.get("yellowPatch", NinePatch.class);
        border = new Image(yellowPatch);
        border.setPosition(-10, -10);
        this.actor = actor;
        addActor(border);
        addActor(actor);
    }

    @Override
    protected void sizeChanged() {
        actor.setSize(getWidth(), getHeight());
        border.setSize(getWidth() + 20, getHeight() + 20);
    }
}
