/*
 * This file is part of Rectball.
 * Copyright (C) 2015 Dani Rodríguez.
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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Switch;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

public class SettingsScreen extends AbstractScreen {

    public SettingsScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        // Build stage entities.
        TextButton backButton = new TextButton("Back", game.getSkin());

        Texture switchTex = game.manager.get("ui/switch.png");
        final Switch soundSwitch = new Switch(switchTex, game.settings.isSoundEnabled(), false);
        final Switch colorSwitch = new Switch(switchTex, game.settings.isColorblind(), false);

        table.pad(20);
        table.add(boldLabel("Settings")).expandX().align(Align.center).colspan(2).height(100).row();
        table.add(newLabel("Sound")).expandX().align(Align.left).height(100);
        table.add(soundSwitch).width(150).height(50).row();
        table.add(newLabel("Colorblind")).expandX().align(Align.left).height(100);
        table.add(colorSwitch).width(150).height(50).row();
        table.add(backButton).fillX().expandY().height(125).align(Align.bottom).colspan(2).row();

        backButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // FIXME: I'm duplicate code!!
                TextButton button = (TextButton)actor;
                button.setChecked(false);

                game.settings.setSoundEnabled(soundSwitch.isEnabled());
                game.settings.setColorblind(colorSwitch.isEnabled());
                game.player.playSound(SoundCode.FAIL);

                game.settings.save();
                game.setScreen(Screens.MAIN_MENU);
            }
        });
    }

    @Override
    public int getID() {
        return Screens.SETTINGS;
    }

    /**
     * Adapter method for legacy bold label creation.
     * @param text the text for the label.
     * @return the bold label.
     */
    private Label boldLabel(CharSequence text) {
        return new Label(text, game.getSkin(), "bold");
    }

    /**
     * Adapter method for legacy label creation.
     * @param text  the text for the label.
     * @return  the label.
     */
    private Label newLabel(CharSequence text) {
        return new Label(text, game.getSkin());
    }
}
