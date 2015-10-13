package es.danirod.rectball.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import es.danirod.rectball.RectballGame;

/**
 * Skin used in Rectball. Once this portion of code is more stable, this code
 * should be ported into a normal Skin file that can be used by Scene2D UI.
 */
public class RectballSkin extends Skin {

    /**
     * Game instance. I need access to the AssetManager.
     */
    private RectballGame game;

    /**
     * Build a new Rectball skin. This will set up all the required styles
     * so that they can be later retrieving using the methods from Skin class.
     * @param game
     */
    public RectballSkin(RectballGame game) {
        this.game = game;
        addPixmapStyles();
        addLabelStyles();
        addTextButtonStyles();
        addWindowStyles();
        addNinePatchesStyles();
        addTextureRegionStyles();
        addCheckboxStyles();
    }

    private void addCheckboxStyles() {
        Texture sheet = game.manager.get("ui/switch.png");
        int width = sheet.getWidth();
        int height = sheet.getHeight() / 3;
        TextureRegion broken = new TextureRegion(sheet, 0, 0, width, height);
        TextureRegion on = new TextureRegion(sheet, 0, height, width, height);
        TextureRegion off = new TextureRegion(sheet, 0, 2 * height, width, height);

        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.checkboxOn = new TextureRegionDrawable(on);
        style.checkboxOff = new TextureRegionDrawable(off);
        style.checkboxOnDisabled = style.checkboxOffDisabled = new TextureRegionDrawable(broken);
        style.font = game.manager.get("normalFont.ttf");
        add("default", style);
    }

    /**
     * Create the default label style and add it to this skin file. This is
     * the label style that will be used for every label using this skin.
     */
    private void addLabelStyles() {
        // Build the label style for normal font.
        BitmapFont normalFont = game.manager.get("normalFont.ttf");
        LabelStyle normalStyle = new LabelStyle(normalFont, Color.WHITE);
        this.add("default", normalStyle);

        // Build the label style for bold font.
        BitmapFont boldFont = game.manager.get("boldFont.ttf");
        LabelStyle boldStyle = new LabelStyle(boldFont, Color.WHITE);
        this.add("bold", boldStyle);

        // Build the label style for bold font.
        BitmapFont smallFont = game.manager.get("smallFont.ttf");
        LabelStyle smallStyle  = new LabelStyle(smallFont, Color.WHITE);
        this.add("small", smallStyle);

        // Build the monospace style
        BitmapFont monospaceFont = game.manager.get("monospace.ttf");
        LabelStyle monospaceStyle = new LabelStyle(monospaceFont, Color.WHITE);
        this.add("monospace", monospaceStyle);
    }

    private NinePatchDrawable generateButton(Color color, Color down) {
        Pixmap pixmap = new Pixmap(9, 9, Pixmap.Format.RGBA8888);
        pixmap.setColor(down);
        pixmap.fill();
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, 9, 5);

        Texture texture = new Texture(pixmap);
        NinePatch ninePatch = new NinePatch(texture, 4, 4, 4, 4);
        return new NinePatchDrawable(ninePatch);
    }

    private void addTextButtonStyles() {
        NinePatchDrawable upButton = generateButton(Color.GRAY, Color.DARK_GRAY);
        NinePatchDrawable downButton = generateButton(Color.DARK_GRAY, Color.GRAY);
        BitmapFont font = game.manager.get("normalFont.ttf");
        TextButtonStyle buttonStyle = new TextButtonStyle(upButton, downButton, downButton, font);
        this.add("default", buttonStyle);
    }

    private void addWindowStyles() {
        Drawable background = newDrawable("pixel", 0.3f, 0.3f, 0.3f, 1);
        BitmapFont font = game.manager.get("normalFont.ttf");
        WindowStyle window = new WindowStyle(font, Color.WHITE, background);
        add("default", window);
    }

    private void addNinePatchesStyles() {
        // Load the yellow ninepatches.
        Texture yellowTexture = game.manager.get("ui/yellowpatch.png");
        NinePatch yellowPatch = new NinePatch(yellowTexture, 10, 10, 10, 10);
        this.add("yellowpatch", yellowPatch);
    }

    private void addTextureRegionStyles() {
        // Texture region for the icons.
        {
            Texture icons = game.manager.get("ui/icons.png");
            TextureRegion[][] iconRegions = TextureRegion.split(icons, 256, 256);
            add("iconClock", iconRegions[0][0]);
            add("iconCrown", iconRegions[0][1]);
        }

        // Texture region for the progress.
        {
            Texture progress = game.manager.get("ui/progress.png");
            add("progress", progress);
        }
    }

    private void addPixmapStyles() {
        Pixmap pixel = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixel.setColor(Color.WHITE);
        pixel.fill();
        this.add("pixel", new Texture(pixel));
    }
}
