package es.danirod.rectball.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.utils.SoundPlayer;

/**
 * @since 0.3.0
 */
public class ScreenPopper extends ChangeListener {

    private RectballGame game;

    /** If true, the event will clear the stack, returning to the main menu. */
    private boolean popAll;

    public ScreenPopper(RectballGame game) {
        this(game, false);
    }

    public ScreenPopper(RectballGame game, boolean popAll) {
        this.game = game;
        this.popAll = popAll;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        game.player.playSound(SoundPlayer.SoundCode.FAIL);
        if (popAll) {
            game.clearStack();
        } else {
            game.popScreen();
        }

        // Cancel the event to avoid checking the actor
        event.cancel();
    }


}
