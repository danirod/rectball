package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Switch;

public class SettingsScreen extends MenuScreen {

    public SettingsScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        // Build stage entities.
        Label musicLabel = newLabel("Music");
        Label soundLabel = newLabel("Sound");
        Label colorLabel = newLabel("Colorblind");
        TextButton backButton = newButton("Back");

        Texture switchTex = game.manager.get("ui/switch.png");
        final Switch musicSwitch = new Switch(switchTex, game.settings.isMusic(), true);
        final Switch soundSwitch = new Switch(switchTex, game.settings.isSound(), true);
        final Switch colorSwitch = new Switch(switchTex, game.settings.isColorblind(), false);

        table.add(musicLabel).pad(20).fillX().height(100);
        table.add(musicSwitch).pad(25).width(150).height(50).row();
        table.add(soundLabel).pad(20).fillX().height(100);
        table.add(soundSwitch).pad(25).width(150).height(50).row();
        table.add(colorLabel).pad(20).fillX().height(100);
        table.add(colorSwitch).pad(25).width(150).height(50).row();
        table.add(backButton).pad(20).fillX().height(100).colspan(2).row();

        backButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.settings.setMusic(musicSwitch.isEnabled());
                game.settings.setSound(soundSwitch.isEnabled());
                game.settings.setColorblind(colorSwitch.isEnabled());
                game.settings.save();
                game.setScreen(3);
            }
        });
    }

    @Override
    public int getID() {
        return 4;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.7f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }
}
