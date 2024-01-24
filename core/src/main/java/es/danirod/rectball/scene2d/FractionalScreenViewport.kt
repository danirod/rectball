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

import com.badlogic.gdx.utils.viewport.ScreenViewport
import kotlin.math.max
import kotlin.math.min

class FractionalScreenViewport(
    val desiredWidth: Int,
    val desiredHeight: Int,
) : ScreenViewport() {

    override fun update(
        screenWidth: Int,
        screenHeight: Int,
        centerCamera: Boolean
    ) {
        val factor = getScalingFactor(screenWidth, screenHeight)
        unitsPerPixel = 1f / factor
        super.update(screenWidth, screenHeight, centerCamera)
    }

    private fun getScalingFactor(width: Int, height: Int) =
        min(fractional(width, desiredWidth), fractional(height, desiredHeight))

    private fun fractional(screenSize: Int, desiredSize: Int): Float {
        val normalScalingFactor = screenSize.toFloat() / desiredSize
        val roundedScalingFactor = normalScalingFactor - normalScalingFactor % 0.5f
        return max(0.5f, roundedScalingFactor)
    }
}
