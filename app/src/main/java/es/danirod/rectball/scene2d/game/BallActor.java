/*
 * This file is part of Rectball
 * Copyright (C) 2015 Dani Rodríguez
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

package es.danirod.rectball.scene2d.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import es.danirod.rectball.model.Ball;
import es.danirod.rectball.model.BallColor;

import java.util.HashMap;
import java.util.Map;

public class BallActor extends Image {

    private final Ball ball;
    private final BoardActor board;
    private final TextureAtlas atlas;
    private boolean selected;

    private TextureRegionDrawable grayDrawable;
    Map<BallColor, TextureRegionDrawable> colorDrawables;

    public BallActor(BoardActor board, Ball ball, TextureAtlas atlas) {
        this.board = board;
        this.ball = ball;
        this.atlas = atlas;
        setScaling(Scaling.fit);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setSelected(!isSelected());
                return true;
            }
        });

        grayDrawable = new TextureRegionDrawable(atlas.findRegion("ball_gray"));
        colorDrawables = new HashMap<>();
        for (BallColor color : BallColor.values()) {
            colorDrawables.put(color, new TextureRegionDrawable(atlas.findRegion("ball_" + color.toString().toLowerCase())));
        }
    }

    public Ball getBall() {
        return ball;
    }

    @Override
    public void act(float delta) {
        if (board.isColoured()) {
            setDrawable(colorDrawables.get(ball.getColor()));
        } else {
            setDrawable(grayDrawable);
        }
        super.act(delta);
    }

    @Override
    protected void sizeChanged() {
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    private boolean isSelected() {
        return selected;
    }

    private void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            board.select(ball.getX(), ball.getY());
        } else {
            board.unselect(ball.getX(), ball.getY());
        }
    }

    void quietlyUnselect() {
        this.selected = false;
    }
}
