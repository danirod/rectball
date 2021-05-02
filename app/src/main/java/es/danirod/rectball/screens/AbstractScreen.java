/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2017 Dani Rodríguez.
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

import static es.danirod.rectball.Constants.STAGE_PADDING;
import static es.danirod.rectball.Constants.VIEWPORT_WIDTH;

/**
 * This is the base screen every screen has to inherit. It contains common
 * boilerplate code such as a stage and a table widget for add child widgets.
 *
 * @author danirod
 */
public abstract class AbstractScreen implements Screen {

    final RectballGame game;

    /**
     * Whether the default BACK/ESCAPE button handler should be used or not.
     *
     * @since 0.3.0
     */
    final boolean handleBack;

    /**
     * Common stage.
     */
    protected Stage stage = null;

    /**
     * Common table.
     */
    protected Table table = null;

    AbstractScreen(RectballGame game) {
        this(game, true);
    }

    AbstractScreen(RectballGame game, boolean handleBack) {
        this.game = game;
        this.handleBack = handleBack;
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(buildViewport());
        stage.getViewport().update(width, height, true);
    }

    public void load() {

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
        Gdx.gl.glClearColor(0.3f, 0.4f, 0.4f, 1f);
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

    @Override
    public void show() {
        if (stage == null) {
            stage = new Stage(buildViewport(), game.getBatch());
        }

        if (table == null) {
            table = new Table();
            table.setFillParent(true);
            table.pad(STAGE_PADDING);
            stage.addActor(table);
        } else {
            table.clear();
        }
        setUpInterface(table);

        Gdx.input.setCatchBackKey(true);
        if (handleBack) {
            InputMultiplexer multiplexer = new InputMultiplexer();
            multiplexer.addProcessor(new BackButtonInputProcessor(game));
            multiplexer.addProcessor(stage);
            Gdx.input.setInputProcessor(multiplexer);
        } else {
            Gdx.input.setInputProcessor(stage);
        }
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

    public abstract int getID();

    Stage getStage() {
        return stage;
    }

    static class BackButtonInputProcessor extends InputAdapter {

        private final RectballGame game;

        public BackButtonInputProcessor(RectballGame game) {
            this.game = game;
        }

        @Override
        public boolean keyDown(int keycode) {
            return keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK;
        }

        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
                game.player.playSound(SoundPlayer.SoundCode.FAIL);
                game.popScreen();
                return true;
            }
            return false;
        }
    }
}
