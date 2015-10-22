/*
 * This file is part of Rectball
 * Copyright (C) 2015 Dani Rodríguez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;
import es.danirod.rectball.scene2d.listeners.ScreenPopper;

/**
 * About screen.
 */
public class AboutScreen extends AbstractScreen {

    private static final int SCREEN_CREDITS = 1;
    private static final int SCREEN_LICENSE = 2;
    private int screen = SCREEN_CREDITS;
    private Table innerContainer;

    public AboutScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        innerContainer = new Table();
        ScrollPane scroll = new ScrollPane(innerContainer, game.getSkin());
        scroll.setFadeScrollBars(false);
        table.add(scroll).expand().fill().align(Align.top).row();
        innerContainer.defaults().fill().expand().padRight(10);

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
                game.player.playSound(SoundPlayer.SoundCode.SELECT);
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
