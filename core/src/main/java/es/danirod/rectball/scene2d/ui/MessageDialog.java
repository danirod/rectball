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
 * A generic message dialog that displays some text and that has a button for
 * dismissing the dialog. This is useful to display some text to the user in a
 * way that is dismissible. Client code can still inject a callback to know
 * when the user dismisses the dialog.
 */
public class MessageDialog extends CommonDialog {

    private MessageCallback callback = null;

    public MessageDialog(Skin skin, String text) {
        this(skin, text, "OK");
    }

    public MessageDialog(Skin skin, String text, String dismissText) {
        super(skin, text);
        TextButton dismiss = new TextButton(dismissText, skin, "blue");
        button(dismiss, "dismiss");
    }

    public void setCallback(MessageCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void result(Object object) {
        if (callback != null && object.equals("dismiss")) {
            callback.dismiss();
        }

        cancel();
        hide(null);
    }

    public interface MessageCallback {

        /**
         * Event received when the user dismisses the message.
         */
        void dismiss();

    }


}
