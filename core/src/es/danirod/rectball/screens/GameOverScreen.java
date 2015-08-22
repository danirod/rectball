package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import es.danirod.rectball.AssetLoader;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Value;

public class GameOverScreen extends AbstractScreen {

    private Texture gameOver;

    private SpriteBatch batch;

    private Stage stage;

    private Value score;

    public GameOverScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void load() {
        batch = new SpriteBatch();
        gameOver = AssetLoader.get().get("gameover.png", Texture.class);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(480, 640));

        Image over = new Image(gameOver);
        over.setBounds(0, 80, 480, 480);
        stage.addActor(over);

        Texture sheet = AssetLoader.get().get("scores.png", Texture.class);
        score = new Value(sheet, 6, game.state.lastScore);
        score.setBounds(60, 100, 360, 100);
        stage.addActor(score);

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

        stage.act(delta);
        stage.draw();

        /*batch.begin();
        batch.draw(gameOver, 0, 0);
        batch.end();*/
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public int getID() {
        return 2;
    }
}
