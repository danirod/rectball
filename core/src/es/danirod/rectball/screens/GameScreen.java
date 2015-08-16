package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.danirod.rectball.AssetLoader;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Timer;
import es.danirod.rectball.actors.Value;
import es.danirod.rectball.model.*;

public class GameScreen extends AbstractScreen {

    private Stage stage;

    public Match board;

    public Value value;

    public Timer timer;

    public GameScreen(RectballGame game) {
        super(game, 1);
    }

    @Override
    public void show() {
        v = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(v);
        stage.setDebugAll(true);

        // Set up the board
        board = new Match(this, 7);
        board.reload();
        stage.addActor(board);

        // Set up the value
        Texture numbers = AssetLoader.get().get("scores.png", Texture.class);
        value = new Value(numbers, 6, 0);
        value.setPosition(0, Gdx.graphics.getHeight() - 80);
        value.setSize(Gdx.graphics.getWidth(), 80);
        stage.addActor(value);

        // Set up the timer
        timer = new Timer(this, 5);
        timer.setPosition(0, Gdx.graphics.getHeight() - 100);
        timer.setSize(Gdx.graphics.getWidth(), 20);
        stage.addActor(timer);

        Gdx.input.setInputProcessor(stage);
    }

    Viewport v;

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    public void gameOver() {
        timer.setRunning(false);
        game.setScreen(2);
    }
}
