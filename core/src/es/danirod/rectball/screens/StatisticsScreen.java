package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.Map;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.StatsTable;
import es.danirod.rectball.statistics.StatisticSet;
import es.danirod.rectball.statistics.Statistics;
import es.danirod.rectball.utils.SoundPlayer;

/**
 * Statistics screen.
 */
public class StatisticsScreen extends MenuScreen {

    public StatisticsScreen(RectballGame game) {
        super(game);
    }

    @Override
    public int getID() {
        return Screens.STATISTICS;
    }

    @Override
    public void show() {
        super.show();

        final TextButton backButton = newButton("Back");
        final Label stats = newLabel("Stats");

        table.pad(20);
        table.add(backButton).align(Align.left).expandX();
        table.add(stats).align(Align.center).expandX().row();

        StatsTable statsTable = new StatsTable(game.statistics, stats.getStyle(), stats.getStyle());

        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        ScrollPane pane = new ScrollPane(statsTable, style);
        table.add(pane).colspan(2).align(Align.topLeft).expand().fill();

        backButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                transitionScreen(Screens.MAIN_MENU);
            }
        });
    }

    private void transitionScreen(final int screenID) {
        stage.getRoot().addAction(
                Actions.sequence(
                        Actions.fadeOut(0.25f),
                        Actions.run(new Runnable() {

                            @Override
                            public void run() {
                                game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
                                game.setScreen(screenID);
                            }
                        })
                )
        );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.6f, 0.6f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }
}
