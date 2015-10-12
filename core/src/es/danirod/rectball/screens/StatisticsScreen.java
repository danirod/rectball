package es.danirod.rectball.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
<<<<<<< HEAD
=======
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
>>>>>>> feature/refactor
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.StatsTable;
import es.danirod.rectball.listeners.ScreenJumper;

/**
 * Statistics screen.
 */
<<<<<<< HEAD
public class StatisticsScreen extends MenuScreen {
=======
public class StatisticsScreen extends AbstractScreen {
>>>>>>> feature/refactor

    public StatisticsScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
<<<<<<< HEAD
        final TextButton backButton = newButton("Back");
        final Label stats = newLabel("Stats");

        table.pad(20);
        table.add(backButton).align(Align.left).expandX();
        table.add(stats).align(Align.center).expandX().row();

        StatsTable statsTable = new StatsTable(game.statistics, stats.getStyle(), stats.getStyle());

        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        ScrollPane pane = new ScrollPane(statsTable, style);
        table.add(pane).colspan(2).align(Align.topLeft).expand().fill();

=======
        table.pad(20);
        table.add(new Label("Statistics", game.getSkin(), "bold")).expandX().align(Align.center).height(100).row();

        LabelStyle bold = game.getSkin().get("bold", LabelStyle.class);
        LabelStyle normal = game.getSkin().get("small", LabelStyle.class);

        StatsTable statsTable = new StatsTable(game, bold, normal);
        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        ScrollPane pane = new ScrollPane(statsTable, style);
        table.add(pane).align(Align.topLeft).expand().fill().row();

        TextButton backButton = new TextButton(game.getLocale().get("core.back"), game.getSkin());
        table.add(backButton).fillX().expandY().height(80).padTop(20).align(Align.bottom).row();
>>>>>>> feature/refactor
        backButton.addCaptureListener(new ScreenJumper(game, Screens.MAIN_MENU));
    }

    @Override
    public int getID() {
        return Screens.STATISTICS;
    }

}
