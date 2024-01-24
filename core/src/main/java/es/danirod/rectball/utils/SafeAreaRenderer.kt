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

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport

class SafeAreaRenderer(
    private val calc: SafeAreaCalculator,
    private val fractional: Viewport
): Disposable {

    private val shaper: ShapeRenderer = ShapeRenderer()

    fun render() {
        shaper.projectionMatrix = fractional.camera.combined

        shaper.begin(ShapeRenderer.ShapeType.Line)
        with(calc.getSafeArea()) { shaper.rect(x, y, width, height) }
        with(calc.getResizableArea()) { shaper.rect(x, y, width, height) }
        shaper.end()
    }

    override fun dispose() {
        shaper.dispose()
    }
}
