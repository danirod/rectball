package es.danirod.rectball.scene2d.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import es.danirod.rectball.RectballGame;

/**
 * ChangeListener that dispatches an analytic analyticEvent at the same time it executes
 * another action such as a screen change.
 *
 * @author danirod
 * @since 0.4.0
 */
public class TrackingListener extends ChangeListener {

    private RectballGame game;

    private ChangeListener after;

    private String action, category, label;

    public TrackingListener(RectballGame game, String category,
                            String action, String label, ChangeListener after) {
        this.game = game;
        this.category = category;
        this.action = action;
        this.label = label;
        this.after = after;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        game.getPlatform().analytic().sendEvent(category, action, label);
        after.changed(event, actor);
    }
}
