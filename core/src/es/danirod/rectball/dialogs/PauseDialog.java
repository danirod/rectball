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

package es.danirod.rectball.dialogs;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * That class is designed to manage in-game pause dialog.
 */
public class PauseDialog extends Dialog {

    private Label titleLabel;
    private TextButton yesButton;
    private TextButton noButton;

    /**
     * Creates a new instance of the pause dialog.
     *
     *  @param windowStyle
     *      The window style to be applied to the dialog.
     *  @param labelStyle
     *      The style to be applied to the label.
     *  @param buttonStyle
     *      The style to be applied to the buttons.
     */
    public PauseDialog(WindowStyle windowStyle, Label.LabelStyle labelStyle, TextButton.TextButtonStyle buttonStyle) {
        super("", windowStyle);

        titleLabel = new Label("Leave game?", labelStyle);
        yesButton = new TextButton("Yes", buttonStyle);
        noButton = new TextButton("No", buttonStyle);

        pad(20).padTop(0);
        row();
        add(titleLabel).colspan(2).expandX().expandY().row();

        add(yesButton).expandX().height(80);
        add(noButton).expandX().height(80);
        row();
    }

    /**
     * Adds a new listener to the yes button.
     *
     *  @param listener
     *      The listener to be added.
     *
     *  @return
     *      Ever "true" (yes, see libGDX docs)
     */
    public boolean addYesButtonCaptureListener(EventListener listener) {
        return yesButton.addCaptureListener(listener);
    }

    /**
     * Adds a new listener to the no button.
     *
     *  @param listener
     *      The listener to be added.
     *
     *  @return
     *      Ever "true" (yes, see libGDX docs)
     */
    public boolean addNoButtonCaptureListener(EventListener listener) {
        return noButton.addCaptureListener(listener);
    }

    /**
     * Gets a reference to the title label.
     *
     *  @return
     *      The title label.
     */
    public Label getTitleLabel() {
        return titleLabel;
    }

    /**
     * Gets a reference to the yes button.
     *
     *  @return
     *      The yes button.
     */
    public TextButton getYesButton() {
        return yesButton;
    }

    /**
     * Gets a reference to the no button.
     *
     *  @return
     *      The no button.
     */
    public TextButton getNoButton() {
        return noButton;
    }
}
