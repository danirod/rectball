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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.ScoreActor;
import es.danirod.rectball.actors.TimerActor;
import es.danirod.rectball.actors.TimerActor.TimerCallback;
import es.danirod.rectball.dialogs.ConfirmDialog;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

import static es.danirod.rectball.Constants.VIEWPORT_WIDTH;

public class GameScreen extends AbstractScreen implements TimerCallback {

    public TimerActor timer;

    private ScoreActor scoreLabel;

    private boolean paused, started;

    public GameScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void load() {
        // Set up the board.
        String file = game.settings.isColorblind() ? "colorblind" : "normal";

        timer = new TimerActor(30, game.getSkin());
        timer.addSubscriber(this);

        scoreLabel = new ScoreActor(game.getSkin());

        super.load();
    }

    /**
     * This method will add to the stage a confirmation dialog asking the user
     * whether to end the game or not. If the user answers YES to that dialog,
     * the game will end.
     */
    private void showLeaveDialog() {
        ConfirmDialog dialog = new ConfirmDialog(game.getSkin(), "Leave game?", "Yes", "No");
        dialog.setCallback(new ConfirmDialog.ConfirmCallback() {
            @Override
            public void ok() {
                // The user wants to leave the game.
                timer.setRunning(false);
                onTimeOut();
            }

            @Override
            public void cancel() {
                setPaused(false);
            }
        });

        // FIXME: fadeIn action is not working because alpha handling in this
        // game is a mess at the moment. Fix that mess, then let the Dialog
        // use the default actions.
        dialog.show(getStage(), null);
        dialog.setPosition(
                Math.round((getStage().getWidth() - dialog.getWidth()) / 2),
                Math.round((getStage().getHeight() - dialog.getHeight()) / 2));
    }

    @Override
    public int getID() {
        return Screens.GAME;
    }

    @Override
    public void show() {
        super.show();

        // Capture Back button so that the game doesn't minimize on Android.
        Gdx.input.setCatchBackKey(true);

        // Reset data
        game.getCurrentGame().reset();
        paused = started = false;
        timer.setRunning(false);
    }

    @Override
    public void setUpInterface(Table table) {
        table.add(timer).fillX().height(50).padBottom(10).row();
        table.add(scoreLabel).width(VIEWPORT_WIDTH / 2).height(65).padBottom(60).row();
    }

    @Override
    public void hide() {
        // Restore original back button functionality.
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (timer.isRunning()) {
            game.getCurrentGame().addTime(delta);
        }

        // Pause the game when you press BACK or ESCAPE.
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (!paused) {
                setPaused(true);
            }
        }
    }

    @Override
    public void onTimeOut() {
        // Update the scoreLabel... and the record.
        game.scores.addScore(game.getCurrentGame().getScore());
        timer.setRunning(false);
        game.player.playSound(SoundCode.GAME_OVER);

        getStage().addAction(Actions.sequence(
                Actions.delay(2f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        game.setScreen(Screens.GAME_OVER);
                    }
                })
        ));
    }

    public void setPaused(boolean paused) {
        this.paused = paused;

        // If the game has just been paused, show the pause dialog.
        if (paused) {
            showLeaveDialog();
        }

        // If the game has already started, pause the timer and hide the board.
        if (started) {
            timer.setRunning(!paused);
        }
    }

    @Override
    public void pause() {
        setPaused(true);
    }
}
