package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import es.danirod.rectball.AssetLoader;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Switch;

public class WelcomeScreen extends AbstractScreen {

    private Stage stage;

    private Image play;

    private Switch blindMode;

    public WelcomeScreen(RectballGame game) {
        super(game, 3);
    }

    @Override
    public void load() {

    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(480, 640));

        Texture switchTex = AssetLoader.get().get("ui/switch.png", Texture.class);
        blindMode = new Switch(switchTex, game.state.colorblindMode);
        blindMode.setPosition(350, 50);
        stage.addActor(blindMode);

        Texture blindTex = AssetLoader.get().get("ui/blind.png", Texture.class);
        Image blindLabel = new Image(blindTex);
        blindLabel.setPosition(50, 50);
        stage.addActor(blindLabel);

        Texture playTex = AssetLoader.get().get("ui/play.png", Texture.class);
        play = new Image(playTex);
        play.setPosition(50, 200);
        play.setSize(380, 80);
        play.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Save switch state.
                game.state.colorblindMode = blindMode.isEnabled();
                game.state.save(Gdx.app.getPreferences("settings"));


                game.setScreen(1);
                return true;
            }
        });
        stage.addActor(play);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        game.state.save(Gdx.app.getPreferences("settings"));
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }
}
