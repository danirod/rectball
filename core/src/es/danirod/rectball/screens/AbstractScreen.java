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

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;
import es.danirod.rectball.platform.AnalyticEvent;

import static es.danirod.rectball.Constants.*;

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
    private final boolean handleBack;

    /**
     * Common stage.
     */
    private Stage stage;

    /**
     * Common table.
     */
    private Table table;

    AbstractScreen(RectballGame game) {
        this(game, true);
    }

    AbstractScreen(RectballGame game, boolean handleBack) {
        this.game = game;
        this.handleBack = handleBack;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public void load() {
        Viewport viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        stage = new Stage(viewport);
        table = new Table();
        table.setFillParent(true);
        table.pad(STAGE_PADDING);
        stage.addActor(table);
    }

    /**
     * This method sets up the visual layout for this screen. Child classes
     * have to override this method and add to the provided table the widgets
     * they want to show in the screen.
     *
     * @param table table that has been assigned to this screen.
     */
    void setUpInterface(Table table) {
        // FIXME: This method should be abstract.
        // However, I cannot make it abstract until I refactor every class
        // or errors may happen.
    }

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

    @Override
    public void show() {
        // Dispatch an analytic event.
        AnalyticEvent event = new AnalyticEvent();
        event.setUserData("action", AnalyticEvent.ACTION_SCREEN);
        event.setUserData("screen", getClass().getCanonicalName());
        game.getPlatform().analytic().sendEvent(event);

        table.clear();
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
        stage.dispose();
    }

    public abstract int getID();

    Stage getStage() {
        return stage;
    }

    private class BackButtonInputProcessor extends InputAdapter {

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
