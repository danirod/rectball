package es.danirod.rectball.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
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

        // Build the monospace style
        BitmapFont monospaceFont = game.manager.get("monospace.ttf");
        LabelStyle monospaceStyle = new LabelStyle(monospaceFont, Color.WHITE);
        this.add("monospace", monospaceStyle);
    }

    private void addTextButtonStyles() {
        // Load button assets.
        Texture buttonTexture = game.manager.get("ui/button.png", Texture.class);
        TextureRegion buttonUp = new TextureRegion(buttonTexture, 0, 0, 128, 128);
        TextureRegion buttonDown = new TextureRegion(buttonTexture, 128, 0, 128, 128);

        // Build button nine patches.
        NinePatchDrawable drawableUp = new NinePatchDrawable(new NinePatch(buttonUp, 32, 32, 32, 32));
        NinePatchDrawable drawableDown = new NinePatchDrawable(new NinePatch(buttonDown, 32, 32, 32, 32));
        BitmapFont font = game.manager.get("normalFont.ttf");
        TextButtonStyle buttonStyle = new TextButtonStyle(drawableUp, drawableDown, drawableDown, font);
        this.add("default", buttonStyle);
    }

    private void addWindowStyles() {
        Drawable background = newDrawable("pixel", Color.DARK_GRAY);
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
        // Texture regions for the timer.
        Texture timer = game.manager.get("timer.png");
        int width = timer.getWidth() / 3, height = timer.getHeight();
        TextureRegion background = new TextureRegion(timer, 0, 0, width, height);
        TextureRegion remaining = new TextureRegion(timer, height, 0, width, height);
        TextureRegion warning = new TextureRegion(timer, 2 * height, 0, width, height);
        this.add("timer_background", background);
        this.add("timer_remaining", remaining);
        this.add("timer_warning", warning);

        // Texture region for the icons.
        Texture icons = game.manager.get("ui/icons.png");
        TextureRegion[][] iconRegions = TextureRegion.split(icons, 256, 256);
        add("iconClock", iconRegions[0][0]);
        add("iconCrown", iconRegions[0][1]);
    }

    private void addPixmapStyles() {
        Pixmap pixel = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixel.setColor(Color.WHITE);
        pixel.fill();
        this.add("pixel", new Texture(pixel));
    }
}
