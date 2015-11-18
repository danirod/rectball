/*
 * This file is part of Rectball
 * Copyright (C) 2015 Dani Rodríguez
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

package es.danirod.rectball.scene2d;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import es.danirod.rectball.RectballGame;

import javax.xml.soap.Text;

/**
 * Skin used in Rectball. Once this portion of code is more stable, this code
 * should be ported into a normal Skin file that can be used by Scene2D UI.
 */
public class RectballSkin extends Skin {

    /**
     * Game instance. I need access to the AssetManager.
     */
    private final RectballGame game;

    /**
     * Build a new Rectball skin. This will set up all the required styles
     * so that they can be later retrieving using the methods from Skin class.
     *
     * @param game the game this skin is attached to.
     */
    public RectballSkin(RectballGame game) {
        this.game = game;
        addPixmapStyles();
        addLabelStyles();
        addTextButtonStyles();
        addTextureRegionStyles();
        addImageButtonStyles();
        addWindowStyles();
        addNinePatchesStyles();
        addCheckboxStyles();
        addScrollPaneStyles();
    }

    private void addScrollPaneStyles() {
        ScrollPaneStyle style = new ScrollPaneStyle();
        Drawable scroll = newDrawable("pixel", 1, 1, 1, 0.20f);
        Drawable knob = newDrawable("pixel", 1, 1, 1, 0.40f);
        scroll.setMinWidth(10);
        knob.setMinWidth(10);
        style.vScroll = style.hScroll = scroll;
        style.vScrollKnob = style.hScrollKnob = knob;
        add("default", style);
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
        style.font = game.manager.get("fonts/normal.ttf");
        add("default", style);
    }

    /**
     * Create the default label style and add it to this skin file. This is
     * the label style that will be used for every label using this skin.
     */
    private void addLabelStyles() {
        // Build the label style for normal font.
        BitmapFont normalFont = game.manager.get("fonts/normal.ttf");
        LabelStyle normalStyle = new LabelStyle(normalFont, Color.WHITE);
        this.add("default", normalStyle);

        // Build the label style for bold font.
        BitmapFont boldFont = game.manager.get("fonts/bold.ttf");
        LabelStyle boldStyle = new LabelStyle(boldFont, Color.WHITE);
        this.add("bold", boldStyle);

        // Build the label style for bold font.
        BitmapFont smallFont = game.manager.get("fonts/normal.ttf");
        LabelStyle smallStyle = new LabelStyle(smallFont, Color.WHITE);
        this.add("small", smallStyle);

        BitmapFont bigFont = game.manager.get("fonts/normal.ttf");
        LabelStyle bigStyle = new LabelStyle(bigFont, Color.WHITE);
        this.add("big", bigStyle);

        // Build the monospace style
        BitmapFont monospaceFont = game.manager.get("fonts/monospace.ttf");
        LabelStyle monospaceStyle = new LabelStyle(monospaceFont, Color.WHITE);
        this.add("monospace", monospaceStyle);

        BitmapFont monospaceFont2 = game.manager.get("fonts/monospace.ttf");
        LabelStyle monospaceStyle2 = new LabelStyle(monospaceFont2, Color.WHITE);
        this.add("monospace2", monospaceStyle2);
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
        {
            NinePatchDrawable upButton = generateButton(Color.GRAY, Color.DARK_GRAY);
            NinePatchDrawable downButton = generateButton(Color.DARK_GRAY, Color.GRAY);
            BitmapFont font = game.manager.get("fonts/normal.ttf");
            TextButtonStyle buttonStyle = new TextButtonStyle(upButton, downButton, downButton, font);
            this.add("default", buttonStyle);
        }

        {
            NinePatchDrawable upButton = generateButton(Color.valueOf("37c837"), Color.valueOf("37c837").lerp(Color.BLACK, 0.25f));
            NinePatchDrawable downButton = generateButton(Color.valueOf("37c837").lerp(Color.BLACK, 0.25f), Color.valueOf("37c837"));
            BitmapFont font = game.manager.get("fonts/normal.ttf");
            TextButtonStyle buttonStyle = new TextButtonStyle(upButton, downButton, downButton, font);
            this.add("green", buttonStyle);
        }

        {
            NinePatchDrawable upButton = generateButton(Color.valueOf("0066cc"), Color.valueOf("0066cc").lerp(Color.BLACK, 0.25f));
            NinePatchDrawable downButton = generateButton(Color.valueOf("0066cc").lerp(Color.BLACK, 0.25f), Color.valueOf("0066cc"));
            BitmapFont font = game.manager.get("fonts/normal.ttf");
            TextButtonStyle buttonStyle = new TextButtonStyle(upButton, downButton, downButton, font);
            this.add("blue", buttonStyle);
        }
    }

