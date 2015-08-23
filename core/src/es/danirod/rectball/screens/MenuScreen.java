package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import es.danirod.rectball.AssetLoader;
import es.danirod.rectball.RectballGame;

/**
 * Abstract base screen that should use screens related to menus, such as
 * the welcome menu, the settings menu or the credits menu. These screens
 * usually have buttons. And this screen makes easy to add buttons.
 *
 * @author danirod
 */
public abstract class MenuScreen extends AbstractScreen {

    protected Stage stage;

    protected Table table;

    protected FreeTypeFontGenerator fontGenerator, boldGenerator;

    private LabelStyle genericLabelStyle;

    private TextButtonStyle genericButtonStyle;

    public MenuScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void load() {
        // Load ninepatches used for buttons.
        Texture tex = AssetLoader.get().get("ui/button.png", Texture.class);
        TextureRegion normalRegion = new TextureRegion(tex, 0, 0, 128, 128);
        TextureRegion hoverRegion = new TextureRegion(tex, 128, 0, 128, 128);
        NinePatchDrawable normalPatch = buildPatch(normalRegion, 32);
        NinePatchDrawable hoverPatch = buildPatch(hoverRegion, 32);

        // Load fonts used in the screen.
        FileHandle normalFont = Gdx.files.internal("fonts/Play-Regular.ttf");
        FileHandle boldFont = Gdx.files.internal("fonts/Play-Bold.ttf");
        fontGenerator = new FreeTypeFontGenerator(normalFont);
        boldGenerator = new FreeTypeFontGenerator(boldFont);

        // Set up the generic style.
        BitmapFont genericFont = fontGenerator.generateFont(buildFontStyle(48, 2, 1));
        genericLabelStyle = new LabelStyle(genericFont, Color.WHITE);
        genericButtonStyle = new TextButtonStyle(normalPatch, hoverPatch, hoverPatch, genericFont);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(480, 640));
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }

    @Override
    public void dispose() {
        fontGenerator.dispose();
        boldGenerator.dispose();
    }

    /**
     * This method builds a NinePatch from a texture region. The margin is
     * the same for all the borders of the texture region so this should be
     * used with buttons.
     *
     * @param r  texture region to make the nine patch from.
     * @param margin  margin used to select the areas from the patch.
     * @return  drawable ninepatch for the given region
     */
    protected static NinePatchDrawable buildPatch(TextureRegion r, int margin) {
        NinePatch patch = new NinePatch(r, margin, margin, margin, margin);
        return new NinePatchDrawable(patch);
    }

    /**
     * This method builds the parameter for setting up fonts.
     *
     * @param size  size of the font
     * @param shadow  size of the shadow
     * @param border  size of the border
     * @return  font parameters for the given properties.
     */
    protected static FreeTypeFontParameter buildFontStyle(int size, int shadow, int border) {
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.borderColor = parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = parameter.shadowOffsetY = shadow;
        parameter.borderWidth = border;
        parameter.size = size;
        return parameter;
    }

    /**
     * Factory method used to build a new label using the default style.
     * @param text  text to put in the label
     * @return  the label as created.
     */
    protected Label newLabel(CharSequence text) {
        return new Label(text, genericLabelStyle);
    }

    /**
     * Factory method used to build a new button using the default style.
     * @param text  text to put in the button
     * @return  the button as created.
     */
    protected TextButton newButton(CharSequence text) {
        return new TextButton(text.toString(), genericButtonStyle);
    }
}
