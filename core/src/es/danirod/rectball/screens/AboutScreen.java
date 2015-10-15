package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

    private static final int SCREEN_CREDITS = 1;
    private static final int SCREEN_LICENSE = 2;
    private int screen = SCREEN_CREDITS;

    private Table innerContainer;

    @Override
    public void setUpInterface(Table table) {
        // TODO: Move this to the skin.
        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();

        table.pad(20);

        Label header = new Label(game.getLocale().get("main.about"), game.getSkin(), "bold");
        table.add(header).expandX().align(Align.center).height(80).row();

        innerContainer = new Table();
        ScrollPane scroll = new ScrollPane(innerContainer, style);
        table.add(scroll).expand().fill().align(Align.top).row();
        innerContainer.defaults().fill().expand();

        screen = SCREEN_CREDITS;
        updateScrollPane();

        final TextButton changeButton = new TextButton(game.getLocale().get("about.license"), game.getSkin());
        changeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (screen == SCREEN_CREDITS) {
                    screen = SCREEN_LICENSE;
                    changeButton.setText(game.getLocale().get("about.credits"));
                    updateScrollPane();
                } else {
                    screen = SCREEN_CREDITS;
                    changeButton.setText(game.getLocale().get("about.license"));
                    updateScrollPane();
                }
                event.cancel();
            }
        });

        Table buttonRow = new Table();
        buttonRow.defaults().fill().expand().width(Value.maxWidth).space(10);
        buttonRow.add(changeButton);

        TextButton backButton = new TextButton(game.getLocale().get("core.back"), game.getSkin());
        buttonRow.add(backButton).row();
        backButton.addListener(new ScreenPopper(game));

        table.add(buttonRow).expandX().fillX().height(60).padTop(20).align(Align.bottom).row();
    }

    private void updateScrollPane() {
        innerContainer.clear();
        switch (screen) {
            case SCREEN_LICENSE:
                innerContainer.add(getLicenseWidget()).row();
                break;
            case SCREEN_CREDITS:
                innerContainer.add(getCreditsWidget()).row();
                break;
        }
    }

    private Widget getCreditsWidget() {
        String credits = RectballGame.VERSION + "\n" + Gdx.files.internal("credits.txt").readString("UTF-8");
        Label creditsLabel = new Label(credits, game.getSkin(), "small");
        creditsLabel.setWrap(true);
        return creditsLabel;
    }

    private Widget getLicenseWidget() {
        String license = Gdx.files.internal("license.txt").readString("UTF-8");
        Label licenseLabel = new Label(license, game.getSkin(), "small");
        licenseLabel.setWrap(true);
        return licenseLabel;
    }

    @Override
    public int getID() {
        return Screens.ABOUT;
    }
}
