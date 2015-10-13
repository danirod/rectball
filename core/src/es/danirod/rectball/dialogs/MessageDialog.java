package es.danirod.rectball.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A generic message dialog that displays some text and that has a button for
 * dismissing the dialog. This is useful to display some text to the user in a
 * way that is dismissible. Client code can still inject a callback to know
 * when the user dismisses the dialog.
 */
public class MessageDialog extends CommonDialog {

    public interface MessageCallback {

        /** Event received when the user dismisses the message. */
        void dismiss();

    }

    private MessageCallback callback = null;

    public MessageDialog(Skin skin, String text) {
        this(skin, text, "OK");
    }

    public MessageDialog(Skin skin, String text, String dismissText) {
        super(skin, text);
        button(dismissText, "dismiss");
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


}
