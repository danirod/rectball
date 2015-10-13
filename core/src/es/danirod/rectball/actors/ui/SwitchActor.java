package es.danirod.rectball.actors.ui;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/**
 * Switch actor.
 *
 * @since 0.3.0
 * @author danirod
 */
public class SwitchActor extends CheckBox {

    public SwitchActor(String text, Skin skin) {
        super(text, skin);
        padTop(10).padBottom(10);
        getImageCell().width(150).height(50).padRight(20);
        getLabelCell().align(Align.left).expandX();
    }
}
