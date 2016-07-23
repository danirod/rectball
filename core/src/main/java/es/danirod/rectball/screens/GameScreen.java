package es.danirod.rectball.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.scene2d.actors.TimeActor;

/**
 * A complete rewrite of the old GameScreen attempting to fix every mess up I made in the past.
 */
public class GameScreen extends AbstractScreen {

    private TimeActor time;

    public GameScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        // If Android has closed the game, don't reset the board.
        if  (!game.isRestoredState()) {
            game.getState().reset();
        }
    }

    @Override
    void setUpInterface(Table table) {
        this.time = new TimeActor(game);
        table.setDebug(true);
        table.add(time).fillX().expandX().height(80).row();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        game.getState().tick(delta);
    }

    @Override
    public int getID() {
        return Screens.GAME;
    }

}
