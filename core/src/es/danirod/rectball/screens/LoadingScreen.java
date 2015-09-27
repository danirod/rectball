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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import es.danirod.rectball.RectballGame;

public class LoadingScreen extends AbstractScreen {

    private ShapeRenderer shaper;

    public LoadingScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void show() {
        shaper = new ShapeRenderer();
    }

    @Override
    public void hide() {
        shaper.dispose();
    }

    @Override
    public int getID() {
        return Screens.LOADING;
    }

    @Override
    public void render(float delta) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        float barWidth = width * 0.8f;
        float barHeight = barWidth * 0.1f;
        float barX = (width - barWidth) / 2;
        float barY = (height - barHeight) / 2;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.manager.update()) {
            // Finish loading.
            game.setScreen(Screens.MAIN_MENU);
        } else {
            float progress = game.manager.getProgress();
            shaper.setAutoShapeType(true);
            shaper.begin();
            shaper.set(ShapeRenderer.ShapeType.Line);
            shaper.rect(barX - 5, barY - 5, barWidth + 10, barHeight + 10);
            shaper.set(ShapeRenderer.ShapeType.Filled);
            shaper.rect(barX, barY, barWidth * progress, barHeight);
            shaper.end();
        }
    }
}
