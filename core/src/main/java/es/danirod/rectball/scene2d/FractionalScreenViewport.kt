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
package es.danirod.rectball.scene2d

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.viewport.ScreenViewport
import es.danirod.rectball.RectballGame
import kotlin.math.max
import kotlin.math.min

class FractionalScreenViewport(
    private val game: RectballGame,
    val desiredWidth: Int,
    val desiredHeight: Int,
) : ScreenViewport() {

    var scalingFactor = 0f
        private set

    /**
     * Returns a rectangle that maps to the entire window area, scaled down
     * using the scaling factor currently in use. Ignores insets and other
     * safe areas.
     */
    fun getWindowArea(): Rectangle = Rectangle(0f, 0f, worldWidth, worldHeight)

    /**
     * Returns a rectangle that maps to the entire window area minus the area
     * that has been reserved for the insets, which means that things outside
     * this area may be covered by user interface elements.
     */
    fun getSafeArea() = getWindowArea().apply {
        val paddingLeft = game.marginLeft * unitsPerPixel
        val paddingBottom = game.marginBottom * unitsPerPixel
        val paddingRight = game.marginRight * unitsPerPixel
        val paddingTop = game.marginTop * unitsPerPixel
        x += paddingLeft
        y += paddingBottom
        width -= (paddingLeft + paddingRight)
        height -= (paddingBottom + paddingTop)
    }

    fun getDesiredArea() = getWindowArea().let { window ->
        val safeArea = getSafeArea()
        val width = min(desiredWidth.toFloat(), safeArea.width)
        val height = min(desiredHeight.toFloat(), safeArea.height)
        val x = (window.width - width) / 2
        val y = (window.height - height) / 2
        Rectangle(x, y, width, height)
    }

    override fun update(
        screenWidth: Int,
        screenHeight: Int,
        centerCamera: Boolean
    ) {
        scalingFactor = getScalingFactor(screenWidth, screenHeight)
        unitsPerPixel = 1f / scalingFactor
        super.update(screenWidth, screenHeight, centerCamera)
    }

    private fun getScalingFactor(width: Int, height: Int) =
        min(fractional(width, desiredWidth, game.marginLeft, game.marginRight),
            fractional(height, desiredHeight, game.marginBottom, game.marginTop))

    private fun fractional(screenSize: Int, desiredSize: Int, paddingStart: Float, paddingEnd: Float): Float {
        var scalingFactor = 0.5f
        while (true) {
            val nextScalingFactor = scalingFactor + 0.5f

            // Check if the next scaling factor will fit.
            val expectedPaddingStart = paddingStart / nextScalingFactor
            val expectedPaddingEnd = paddingEnd / nextScalingFactor
            val screenSizeWithSafeArea = screenSize - expectedPaddingStart - expectedPaddingStart
            val minimumSize = desiredSize * nextScalingFactor
            if (minimumSize > screenSizeWithSafeArea) {
                // Doesn't fit already
                return scalingFactor
            } else {
                scalingFactor = nextScalingFactor
            }
        }
    }
}
