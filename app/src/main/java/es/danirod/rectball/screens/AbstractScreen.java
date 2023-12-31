/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2023 Dani Rodríguez.
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

import static es.danirod.rectball.Constants.STAGE_PADDING;
import static es.danirod.rectball.Constants.VIEWPORT_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;

/**
 * This is the base screen every screen has to inherit. It contains common
 * boilerplate code such as a stage and a table widget for add child widgets.
 *
 * @author danirod
 */
public abstract class AbstractScreen implements Screen {

    final RectballGame game;

    /**
     * Common stage.
     */
    protected Stage stage = null;

    /**
     * Common table.
     */
    protected Table table = null;

    public AbstractScreen(RectballGame game) {
        this.game = game;
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(buildViewport());
        stage.getViewport().update(width, height, true);
    }

    /**
     * This method sets up the visual layout for this screen. Child classes
     * have to override this method and add to the provided table the widgets
     * they want to show in the screen.
     *
     * @param table table that has been assigned to this screen.
     */
    abstract void setUpInterface(Table table);

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(RectballGame.BG_COLOR.r, RectballGame.BG_COLOR.g, RectballGame.BG_COLOR.b, RectballGame.BG_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    Viewport buildViewport() {
        boolean landscape = Gdx.graphics.getWidth() > Gdx.graphics.getHeight();
        float ar = landscape ?
                (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight() :
                (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        float width = (float) VIEWPORT_WIDTH;
        float height = landscape ? width : width * ar;
        return new FitViewport(width, height);
    }

    public void updateTablePadding() {
        float pixelsPerViewport = (float) Gdx.graphics.getWidth() / VIEWPORT_WIDTH;
        float paddingTop = STAGE_PADDING + game.getMarginTop() / pixelsPerViewport;
        float paddingBottom = STAGE_PADDING + game.getMarginBottom() / pixelsPerViewport;
        float paddingLeft = STAGE_PADDING + game.getMarginLeft() / pixelsPerViewport;
        float paddingRight = STAGE_PADDING + game.getMarginRight() / pixelsPerViewport;
        table.pad(paddingTop, paddingLeft, paddingBottom, paddingRight);
    }

    @Override
    public void show() {
        if (stage == null) {
            stage = new Stage(buildViewport(), game.getBatch());
        }

        if (table != null) {
            table.remove();
        }

        table = new Table();
        table.setFillParent(true);
        updateTablePadding();
        stage.addActor(table);
        setUpInterface(table);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setCatchKey(Input.Keys.ESCAPE, true);
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(new BackButtonInputProcessor());
        multi.addProcessor(stage);
        Gdx.input.setInputProcessor(multi);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
        table = null;
    }

    protected void escape() {
        game.player.playSound(SoundPlayer.SoundCode.FAIL);
        game.popScreen();
    }

    private class BackButtonInputProcessor extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {
            return keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK;
        }

        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
                escape();
                return true;
            }
            return false;
        }
    }
}
