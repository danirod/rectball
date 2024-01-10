/* This file is part of Rectball
 * Copyright (C) 2015-2024  Dani Rodríguez
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import es.danirod.rectball.RectballGame;

/**
 * Skin used in Rectball. Once this portion of code is more stable, this code
 * should be ported into a normal Skin file that can be used by Scene2D UI.
 */
public class RectballSkin {

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
        game.getAppSkin().getFont("normal").setUseIntegerPositions(false);
        game.getAppSkin().getFont("small").setUseIntegerPositions(false);
        game.getAppSkin().getFont("large").setUseIntegerPositions(false);
        addLabelStyles();
        addExtraImageButtonStyles();
        addTextureRegionStyles();
        addCheckboxStyles();
        addScrollPaneStyles();
    }

    private void addScrollPaneStyles() {
        ScrollPaneStyle style = new ScrollPaneStyle();
        Drawable scroll = game.getAppSkin().newDrawable("pixel", 1, 1, 1, 0.20f);
        Drawable knob = game.getAppSkin().newDrawable("pixel", 1, 1, 1, 0.40f);
        scroll.setMinWidth(10);
        knob.setMinWidth(10);
        style.vScroll = style.hScroll = scroll;
        style.vScrollKnob = style.hScrollKnob = knob;
        game.getAppSkin().add("default", style);
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
        style.font = game.getAppSkin().get("normal", BitmapFont.class);
        game.getAppSkin().add("default", style);
    }

    /**
     * Create the default label style and add it to this skin file. This is
     * the label style that will be used for every label using this skin.
     */
    private void addLabelStyles() {
        BitmapFont monospaceFont2 = game.manager.get("fonts/monospace.fnt");
        LabelStyle monospaceStyle2 = new LabelStyle(monospaceFont2, Color.WHITE);
        game.getAppSkin().add("monospace2", monospaceStyle2);
    }

    private void addExtraImageButtonStyles() {
        if (game.getContext().getGameServices().getSupported()) {
            ImageButtonStyle buttonLeaderboard = new ImageButtonStyle(game.getAppSkin().get("lime", Button.ButtonStyle.class));
            Texture texture = game.manager.get("google/gpg_leaderboard.png");
            TextureRegion region = new TextureRegion(texture);
            TextureRegionDrawable leaderboard = new TextureRegionDrawable(region);
            buttonLeaderboard.imageUp = leaderboard;
            game.getAppSkin().add("leaderboard", buttonLeaderboard);
        }

        if (game.getContext().getGameServices().getSupported()) {
            ImageButtonStyle buttonAchievements = new ImageButtonStyle(game.getAppSkin().get("lime", Button.ButtonStyle.class));
            Texture texture = game.manager.get("google/gpg_achievements.png");
            TextureRegion region = new TextureRegion(texture);
            TextureRegionDrawable achievements = new TextureRegionDrawable(region);
            buttonAchievements.imageUp = achievements;
            game.getAppSkin().add("achievements", buttonAchievements);
        }
    }

    private void addTextureRegionStyles() {
        // Texture region for the progress.
        {
            Texture progress = game.manager.get("ui/progress.png");
            game.getAppSkin().add("progress", progress);
        }
    }
}
