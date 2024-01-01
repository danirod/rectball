/* This file is part of Rectball
 * Copyright (C) 2015-2024  Dani Rodríguez
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.scene2d.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.Constants;

/**
 * Common dialog for Rectball. This common dialog is a tweak over Scene2D's
 * dialog to allow things such as full width dialogs or full width buttons.
 *
 * @since 0.3.0
 */
class CommonDialog extends Dialog {

    CommonDialog(Skin skin, String text) {
        super("", skin);

        // Put the table on center and don't let it move.
        setMovable(false);
        setResizable(false);
        pad(20);

        // Make the table use the full width.
        getCell(getContentTable()).growX();
        getCell(getButtonTable()).growX().padTop(20);

        // Make the child items use the max width.
        getContentTable().defaults().growX();
        getButtonTable().defaults().growX().uniformX().height(60f);

        // Add text to the center.
        Label textLabel = new Label(text, skin, "normal", "black");
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);
        getContentTable().add(textLabel).minWidth(Constants.VIEWPORT_WIDTH * 0.75f).prefWidth(Constants.VIEWPORT_WIDTH * 0.75f);
    }

    @Override
    public Dialog show(Stage stage) {
        setTransform(true);
        show(stage, Actions.sequence(Actions.scaleTo(0.8f, 0.8f), Actions.scaleTo(1f, 1f, 0.25f, Interpolation.sineOut)));
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
        setOrigin(Align.center);
        return this;
    }
}
