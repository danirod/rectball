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
package es.danirod.rectball.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;
import es.danirod.rectball.scene2d.FractionalScreenViewport;
import es.danirod.rectball.utils.SafeAreaCalculator;
import es.danirod.rectball.utils.SafeAreaRenderer;

import static es.danirod.rectball.Constants.STAGE_PADDING;
import static es.danirod.rectball.Constants.VIEWPORT_WIDTH;

/**
 * This is the base screen every screen has to inherit. It contains common
 * boilerplate code such as a stage and a table widget for add child widgets.
 *
 * @author danirod
 */
public abstract class AbstractScreen implements Screen {
    protected final RectballGame game;
    protected final Stage stage;
    protected final Table table;
    protected final FractionalScreenViewport viewport;
    protected final SafeAreaCalculator safeAreaCalculator;
    protected final SafeAreaRenderer safeAreaRenderer;

    private boolean renderDebug = false;

    public AbstractScreen(RectballGame game) {
        this.game = game;
        this.viewport = new FractionalScreenViewport(480, 640);
        this.table = new Table();
        this.stage = new Stage(viewport, game.getBatch());
        this.safeAreaCalculator = new SafeAreaCalculator(game, stage, viewport);
        this.safeAreaRenderer = new SafeAreaRenderer(safeAreaCalculator, viewport);

        table.setFillParent(true);
        updateTablePadding();
        stage.addActor(table);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        updateTablePadding();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            renderDebug = !renderDebug;
        }
        Gdx.gl.glClearColor(RectballGame.BG_COLOR.r, RectballGame.BG_COLOR.g, RectballGame.BG_COLOR.b, RectballGame.BG_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        if (renderDebug && safeAreaRenderer != null) {
            safeAreaRenderer.render();
        }
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
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setCatchKey(Input.Keys.ESCAPE, true);
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(new BackButtonInputProcessor());
        multi.addProcessor(stage);
        Gdx.input.setInputProcessor(multi);
    }

    @Override
    public void hide() {
        table.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
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
