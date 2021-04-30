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
package es.danirod.rectball.screens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import es.danirod.rectball.RectballGame
import es.danirod.rectball.scene2d.ui.LoadingAnimation

class LoadingScreen(game: RectballGame?) : AbstractScreen(game, false) {

    private var canUpdate = false

    private lateinit var ballsTexture: Texture

    private lateinit var ballAtlas: TextureAtlas

    public override fun setUpInterface(table: Table) {
        ballsTexture = Texture("board/normal.png")
        ballAtlas = TextureAtlas().apply {
            val regions = TextureRegion.split(ballsTexture, 256, 256)
            addRegion("ball_red", regions[0][0])
            addRegion("ball_yellow", regions[0][1])
            addRegion("ball_blue", regions[1][0])
            addRegion("ball_green", regions[1][1])
            addRegion("ball_gray", regions[1][2])
        }

        val load = LoadingAnimation(ballAtlas)
        table.add(load).size(100f).align(Align.center)
        load.syncColors()
        load.animate()
    }

    override fun getID(): Int {
        return Screens.LOADING
    }

    override fun show() {
        super.show()
        canUpdate = false
        getStage().addAction(Actions.sequence(
                Actions.alpha(0f),
                Actions.alpha(1f, FADE_SPEED),
                Actions.run { canUpdate = true }
        ))
    }

    override fun render(delta: Float) {
        super.render(delta)
        if (canUpdate && game.manager.update(1000 / 120)) {
            canUpdate = false
            getStage().addAction(Actions.sequence(
                    Actions.alpha(0f, FADE_SPEED),
                    Actions.delay(0.1f, Actions.run {
                        ballAtlas.dispose()
                        ballsTexture.dispose()
                        game.finishLoading()
                    })
            ))
        }
    }

    companion object {
        private const val FADE_SPEED = 0.05f
    }
}