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
package es.danirod.rectball.screens

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Pools
import es.danirod.rectball.RectballGame
import es.danirod.rectball.scene2d.ui.LoadingAnimation

class LoadingScreen(game: RectballGame) : AbstractScreen(game) {

    private var canUpdate = false

    private val loading = LoadingAnimation(game.ballAtlas).apply {
        syncColors()
    }

    override fun show() {
        super.show()

        stage.addActor(loading)
        loading.animate()

        canUpdate = false
        stage.addAction(Actions.sequence(
                Actions.alpha(0f),
                Actions.alpha(1f, FADE_SPEED),
                Actions.run { canUpdate = true }
        ))
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        loading.setSize(100f, 100f)
        val center = Pools.obtain(Vector2::class.java)
        viewport.getDesiredArea().getCenter(center)
        loading.setPosition(center.x, center.y, Align.center)
    }

    override fun render(delta: Float) {
        super.render(delta)
        if (canUpdate && game.manager.update(1000 / 120)) {
            canUpdate = false
            stage.addAction(Actions.sequence(
                    Actions.alpha(0f, FADE_SPEED),
                    Actions.delay(0.1f, Actions.run {
                        game.finishLoading()
                    })
            ))
        }
    }

    companion object {
        private const val FADE_SPEED = 0.05f
    }

    override fun escape() {
        /* Don't do anything here */
    }
}
