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

import com.badlogic.gdx.Application;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
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

    protected Stage stage;

    protected Table table;

    protected FreeTypeFontGenerator boldGenerator;

    private LabelStyle genericLabelStyle;

    private TextButtonStyle genericButtonStyle;

    public MenuScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void load() {
        // Load fonts used in the screen.
        FileHandle boldFont = Gdx.files.internal("fonts/Play-Bold.ttf");
        boldGenerator = new FreeTypeFontGenerator(boldFont);

        // Set up the generic style.
        BitmapFont font = StyleFactory.buildFont("fonts/Play-Regular.ttf", StyleFactory.buildFontStyle(64, 2, 1));
        genericLabelStyle = new LabelStyle(font, Color.WHITE);
    }

    @Override
    public void show() {
        // Load ninepatches used for buttons.
        Texture tex = game.manager.get("ui/button.png", Texture.class);
        TextureRegion normalRegion = new TextureRegion(tex, 0, 0, 128, 128);
        TextureRegion hoverRegion = new TextureRegion(tex, 128, 0, 128, 128);
        genericButtonStyle = StyleFactory.buildTextButtonStyle(normalRegion, hoverRegion, 32, genericLabelStyle.font);

        stage = new Stage(new FitViewport(540, 960));
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        stage.getCamera().update();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }

    @Override
    public void dispose() {
        boldGenerator.dispose();
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
