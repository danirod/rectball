package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import es.danirod.rectball.AssetLoader;
import es.danirod.rectball.RectballGame;

public class GameOverScreen extends AbstractScreen {

    private Texture gameOver;

    private SpriteBatch batch;

    public GameOverScreen(RectballGame game) {
        super(game, 2);
    }

    @Override
    public void load() {
        batch = new SpriteBatch();
        gameOver = AssetLoader.get().get("gameover.png", Texture.class);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                game.setScreen(1);
                return true;
            }
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // TODO: Background color

        batch.begin();
        batch.draw(gameOver, 0, 0);
        batch.end();
    }
}
