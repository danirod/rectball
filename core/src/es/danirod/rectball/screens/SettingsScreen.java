package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import es.danirod.rectball.AssetLoader;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Button;
import es.danirod.rectball.actors.Switch;

public class SettingsScreen extends AbstractScreen {

    private Stage stage;

    private Table table;

    public SettingsScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void show() {
        Texture tex = AssetLoader.get().get("ui/yellowpatch.png", Texture.class);
        NinePatch ninePatch = new NinePatch(tex, 6, 6, 6, 6);
        NinePatchDrawable drb = new NinePatchDrawable(ninePatch);

        FileHandle font = Gdx.files.internal("fonts/Play-Regular.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(font);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        BitmapFont buttonFont = generator.generateFont(parameter);
        generator.dispose();

        LabelStyle labelStyle = new LabelStyle(buttonFont, Color.WHITE);
        TextButtonStyle tbs = new TextButtonStyle(drb, drb, drb, buttonFont);

        Viewport v = new FitViewport(480, 640);
        stage = new Stage(v);
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);

        Label musicLabel = new Label("MUSIC", labelStyle);
        Label soundLabel = new Label("SOUND", labelStyle);
        Label colorLabel = new Label("COLORBLIND", labelStyle);

        Texture switchTex = AssetLoader.get().get("ui/switch.png");
        final Switch musicSwitch = new Switch(switchTex, game.settings.isMusic());
        final Switch soundSwitch = new Switch(switchTex, game.settings.isSound());
        final Switch colorSwitch = new Switch(switchTex, game.settings.isColorblind());

        table.add(musicLabel).expandX().align(Align.right).height(100);
        table.add(musicSwitch).width(150).height(50).pad(25).row();

        table.add(soundLabel).expandX().align(Align.right).height(100);
        table.add(soundSwitch).width(150).height(50).pad(25).row();

        table.add(colorLabel).expandX().align(Align.right).height(100);
        table.add(colorSwitch).width(150).height(50).pad(25).row();

        TextButton backButton = new TextButton("BACK", tbs);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.settings.setMusic(musicSwitch.isEnabled());
                game.settings.setSound(soundSwitch.isEnabled());
                game.settings.setColorblind(colorSwitch.isEnabled());
                game.settings.save();
                game.setScreen(3);
            }
        });

        table.add(backButton).colspan(2).width(400).padTop(50).height(100).row();
    }

    @Override
    public int getID() {
        return 4;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.8f, 0.4f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }
}
