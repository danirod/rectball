package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.model.*;

public class GameScreen extends AbstractScreen {

    private Stage stage;

    public Match board;

    public Score score;

    public Timer timer;

    public GameScreen(RectballGame game) {
        super(game, 1);
    }

    @Override
    public void show() {
        v = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(v);

        // Set up the board
        board = new Match(this, 7);
        board.reload();
        stage.addActor(board);

        // Set up the score
        score = new Score(this, 0);
        score.setPosition(0, Gdx.graphics.getHeight() - 80);
        score.setSize(Gdx.graphics.getWidth(), 80);
        stage.addActor(score);

        // Set up the timer
        timer = new Timer(this, 30);
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
