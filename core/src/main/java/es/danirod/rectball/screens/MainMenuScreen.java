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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;
import es.danirod.rectball.scene2d.game.BackgroundActor;
import es.danirod.rectball.scene2d.ui.ConfirmDialog;
import es.danirod.rectball.scene2d.ui.MainMenuGrid;
import es.danirod.rectball.scene2d.ui.MessageDialog;

public class MainMenuScreen extends AbstractScreen {

    private final MainMenuGrid grid;

    private final Label version;

    private final ConfirmDialog askTutorial;

    private final ConfirmDialog askInputMethod;

    private final MessageDialog closeDialog;

    private final BackgroundActor background;

    public MainMenuScreen(RectballGame game) {
        super(game);
        this.grid = new MainMenuGrid(game);
        this.version = new Label(game.getVersion(), game.getAppSkin(), "small", "white");
        this.askTutorial = askTutorial("main.ask_tutorial");
        this.askInputMethod = askTutorial("main.ask_input_method");
        this.closeDialog = tutorialCancel();
        this.background = new BackgroundActor(game.getBallAtlas());
    }

    @Override
    public void show() {
        super.show();

        background.getColor().a = 0.1f;

        stage.addActor(background);
        stage.addActor(grid);
        stage.addActor(version);

        // On first run, show the tutorial.
        if (!game.getSettings().getTutorialAsked()) {
            askTutorial.show(stage);
        } else if (!game.getSettings().getNewInputMethodAsked()) {
            askInputMethod.show(stage);
        }
    }

    @Override
    protected void escape() {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        // Reposition the grid
        background.setSize(stage.getWidth() + 64f, stage.getHeight() + 64f);
        background.setPosition(stage.getWidth() / 2, stage.getHeight() / 2, Align.center);
        grid.setSize(400f, grid.getPrefHeight());
        center(grid);
        center(askInputMethod);
        center(askTutorial);
        center(closeDialog);
        Rectangle safeArea = viewport.getSafeArea();
        version.setPosition(safeArea.x + 10, safeArea.y + 10);
    }

    private void center(Actor actor) {
        if (actor.getStage() != null) {
            Vector2 center = Pools.obtain(Vector2.class);
            viewport.getDesiredArea().getCenter(center);
            actor.setPosition(center.x, center.y, Align.center);
            Pools.free(center);
        }
    }

    private ConfirmDialog askTutorial(String resId) {
        String message = game.getLocale().get(resId);
        String yes = game.getLocale().get("core.yes");
        String no = game.getLocale().get("core.no");
        ConfirmDialog dialog = new ConfirmDialog(game.getAppSkin(), message, yes, no);
        dialog.setCallback(new ConfirmDialog.ConfirmCallback() {
            @Override
            public void ok() {
                game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
                game.pushScreen(new TutorialScreen(game));
            }

            @Override
            public void cancel() {
                game.player.playSound(SoundPlayer.SoundCode.FAIL);
                game.getSettings().setTutorialAsked(true);
                game.getSettings().setNewInputMethodAsked(true);
                closeDialog.show(stage);
            }
        });
        return dialog;
    }

    private MessageDialog tutorialCancel() {
        String message = game.getLocale().get("main.dismiss_tutorial");
        MessageDialog dialog = new MessageDialog(game.getAppSkin(), message);
        dialog.setCallback(new MessageDialog.MessageCallback() {
            @Override
            public void dismiss() {
                game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
            }
        });
        return dialog;
    }
}
