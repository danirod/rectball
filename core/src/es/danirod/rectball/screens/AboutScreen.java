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
        // TODO: Move this to the skin.
        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();

        table.setDebug(true);
        table.pad(20);

        Label header = new Label(game.getLocale().get("main.about"), game.getSkin(), "bold");
        table.add(header).expandX().align(Align.center).height(80).row();

        Table innerContainer = new Table();
        ScrollPane scroll = new ScrollPane(innerContainer, style);
        table.add(scroll).expand().fill().align(Align.top).row();

        innerContainer.add(getCreditsWidget()).fill().expand();

        TextButton backButton = new TextButton(game.getLocale().get("core.back"), game.getSkin());
        table.add(backButton).fillX().expandY().height(80).padTop(20).align(Align.bottom).row();
        backButton.addListener(new ScreenPopper(game));
    }

    private String getCredits() {
        return RectballGame.VERSION + "\n" + Gdx.files.internal("credits.txt").readString();
    }

    private Widget getCreditsWidget() {
        String credits = RectballGame.VERSION + "\n" + Gdx.files.internal("credits.txt").readString("UTF-8");
        Label creditsLabel = new Label(credits, game.getSkin(), "small");
        creditsLabel.setWrap(true);
        return creditsLabel;
    }

    @Override
    public int getID() {
        return Screens.ABOUT;
    }
}
