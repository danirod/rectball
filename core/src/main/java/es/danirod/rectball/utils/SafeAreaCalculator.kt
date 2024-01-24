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
package es.danirod.rectball.utils

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Stage
import es.danirod.rectball.RectballGame
import es.danirod.rectball.scene2d.FractionalScreenViewport

class SafeAreaCalculator(
    private val game: RectballGame,
    private val stage: Stage,
    private val fractional: FractionalScreenViewport
) {

    fun getResizableArea(): Rectangle {
        val width = fractional.desiredWidth.toFloat()
        val height = fractional.desiredHeight.toFloat()
        return getSafeArea().let { safe ->
            val x = (safe.width - width) / 2
            val y = (safe.height - height) / 2
            Rectangle(x, y, width, height)
        }
    }

    fun getSafeArea(): Rectangle {
        val pixelsPerViewport = fractional.unitsPerPixel
        val paddingTop = game.marginTop * pixelsPerViewport
        val paddingBottom = game.marginBottom * pixelsPerViewport
        val paddingLeft = game.marginLeft * pixelsPerViewport
        val paddingRight = game.marginRight * pixelsPerViewport

        return Rectangle(
            paddingLeft, paddingBottom,
            stage.width - paddingRight - paddingLeft,
            stage.height - paddingTop - paddingBottom
        )
    }
}