    private ImageButtonStyle buildImageButton(TextButtonStyle source, String region) {
        ImageButtonStyle style = new ImageButtonStyle(source);
        style.imageUp = new TextureRegionDrawable(getRegion(region));
        return style;
    }

    private void addImageButtonStyles() {
        add("share", buildImageButton(get(TextButtonStyle.class), "iconShare"));
        add("repeat", buildImageButton(get(TextButtonStyle.class), "iconRepeat"));
        add("house", buildImageButton(get(TextButtonStyle.class), "iconHouse"));
        add("help", buildImageButton(get(TextButtonStyle.class), "iconQuestion"));
        add("cross", buildImageButton(get(TextButtonStyle.class), "iconCross"));

        add("blueHelp", buildImageButton(get("blue", TextButtonStyle.class), "iconQuestion"));
        add("blueCross", buildImageButton(get("blue", TextButtonStyle.class), "iconCross"));
        add("greenPlay", buildImageButton(get("green", TextButtonStyle.class), "iconPlay"));

        add("play", buildImageButton(get(TextButtonStyle.class), "iconPlay"));
        add("settings", buildImageButton(get(TextButtonStyle.class), "iconSettings"));
        add("info", buildImageButton(get(TextButtonStyle.class), "iconInfo"));
        add("charts", buildImageButton(get(TextButtonStyle.class), "iconCharts"));
        add("star", buildImageButton(get(TextButtonStyle.class), "iconStar"));

        {
            Drawable redCross = newDrawable("iconCross", 0.9f, 0.1f, 0.1f, 1f);
            ImageButtonStyle crossStyle = new ImageButtonStyle(null, null, null, redCross, null, null);
            add("quit", crossStyle);
        }
    }

    private void addWindowStyles() {
        final Color backgroundColor = new Color(0, 0.3f, 0.6f, 1f);
        final Color borderColor = new Color(backgroundColor).lerp(Color.WHITE, 0.2f);
        final int borderBorder = 4;
        final int borderWidth = 4;

        final int pixmapSize = 2 * (borderBorder + borderWidth) + 1;

        Pixmap windowBackground = new Pixmap(pixmapSize, pixmapSize, Pixmap.Format.RGBA8888);

        windowBackground.setColor(backgroundColor);
        windowBackground.fill();

        windowBackground.setColor(borderColor);
        windowBackground.fillRectangle(borderBorder, borderBorder, pixmapSize - 2 * borderBorder, pixmapSize - 2 * borderBorder);

        windowBackground.setColor(backgroundColor);
        windowBackground.fillRectangle(borderBorder + borderWidth, borderBorder + borderWidth, pixmapSize - 2 * (borderBorder + borderWidth), pixmapSize - 2 * (borderBorder + borderWidth));

        Texture backgroundWindow = new Texture(windowBackground);
        NinePatch backgroundPatch = new NinePatch(backgroundWindow, borderBorder + borderWidth, borderBorder + borderWidth, borderBorder + borderWidth, borderBorder + borderWidth);
        Drawable background = new NinePatchDrawable(backgroundPatch);
        BitmapFont font = game.manager.get("fonts/normal.ttf");
        WindowStyle window = new WindowStyle(font, Color.WHITE, background);
        add("default", window);
    }

    private void addNinePatchesStyles() {
        // Load the yellow nine patches.
        Texture yellowTexture = game.manager.get("ui/yellow_patch.png");
        NinePatch yellowPatch = new NinePatch(yellowTexture, 10, 10, 10, 10);
        this.add("yellowPatch", yellowPatch);
    }

    private void addTextureRegionStyles() {
        // Texture region for the icons.
        {
            Texture icons = game.manager.get("ui/icons.png");
            TextureRegion[][] iconRegions = TextureRegion.split(icons, 100, 100);
            add("iconCharts", iconRegions[0][0]);
            add("iconScore", iconRegions[0][1]);
            add("iconStar", iconRegions[0][2]);
            add("iconSettings", iconRegions[0][3]);
            add("iconCross", iconRegions[0][4]);
            add("iconShare", iconRegions[1][0]);
            add("iconQuestion", iconRegions[1][1]);
            add("iconRepeat", iconRegions[1][2]);
            add("iconPlay", iconRegions[1][3]);
            add("iconClock", iconRegions[2][0]);
            add("iconCrown", iconRegions[2][1]);
            add("iconHouse", iconRegions[2][2]);
            add("iconInfo", iconRegions[2][3]);
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
