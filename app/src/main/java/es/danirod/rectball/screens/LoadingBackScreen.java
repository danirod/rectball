/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2023 Dani Rodríguez.
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

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.model.Board;
import es.danirod.rectball.scene2d.game.BoardActor;

public class LoadingBackScreen extends AbstractScreen {

    private static final float FADE_SPEED = 0.15f;
    private final Board board;

    private BoardActor boardActor;

    private boolean canUpdate;

    public LoadingBackScreen(RectballGame game) {
        super(game);
        board = new Board(6);
    }

    @Override
    void setUpInterface(Table table) {
        boardActor = new BoardActor(game.getBallAtlas(), game.getAppSkin(), board);
        Rectangle bounds = game.getState().getBoardBounds();
        boardActor.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        getStage().addActor(boardActor);
    }

    @Override
    public void show() {
        super.show();

        // Hide all balls
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                boardActor.getBall(i, j).setVisible(false);

        canUpdate = false;
        getStage().addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, FADE_SPEED),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        canUpdate = true;
                    }
                })
        ));
    }
    
    @Override
    public void render(float delta) {
        super.render(delta);
        if (canUpdate && game.manager.update(1000 / 120)) {
            canUpdate = false;
            boardActor.remove();
            game.finishLoading();
        } else {
            float progress = game.manager.getProgress();
            float ballPercentage = 1.0f / (6 * 6);
            int ball = 0;
            for (int j = 0; j < 6; j++) {
                for (int i = 0; i < 6; i++) {
                    if (ball * ballPercentage < progress) {
                        boardActor.getBall(i, j).setVisible(true);
                    }
                    ball++;
                }
            }
        }
    }

    @Override
    public int getID() {
        return Screens.LOADING_BACK;
    }

    @Override
    protected void escape() {
        /* Don't do anything here */
    }
}
