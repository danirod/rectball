package es.danirod.rectball.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import es.danirod.rectball.RectballGame;

public class ScreenJumper extends ChangeListener {

    private RectballGame game;

    private int target;

    public ScreenJumper(RectballGame game, int target) {
        this.game = game;
        this.target = target;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        // if the actor is a TextButton, we need
        // to force uncheck it
        if (actor instanceof TextButton) {
            TextButton button = (TextButton)actor;
            button.setChecked(false);
        }

        game.setScreen(target);
    }


}
