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

package es.danirod.rectball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;
import es.danirod.rectball.scene2d.listeners.ScreenPopper;
import es.danirod.rectball.scene2d.ui.StatsTable;

/**
 * Statistics screen.
 */
public class StatisticsScreen extends AbstractScreen {

    private StatsTable statsTable = null;

    private ScrollPane pane = null;

    private TextButton backButton = null;

    private ImageButton shareButton = null;

    public StatisticsScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        if (statsTable == null) {
            LabelStyle bold = game.getSkin().get("bold", LabelStyle.class);
            LabelStyle normal = game.getSkin().get("small", LabelStyle.class);
            statsTable = new StatsTable(game, bold, normal);
        }

        if (pane == null) {
            pane = new ScrollPane(statsTable, game.getSkin());
            pane.setFadeScrollBars(false);
        }

        if (backButton == null) {
            backButton = new TextButton(game.getLocale().get("core.back"), game.getSkin());
            backButton.addListener(new ScreenPopper(game));
        }

        if (shareButton == null) {
            // Add share button
            shareButton = new ImageButton(game.getSkin(), "share");
            shareButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.player.playSound(SoundPlayer.SoundCode.SELECT);

                    /* Generate a new table. */
                    LabelStyle bold = game.getSkin().get("bold", LabelStyle.class);
                    LabelStyle normal = game.getSkin().get("small", LabelStyle.class);
                    StatsTable table = new StatsTable(game, bold, normal);

                    /* Build an offscreen FrameBuffer. */
                    float aspectRatio = statsTable.getWidth() / statsTable.getHeight();
                    int width = 480, height = (int) (480 / aspectRatio);
                    FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

                    /* Generate a Stage for this FrameBuffer. */
                    Stage stage = new Stage(new FitViewport(width, height));
                    table.setFillParent(true);
                    stage.addActor(table);

                    /* Render the FBO. */
                    fbo.begin();
                    Gdx.gl.glClearColor(0.3f, 0.4f, 0.4f, 1f);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                    stage.act();
                    stage.draw();
                    Pixmap pmap = game.requestScreenshot(0, 0, width, height);
                    fbo.end();
                    fbo.dispose();

                    game.getPlatform().sharing().shareScreenshot(pmap);
                    event.cancel();
                }
            });
        }

        table.add(pane).colspan(2).align(Align.topLeft).expand().fill().row();
        table.add(backButton).fillX().expandX().expandY().height(80).padTop(20).align(Align.bottom);
        table.add(shareButton).height(80).padTop(20).padLeft(10).align(Align.bottomRight).row();
    }

    @Override
    public int getID() {
        return Screens.STATISTICS;
    }

    @Override
    public void dispose() {
        statsTable = null;
        pane = null;
        backButton = null;
    }
}
