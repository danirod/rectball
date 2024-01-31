/* This file is part of Rectball
 * Copyright (C) 2015-2024  Dani Rodríguez
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.screens;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.model.Board;
import es.danirod.rectball.model.GameState;
import es.danirod.rectball.scene2d.game.BoardActor;

public class LoadingBackScreen extends AbstractScreen {

    private static final float FADE_SPEED = 0.15f;

    private BoardActor boardActor;

    private boolean canUpdate;

    private final GameState state;

    public LoadingBackScreen(RectballGame game) {
        super(game);
        state = new GameState();
    }

    @Override
    public void show() {
        super.show();

        table.setFillParent(false);

        boardActor = new BoardActor(game.getBallAtlas(), game.getAppSkin(), state.getBoard());
        Rectangle bounds = state.getBoardBounds();
        boardActor.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);

        Value boardValue = new Value() {
            @Override
            public float get(Actor context) {
                Rectangle safeArea = viewport.getSafeArea();
                float idealWidth = MathUtils.clamp(safeArea.width - 80f, 440f, 640f);
                return Math.min(idealWidth, safeArea.height - 180f);
            }
        };

        table.add(boardActor).growX().width(boardValue).height(boardValue).padTop(140f)
                .expand().align(Align.center).row();

        // Hide all balls
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                boardActor.getBall(i, j).setVisible(false);

        canUpdate = false;
        stage.addAction(Actions.sequence(
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
    public void resize(int width, int height) {
        super.resize(width, height);

        Rectangle centerArea = viewport.getSafeArea();
        table.setPosition(centerArea.x, centerArea.y);
        table.setSize(centerArea.width, centerArea.height);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (canUpdate && game.manager.update(1000 / 120)) {
            canUpdate = false;
            stage.addAction(Actions.sequence(
                    Actions.run(() -> {
                        boardActor.remove();
                        game.finishLoading();
                    })
            ));
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
    protected void escape() {
        /* Don't do anything here */
    }
}
