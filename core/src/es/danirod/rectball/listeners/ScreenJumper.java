package es.danirod.rectball.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
        game.setScreen(target);
    }


}
