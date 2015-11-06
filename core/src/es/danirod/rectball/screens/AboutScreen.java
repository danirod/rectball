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

    private Table innerContainer = null;

    private String credits = null;

    private ScrollPane scroll = null;

    private TextButton backButton = null;

    private Label creditsLabel = null;

    public AboutScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        if (credits == null) {
            credits = RectballGame.VERSION + "\n" + Gdx.files.internal("credits.txt").readString("UTF-8");
        }
        if (creditsLabel == null) {
            creditsLabel = new Label(credits, game.getSkin(), "small");
            creditsLabel.setWrap(true);
        }
        if (innerContainer == null) {
            innerContainer = new Table();
            innerContainer.defaults().fill().expand().padRight(10);
            innerContainer.add(creditsLabel);
        }
        if (scroll == null) {
            scroll = new ScrollPane(innerContainer, game.getSkin());
            scroll.setFadeScrollBars(false);
        }
        if (backButton == null) {
            backButton = new TextButton(game.getLocale().get("core.back"), game.getSkin());
            backButton.addListener(new ScreenPopper(game));
        }
        table.add(scroll).expand().fill().align(Align.top).row();
        table.add(backButton).fillX().height(80).padTop(20).align(Align.bottom).row();
    }

    @Override
    public void dispose() {
        credits = null;
        creditsLabel = null;
        backButton = null;
        innerContainer = null;
        scroll = null;
    }

    @Override
    public int getID() {
        return Screens.ABOUT;
    }
}
