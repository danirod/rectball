package es.danirod.rectball.screens;

import com.badlogic.gdx.Screen;

import es.danirod.rectball.RectballGame;

public abstract class AbstractScreen implements Screen {

    protected RectballGame game;

    public AbstractScreen(RectballGame game) {
        this.game = game;
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

    public abstract int getID();
}
