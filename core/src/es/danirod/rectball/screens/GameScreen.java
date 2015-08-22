package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import es.danirod.rectball.AssetLoader;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Board;
import es.danirod.rectball.actors.Timer;
import es.danirod.rectball.actors.Value;

public class GameScreen extends AbstractScreen {

    private Stage stage;

    public Board board;

    public Value score;

    public Timer timer;

    public GameScreen(RectballGame game) {
        super(game);
    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        // Set up the board
        board = new Board(this, game.settings.isColorblind(), 7);
        board.randomize();
        stage.addActor(board);

        // Set up the score
        Texture numbers = AssetLoader.get().get("scores.png", Texture.class);
        score = new Value(numbers, 6, 0);
        stage.addActor(score);

        // Set up the timer
        timer = new Timer(this, 30);
        stage.addActor(timer);

        resizeScene(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0.4f, 0.6f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    public void gameOver() {
        // Update the score... and the record.
        long lastScore = score.getValue();
        game.scores.setLastScore(lastScore);
        if (lastScore > game.scores.getHighestScore()) {
            game.scores.setHighestScore(lastScore);
        }

        timer.setRunning(false);
        game.setScreen(2);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        resizeScene(width, height);
    }

    private void resizeScene(int width, int height) {
        if ((float) width / height >= 1.6f) {
            horizontalResize(width, height);
        } else {
            verticalResize(width, height);
        }
    }

    private void horizontalResize(int width, int height) {
        // Put the board on the right.
        board.setSize(width * 0.5f, height * 0.9f);
        board.setPosition(width * 0.45f, height * 0.05f);

        // Put the score on the left.
        score.setSize(width * 0.35f, height / 8);
        score.setPosition(width * 0.05f, height * 0.6f);

        // Put the timer below the score.
        timer.setSize(width * 0.35f, height / 16);
        timer.setPosition(width * 0.05f, score.getY() - timer.getHeight());
    }

    private void verticalResize(int width, int height) {
        float scoreWidth = width * 0.9f;
        float scoreHeight = height / 8 * 0.9f;
        score.setSize(scoreWidth, scoreHeight);
        score.setPosition(width * 0.05f, height * 7 / 8);

        float timerWidth = width * 0.9f;
        float timerHeight = height / 16 * 0.9f;
        timer.setSize(timerWidth, timerHeight);
        timer.setPosition(width * 0.05f, score.getY() - timer.getHeight());

        float boardWidth = width * 0.9f;
        float boardHeight = timer.getY() * 0.9f;
        board.setSize(boardWidth, boardHeight);
        board.setPosition(width * 0.05f, height * 0.05f);
    }
}
