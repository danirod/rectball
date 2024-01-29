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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;
import es.danirod.rectball.scene2d.FractionalScreenViewport;

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

    private final ShapeRenderer rendererDebug;
    private boolean renderDebug = false;

    public AbstractScreen(RectballGame game) {
        this.game = game;
        this.viewport = new FractionalScreenViewport(game, 480, 640);
        this.table = new Table();
        this.stage = new Stage(viewport, game.getBatch());
        this.rendererDebug = new ShapeRenderer();

        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

        if (renderDebug) {
            rendererDebug.setProjectionMatrix(viewport.getCamera().combined);
            rendererDebug.begin(ShapeRenderer.ShapeType.Line);

            /* Render window: this line should not be visible. */
            Rectangle windowBounds = viewport.getWindowArea();
            rendererDebug.setColor(Color.BLUE);
            rendererDebug.rect(windowBounds.x, windowBounds.y, windowBounds.width, windowBounds.height);

            /* Safe area window: this line should visible shall the screen have insets. */
            Rectangle insetBounds = viewport.getSafeArea();
            rendererDebug.setColor(Color.RED);
            rendererDebug.rect(insetBounds.x, insetBounds.y, insetBounds.width, insetBounds.height);

            /* Desired area: the actual viewport bounds. */
            Rectangle desiredBounds = viewport.getDesiredArea();
            rendererDebug.setColor(Color.GREEN);
            rendererDebug.rect(desiredBounds.x, desiredBounds.y, desiredBounds.width, desiredBounds.height);

            rendererDebug.end();
        }
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
        rendererDebug.dispose();
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
