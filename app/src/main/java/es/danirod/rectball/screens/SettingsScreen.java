/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2023 Dani Rodríguez.
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

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.android.R;
import es.danirod.rectball.scene2d.listeners.ScreenPopper;
import es.danirod.rectball.scene2d.menus.SettingsMenu;

public class SettingsScreen extends AbstractScreen {

    public SettingsScreen(RectballGame game) {
        super(game);
    }

    private SettingsMenu settings = null;

    private TextButton backButton = null;

    @Override
    public void setUpInterface(Table table) {
        if (this.settings == null) {
            this.settings = new SettingsMenu(game);
        }
        if (backButton == null) {
            backButton = new TextButton(game.getContext().getString(R.string.core_back), game.getSkin());
            backButton.addListener(new ScreenPopper(game));
        }

        table.add(settings).align(Align.top).expand().fill().row();
        table.add(backButton).fillX().height(80).padTop(20).align(Align.bottom).row();
    }

    @Override
    public void dispose() {
        this.settings = null;
        this.backButton = null;
    }

    @Override
    public int getID() {
        return Screens.SETTINGS;
    }
}
