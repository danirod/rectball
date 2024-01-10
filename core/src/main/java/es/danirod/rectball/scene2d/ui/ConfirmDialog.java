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

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * A generic confirmation dialog based on Scene2D.UI's Dialog class.
 */
public class ConfirmDialog extends CommonDialog {

    private ConfirmCallback callback = null;

    public ConfirmDialog(Skin skin, String text, String okText, String cancelText) {
        super(skin, text);
        TextButton okButton = new TextButton(okText, skin, "blue");
        TextButton cancelButton = new TextButton(cancelText, skin, "blue");
        button(okButton, "ok").button(cancelButton, "cancel");
    }

    public void setCallback(ConfirmCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void result(Object object) {
        if (callback != null) {
            if (object.equals("ok")) {
                callback.ok();
            } else if (object.equals("cancel")) {
                callback.cancel();
            }
        }

        cancel();
        hide(null);
    }

    /**
     * This is the callback class that will subscribe to events happening in
     * this confirmation dialog class.
     */
    public interface ConfirmCallback {

        /**
         * This event is triggered when the OK button is pressed.
         */
        void ok();

        /**
         * This event is triggered when the CANCEL button is pressed.
         */
        void cancel();
    }
}
