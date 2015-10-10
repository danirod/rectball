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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.Value;
import es.danirod.rectball.settings.ScoreIO;
import es.danirod.rectball.statistics.Statistics;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

public class GameOverScreen extends AbstractScreen {

    private Label aliveTime;

    private Label highScore;
    private Value score;

    public GameOverScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        Label gameOver = new Label("GAME OVER", game.getSkin());
        aliveTime = new Label("Alive: " + (int) game.aliveTime + " s", game.getSkin());
        highScore = new Label("High Score: " + game.scores.getHighestScore(), game.getSkin());

        Texture sheet = game.manager.get("scores.png", Texture.class);
        score = new Value(sheet, 4, game.scores.getLastScore());
        table.add(score).pad(40).colspan(2).fillX().height(200).row();

        table.add(gameOver).pad(40).colspan(2).expandX().expandY().align(Align.center).row();
        table.add(aliveTime).pad(20).colspan(2).expandX().expandY().align(Align.center).row();
        table.add(highScore).pad(20).colspan(2).expandX().expandY().align(Align.center).row();

        TextButton replay = new TextButton("Replay", game.getSkin());
        TextButton menu = new TextButton("Menu", game.getSkin());
        table.add(replay).pad(40).expandX().height(100);
        table.add(menu).pad(40).expandX().height(100).row();

        replay.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.player.playSound(SoundCode.SUCCESS);
                game.setScreen(Screens.GAME);
            }
        });
        menu.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.player.playSound(SoundCode.FAIL);
                game.setScreen(Screens.MAIN_MENU);
            }
        });
    }

    @Override
    public void show() {
        super.show();

        aliveTime.setText("Alive: " + (int) game.aliveTime + " s");
        highScore.setText("High Score: " + game.scores.getHighestScore());
        score.setValue(game.scores.getLastScore());

        game.statistics.getTotalData().incrementValue("games");
        game.statistics.getTotalData().incrementValue("time", (int) game.aliveTime);
        ScoreIO.save(game.scores);
        Statistics.saveStats(game.statistics);
    }

    @Override
    public int getID() {
        return Screens.GAME_OVER;
    }
}
