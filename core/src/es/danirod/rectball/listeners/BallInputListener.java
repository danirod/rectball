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
package es.danirod.rectball.listeners;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import es.danirod.rectball.actors.Ball;
import es.danirod.rectball.actors.Board;

/**
 * Created by danirod on 29/8/15.
 */
public class BallInputListener extends InputListener {

    private final Board board;

    private final Ball ball;

    public BallInputListener(Ball ball, Board board) {
        this.ball = ball;
        this.board = board;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        ball.setSelected(!ball.isSelected());
        if (ball.isSelected()) {
            ScaleToAction scale = Actions.scaleTo(0.8f, 0.8f, 0.1f);
            ball.addAction(Actions.scaleTo(0.8f, 0.8f, 0.1f));
        } else {
            ball.addAction(Actions.scaleTo(1f, 1f, 0.1f));
        }
        board.ballSelected(ball);
        return true;
    }

}
