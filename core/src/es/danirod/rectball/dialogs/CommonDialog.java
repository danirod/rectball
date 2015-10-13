package es.danirod.rectball.dialogs;

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

    public CommonDialog(Skin skin, String text) {
        super("", skin);

        // Put the table on center and don't let it move.
        setMovable(false);
        setResizable(false);
        pad(20);

        // Make the table use the full width.
        getCell(getContentTable()).fillX().expandX();
        getCell(getButtonTable()).fillX().expandX().padTop(20);

        // Make the child items use the max width.
        getContentTable().defaults().expandX().fillX();
        getButtonTable().defaults().expandX().fillX();

        // Add text to the center.
        Label textLabel = new Label(text, skin);
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);
        getContentTable().add(textLabel).width(Constants.VIEWPORT_WIDTH - 80);
    }

    @Override
    public Dialog show(Stage stage) {
        show(stage, Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.1f)));
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
        return this;
    }
}
