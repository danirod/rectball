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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import es.danirod.rectball.Constants;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;
import es.danirod.rectball.scene2d.listeners.ScreenJumper;
import es.danirod.rectball.scene2d.ui.ConfirmDialog;
import es.danirod.rectball.scene2d.ui.MessageDialog;

import static es.danirod.rectball.Constants.STAGE_PADDING;

public class MainMenuScreen extends AbstractScreen {

    public MainMenuScreen(RectballGame game) {
        super(game, false);
    }

    @Override
    public void setUpInterface(Table table) {
        // Build the actors.
        Image title = new Image(game.getAtlas().findRegion("Logo"));
        title.setScaling(Scaling.fit);
        ImageButton play = new ImageButton(game.getSkin(), "greenPlay");
        ImageButton settings = new ImageButton(game.getSkin(), "settings");
        ImageButton statistics = new ImageButton(game.getSkin(), "charts");
        ImageButton about = new ImageButton(game.getSkin(), "info");

        // Add the title button and the clickable about text.
        table.add(title).pad(40).padBottom(60).row();

        // Add the big green play button.
        table.add(play).fillX().height(150).row();

        // Add the table layout for all the extra buttons.
        Table extraButtons = new Table();
        extraButtons.defaults().expandX().fillX();
        extraButtons.add(settings);
        extraButtons.add(statistics).pad(0, 20, 0, 20);
        extraButtons.add(about).row();
        table.add(extraButtons).fillX().height(150).row();

        // Then add the capture listeners for the buttons.
        play.addListener(new ScreenJumper(game, Screens.GAME));
        settings.addListener(new ScreenJumper(game, Screens.SETTINGS));
        statistics.addListener(new ScreenJumper(game, Screens.STATISTICS));
        about.addListener(new ScreenJumper(game, Screens.ABOUT));

        // Add a quit button.
        ImageButton quit = new ImageButton(game.getSkin(), "quit");
        quit.setSize(50, 50);
        quit.setPosition(getStage().getViewport().getWorldWidth() - 60, getStage().getViewport().getWorldHeight() - 60);
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        getStage().addActor(quit);
    }

    @Override
    public void show() {
        super.show();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(getStage());
        multiplexer.addProcessor(new MainMenuInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);

        // On first run, show the tutorial.
        if (!game.getPlatform().preferences().getBoolean("tutorialAsked")) {
            askTutorial().show(getStage());
        }
    }

    @Override
    public int getID() {
        return Screens.MAIN_MENU;
    }

    private ConfirmDialog askTutorial() {
        String message = game.getLocale().get("main.askTutorial");
        String yes = game.getLocale().get("core.yes");
        String no = game.getLocale().get("core.no");
        ConfirmDialog dialog = new ConfirmDialog(game.getSkin(), message, yes, no);
        dialog.setCallback(new ConfirmDialog.ConfirmCallback() {
            @Override
            public void ok() {
                game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
                game.pushScreen(Screens.TUTORIAL);
            }

            @Override
            public void cancel() {
                game.player.playSound(SoundPlayer.SoundCode.FAIL);
                game.getPlatform().preferences().putBoolean("tutorialAsked", true);
                game.getPlatform().preferences().flush();
                tutorialCancel().show(getStage());
            }
        });
        return dialog;
    }

    private MessageDialog tutorialCancel() {
        String message = game.getLocale().get("main.cancelTutorial");
        MessageDialog dialog = new MessageDialog(game.getSkin(), message);
        dialog.setCallback(new MessageDialog.MessageCallback() {
            @Override
            public void dismiss() {
                game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
            }
        });
        return dialog;
    }

    private class MainMenuInputProcessor extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
                Gdx.app.exit();
                return true;
            }
            return false;
        }
    }
}
