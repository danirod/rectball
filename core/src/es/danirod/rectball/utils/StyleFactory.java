package es.danirod.rectball.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

@Deprecated
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

    public static TextButtonStyle buildTextButtonStyle(TextureRegion normal, TextureRegion hover, int margin, BitmapFont font) {
        NinePatchDrawable normalDrawable = buildPatch(normal, margin);
        NinePatchDrawable hoverDrawable = buildPatch(hover, margin);
        return new TextButtonStyle(normalDrawable, hoverDrawable, hoverDrawable, font);
    }

}
