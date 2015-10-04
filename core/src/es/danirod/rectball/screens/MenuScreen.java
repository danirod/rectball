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
package es.danirod.rectball.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.utils.StyleFactory;

/**
 * Abstract base screen that should use screens related to menus, such as
 * the welcome menu, the settings menu or the credits menu. These screens
 * usually have buttons. And this screen makes easy to add buttons.
 *
 * @author danirod
 */
public abstract class MenuScreen extends AbstractScreen {

    private LabelStyle genericLabelStyle;

    private TextButtonStyle genericButtonStyle;

    public MenuScreen(RectballGame game) {
        super(game);
    }

    /**
     * Factory method used to build a new label using the default style.
     * @param text  text to put in the label
     * @return  the label as created.
     */
    protected Label newLabel(CharSequence text) {
        BitmapFont normalFont = game.manager.get("normalFont.ttf");
        LabelStyle genericLabelStyle = new LabelStyle(normalFont, Color.WHITE);
        return new Label(text, genericLabelStyle);
    }

    protected Label boldLabel(CharSequence text) {
        BitmapFont boldFont = game.manager.get("boldFont.ttf");
        LabelStyle boldStyle = new LabelStyle(boldFont, Color.WHITE);
        return new Label(text, boldStyle);
    }

    /**
     * Factory method used to build a new button using the default style.
     * @param text  text to put in the button
     * @return  the button as created.
     */
    protected TextButton newButton(CharSequence text) {
        BitmapFont normalFont = game.manager.get("normalFont.ttf");
        Texture tex = game.manager.get("ui/button.png", Texture.class);
        TextureRegion normalRegion = new TextureRegion(tex, 0, 0, 128, 128);
        TextureRegion hoverRegion = new TextureRegion(tex, 128, 0, 128, 128);
        TextButtonStyle genericButtonStyle = StyleFactory.buildTextButtonStyle(normalRegion, hoverRegion, 32, normalFont);
        return new TextButton(text.toString(), genericButtonStyle);
    }
}
