package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.StatsTable;
import es.danirod.rectball.listeners.ScreenJumper;
import es.danirod.rectball.listeners.ScreenPopper;

/**
 * About screen.
 */
public class AboutScreen extends AbstractScreen {

    public AboutScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        table.pad(20);
        table.add(new Label(game.getLocale().get("main.about"), game.getSkin(), "bold")).expandX().align(Align.center).height(100).row();

        Label credits = new Label(getCredits(), game.getSkin(), "small");
        credits.setWrap(true);
        credits.setFillParent(true);

        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        ScrollPane pane = new ScrollPane(credits, style);
        table.add(pane).align(Align.topLeft).expand().fill().row();

        TextButton backButton = new TextButton(game.getLocale().get("core.back"), game.getSkin());
        table.add(backButton).fillX().expandY().height(80).padTop(20).align(Align.bottom).row();
        backButton.addCaptureListener(new ScreenPopper(game));
    }

    private String getCredits() {
        return RectballGame.VERSION + "\n" + Gdx.files.internal("credits.txt").readString();
    }

    @Override
    public int getID() {
        return Screens.ABOUT;
    }

}
