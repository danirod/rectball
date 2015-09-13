package es.danirod.rectball.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class StyleFactory {

    private StyleFactory() {

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
    public static NinePatchDrawable buildPatch(TextureRegion r, int margin) {
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
    public static FreeTypeFontParameter buildFontStyle(int size, int shadow, int border) {
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.borderColor = parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = parameter.shadowOffsetY = shadow;
        parameter.borderWidth = border;
        parameter.size = size;
        return parameter;
    }

    /**
     * This method builds a bitmap font from a text file and some parameter.
     *
     * @param file string with the font name.
     * @param par  font parameter
     * @return  bitmap font
     */
    public static BitmapFont buildFont(String file, FreeTypeFontParameter par) {
        FileHandle handle = Gdx.files.internal(file);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(handle);
        BitmapFont font = generator.generateFont(par);
        generator.dispose();
        return font;
    }

    public static TextButtonStyle buildTextButtonStyle(TextureRegion normal, TextureRegion hover, int margin, BitmapFont font) {
        NinePatchDrawable normalDrawable = buildPatch(normal, margin);
        NinePatchDrawable hoverDrawable = buildPatch(hover, margin);
        return new TextButtonStyle(normalDrawable, hoverDrawable, hoverDrawable, font);
    }

}
