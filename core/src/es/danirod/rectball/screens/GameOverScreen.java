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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.listeners.ScreenJumper;
import es.danirod.rectball.settings.ScoreIO;
import es.danirod.rectball.statistics.Statistics;
import es.danirod.rectball.utils.SoundPlayer.SoundCode;

public class GameOverScreen extends AbstractScreen {

    /**
     * The label that displays how many seconds the last game has lasted.
     * I need to save a reference to this label because every time the player
     * enter this screen, the value will probably change.
     */
    private Label aliveTime;

    /**
     * The highest lastScore the player has ever had on Rectball. I need to save
     * a reference to this label in case the value changes when the user
     * enters this screen after losing a game.
     */
    private Label highScore;

    /**
     * The lastScore of the last game. I need to save a reference to this label
     * because every time the player enters this screen, the value will
     * probably change.
     */
    private Label lastScore;

    public GameOverScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        // These labels have to be updated when show() is called because every
        // time the player enters this screen their values might have changed.
        lastScore = new Label("", game.getSkin(), "monospace");
        highScore = new Label("", game.getSkin());
        aliveTime = new Label("", game.getSkin());

        lastScore.setFontScale(10f);
        lastScore.setAlignment(Align.center);

        // Icon drawables.
        Drawable clockDrawable = game.getSkin().newDrawable("iconClock");
        Drawable crownDrawable = game.getSkin().newDrawable("iconCrown");

        // Populate the table.
        table.add(new Label("GAME OVER", game.getSkin())).colspan(2).expandX().row();
        table.add(lastScore).fillX().expand().colspan(2).align(Align.center).row();
        table.add(new Image(clockDrawable)).size(80).expandX().align(Align.right).padRight(20);
        table.add(aliveTime).expandX().align(Align.left).padLeft(20).row();
        table.add(new Image(crownDrawable)).size(80).expandX().align(Align.right).padRight(20);
        table.add(highScore).expandX().align(Align.left).padLeft(20).row();

        TextButton replay = new TextButton("Replay", game.getSkin());
        TextButton menu = new TextButton("Menu", game.getSkin());
        table.add(replay).colspan(2).fillX().height(100).padTop(30).row();
        table.add(menu).colspan(2).fillX().height(100).padTop(30).row();

        replay.addCaptureListener(new ScreenJumper(game, Screens.GAME));
        menu.addCaptureListener(new ScreenJumper(game, Screens.MAIN_MENU));
    }

    @Override
    public void show() {
        super.show();

        int aliveSeconds = (int) game.getCurrentGame().getTime();

        aliveTime.setText(Integer.toString(aliveSeconds) + " s");
        highScore.setText(Long.toString(game.scores.getHighestScore()));

        // Set the last lastScore.
        String stringScore = Integer.toString(game.getCurrentGame().getScore());
        while (stringScore.length() < 4) {
            stringScore = "0" + stringScore;
        }
        lastScore.setText(stringScore);

        game.statistics.getTotalData().incrementValue("games");
        game.statistics.getTotalData().incrementValue("time", aliveSeconds);

        ScoreIO.save(game.scores);
        Statistics.saveStats(game.statistics);
    }

    @Override
    public int getID() {
        return Screens.GAME_OVER;
    }
}
