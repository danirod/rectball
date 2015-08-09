package es.danirod.rectball.screens;

import com.badlogic.gdx.Screen;

import es.danirod.rectball.RectballGame;

public abstract class AbstractScreen implements Screen {

    protected RectballGame game;

    /**
     * The screen ID. Every screen should have an unique identifier.
     */
    private int id;

    public AbstractScreen(RectballGame game, int id) {
        this.game = game;
        this.id = id;
    }

    @Override
    public void resize(int width, int height) {

    }

    public void load() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public int getID() {
        return id;
    }
}
