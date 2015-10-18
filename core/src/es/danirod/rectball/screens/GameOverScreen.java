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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.Constants;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.listeners.ScreenJumper;
import es.danirod.rectball.listeners.ScreenPopper;
import es.danirod.rectball.utils.SoundPlayer;

public class GameOverScreen extends AbstractScreen {

    public GameOverScreen(RectballGame game) {
        super(game, true);
    }

    @Override
    public void setUpInterface(Table table) {
        // GAME OVER!
        table.add(new Label(game.getLocale().get("game.gameover"), game.getSkin())).row();

        // Set up the label data.
        String lastScore = Integer.toString(game.getState().getScore());
        while (lastScore.length() < 4) lastScore = "0" + lastScore;
        String aliveTime = Integer.toString(Math.round(game.getState().getTime()));
        String highScore = Long.toString(game.scores.getHighestScore());

        // Scores table
        Table scoresTable = new Table();

        // Last score.
        Label highScoreLabel = new Label(lastScore, game.getSkin(), "monospace");
        highScoreLabel.setFontScale(10f);
        highScoreLabel.setAlignment(Align.bottom);
        scoresTable.add(highScoreLabel).expandX().height(120).colspan(2).align(Align.center).row();

        // Alive time.
        Drawable clock = game.getSkin().newDrawable("iconClock");
        scoresTable.add(new Image(clock)).size(60).expandX().align(Align.right).padRight(20);
        scoresTable.add(new Label(aliveTime, game.getSkin())).expandX().align(Align.left).padLeft(20).row();

        // High score.
        Drawable crown = game.getSkin().newDrawable("iconCrown");
        scoresTable.add(new Image(crown)).size(60).expandX().align(Align.right).padRight(20);
        scoresTable.add(new Label(highScore, game.getSkin())).expandX().align(Align.left).padLeft(20).row();

        Table buttonRow = new Table();
        buttonRow.defaults().fillX().expandX().space(20);

        // Add replay button.
        ImageButton replay = new ImageButton(game.getSkin(), "repeat");
        replay.addListener(new ScreenPopper(game));
        buttonRow.add(replay);

        // Add share button
        ImageButton share = new ImageButton(game.getSkin(), "share");
        share.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.player.playSound(SoundPlayer.SoundCode.SELECT);
                Pixmap screenshot = game.requestScreenshot();
                game.getPlatform().sharing().shareGameOverScreenshot(screenshot, game.getState().getScore(), Math.round(game.getState().getTime()));
                event.cancel();
            }
        });
        buttonRow.add(share);

        // Add menu button.
        ImageButton menu = new ImageButton(game.getSkin(), "house");
        menu.addListener(new ScreenPopper(game, true));
        buttonRow.add(menu);

        table.add(scoresTable).fillX().expand().row();
        table.add(buttonRow).fillX().expandX().row();

        // Now animate the stage to make it fall.
        getStage().addAction(Actions.sequence(
                Actions.moveBy(0, Constants.VIEWPORT_HEIGHT),
                Actions.moveBy(0, -Constants.VIEWPORT_HEIGHT, 0.25f)
        ));
    }

    @Override
    public void show() {
        super.show();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(getStage());
        multiplexer.addProcessor(new GameOverInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public int getID() {
        return Screens.GAME_OVER;
    }

    private class GameOverInputProcessor extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
                game.player.playSound(SoundPlayer.SoundCode.FAIL);
                game.clearStack();
                return true;
            }
            return false;
        }

    }

}
