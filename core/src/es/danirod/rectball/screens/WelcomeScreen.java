package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.utils.SoundPlayer;

public class WelcomeScreen extends MenuScreen {

    public WelcomeScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        // Build styles for title and version
        BitmapFont titleFont = boldGenerator.generateFont(buildFontStyle(100, 4, 2));
        BitmapFont versionFont = fontGenerator.generateFont(buildFontStyle(25, 0, 1));
        LabelStyle titleStyle = new LabelStyle(titleFont, Color.WHITE);
        LabelStyle versionStyle = new LabelStyle(versionFont, Color.WHITE);

        // Build the actors.
        Label title = new Label("Rectball", titleStyle);
        TextButton play = newButton("Play");
        TextButton settings = newButton("Settings");
        TextButton statistics = newButton("Stats");
        Label version = new Label(RectballGame.VERSION, versionStyle);

        // Position the actors in the screen.
        table.add(title).pad(20).align(Align.center).row();
        table.add(play).pad(20).fillX().height(100).row();
        table.add(settings).pad(20).fillX().height(100).row();
        table.add(statistics).pad(20).fillX().height(100).row();
        table.add(version).pad(20).align(Align.bottomRight).expandY().row();

        // Add sounds.
        final SoundPlayer player = new SoundPlayer(game);

        // Then add the capture listeners for the buttons.
        play.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.playSelect();
                game.setScreen(1);
            }
        });
        settings.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.playSelect();
                game.setScreen(4);
            }
        });
        statistics.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.playFail();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public int getID() {
        return 3;
    }
}
