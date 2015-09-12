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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import es.danirod.rectball.actors.Ball;
import es.danirod.rectball.model.BallColor;

/**
 * Created by danirod on 6/9/15.
 */
public class DebugBallListener extends InputListener {

    private Ball ball;

    private int pos = 0;

    private static BallColor[] COLORS = {
            BallColor.BLUE,
            BallColor.RED,
            BallColor.GREEN,
            BallColor.YELLOW
    };

    public DebugBallListener(Ball ball) {
        this.ball = ball;
        ball.setBallColor(COLORS[pos]);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        pos = (pos + 1) % COLORS.length;
        ball.setBallColor(COLORS[pos]);
        return true;
    }
}
