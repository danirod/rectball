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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.actors.board.BoardActor;
import es.danirod.rectball.model.BallColor;
import es.danirod.rectball.model.Board;

public class LoadingScreen extends AbstractScreen {

    private static final float FADE_SPEED = 0.15f;

    private static final float BALL_SPEED = 0.15f;

    private final Board board;

    private BoardActor boardActor;

    private Texture ballsTexture;

    private boolean canUpdate;
    private TextureAtlas ballAtlas;

    public LoadingScreen(RectballGame game) {
        super(game);
        board = new Board(2);
        board.getBall(0, 0).setColor(BallColor.BLUE);
        board.getBall(0, 1).setColor(BallColor.RED);
        board.getBall(1, 1).setColor(BallColor.GREEN);
        board.getBall(1, 0).setColor(BallColor.YELLOW);
    }

    @Override
    public void setUpInterface(Table table) {
        ballsTexture = new Texture("board/normal.png");
        TextureRegion[][] regions = TextureRegion.split(ballsTexture, 256, 256);
        ballAtlas = new TextureAtlas();
        ballAtlas.addRegion("ball_red", regions[0][0]);
        ballAtlas.addRegion("ball_yellow", regions[0][1]);
        ballAtlas.addRegion("ball_blue", regions[1][0]);
        ballAtlas.addRegion("ball_green", regions[1][1]);
        ballAtlas.addRegion("ball_gray", regions[1][2]);

        boardActor = new BoardActor(ballAtlas, board);
        boardActor.setColoured(true);
        table.add(boardActor).size(100).align(Align.center);
    }

    @Override
    public int getID() {
        return Screens.LOADING;
    }

    @Override
    public void show() {
        super.show();
        canUpdate = false;
        swapBalls();
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
            getStage().addAction(Actions.sequence(
                    Actions.alpha(0, FADE_SPEED),
                    Actions.delay(0.1f, Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            boardActor.remove();
                            ballAtlas.dispose();
                            ballsTexture.dispose();
                            game.finishLoading();
                        }
                    }))
            ));
        }
    }

    private void swapBalls() {
        getStage().addAction(Actions.sequence(
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        boardActor.getBall(0, 0).addAction(Actions.moveBy(50, 0, BALL_SPEED));
                        boardActor.getBall(1, 0).addAction(Actions.moveBy(0, 50, BALL_SPEED));
                        boardActor.getBall(1, 1).addAction(Actions.moveBy(-50, 0, BALL_SPEED));
                        boardActor.getBall(0, 1).addAction(Actions.moveBy(0, -50, BALL_SPEED));
                    }
                }),
                Actions.delay(BALL_SPEED * 2, Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                boardActor.getBall(0, 0).addAction(Actions.moveBy(-50, 0));
                                boardActor.getBall(1, 0).addAction(Actions.moveBy(0, -50));
                                boardActor.getBall(1, 1).addAction(Actions.moveBy(50, 0));
                                boardActor.getBall(0, 1).addAction(Actions.moveBy(0, 50));
                                BallColor bottomLeft = board.getBall(0, 0).getColor();
                                BallColor bottomRight = board.getBall(1, 0).getColor();
                                BallColor upperLeft = board.getBall(0, 1).getColor();
                                BallColor upperRight = board.getBall(1, 1).getColor();

                                board.getBall(0, 0).setColor(upperLeft);
                                board.getBall(1, 0).setColor(bottomLeft);
                                board.getBall(1, 1).setColor(bottomRight);
                                board.getBall(0, 1).setColor(upperRight);

                                swapBalls();
                            }
                        }))
        ));
    }
}
