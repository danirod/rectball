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

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.scene2d.listeners.ScreenPopper;
import es.danirod.rectball.scene2d.ui.StatsTable;

/**
 * Statistics screen.
 */
public class StatisticsScreen extends AbstractScreen {

    public StatisticsScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        LabelStyle bold = game.getSkin().get("bold", LabelStyle.class);
        LabelStyle normal = game.getSkin().get("small", LabelStyle.class);

        StatsTable statsTable = new StatsTable(game, bold, normal);
        ScrollPane pane = new ScrollPane(statsTable, game.getSkin());
        pane.setFadeScrollBars(false);
        table.add(pane).align(Align.topLeft).expand().fill().row();

        TextButton backButton = new TextButton(game.getLocale().get("core.back"), game.getSkin());
        table.add(backButton).fillX().expandY().height(80).padTop(20).align(Align.bottom).row();
        backButton.addListener(new ScreenPopper(game));
    }

    @Override
    public int getID() {
        return Screens.STATISTICS;
    }

}
