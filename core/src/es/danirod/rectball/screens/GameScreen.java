package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.model.Ball;
import es.danirod.rectball.model.BallColor;
import es.danirod.rectball.model.Match;

public class GameScreen extends AbstractScreen {

    private Texture gdxTexture;

    private Stage stage;

    private Match match;

    public GameScreen(RectballGame game) {
        super(game, 1);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    Viewport v;

    @Override
    public void load() {
        gdxTexture = new Texture(Gdx.files.internal("background_game.png"));
        v = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(v);

        match = new Match(7);
        match.reload();
        stage.addActor(match);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        super.dispose();
        gdxTexture.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
