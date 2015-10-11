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

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * That class is designed to manage in-game pause dialog.
 */
public class LeaveDialog extends Dialog {

    /**
     * This is the class that executes callbacks when the user interacts.
     * TODO: This is calling for a generic ConfirmDialog class.
     */
    public interface LeaveDialogCallback {

        /** This event is invoked when the YES button is pressed. */
        void onYesButton();

        /** This event is invoked when the NO button is pressed. */
        void onNoButton();

    }

    private LeaveDialogCallback callbacks = null;

    /**
     * Creates a new instance of the leave dialog.
     */
    public LeaveDialog(Skin skin) {
        super("", skin);

        getButtonTable().defaults().height(80);
        pad(20);
        text("Leave game?").button("Yes", "yes").button("No", "no");
    }

    public void setCallback(LeaveDialogCallback callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    protected void result(Object object) {
        if (callbacks != null) {
            if (object.equals("yes")) {
                callbacks.onYesButton();
            } else {
                callbacks.onNoButton();
            }
        }

        cancel();
        hide(null);
    }
}
