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
package es.danirod.rectball.settings

import es.danirod.rectball.model.BallColor

data class LocalStatistics(
    val hints: Long = 0L,
    val gems: Long = 0L,
    val combinations: Long = 0L,
    val perfects: Long = 0L,
    val byColor: Map<String, Long> = mapOf(),
    val bySize: Map<String, Long> = mapOf(),
) {

    fun incrementHint() = copy(hints = hints + 1)

    fun incrementCombinations(width: Long, height: Long, color: BallColor, isPerfect: Boolean) =
        copy(
            gems = this.gems + (width * height),
            combinations = combinations + 1,
            perfects = perfects + (if (isPerfect) 1 else 0),
            byColor = mergeColorMap(color),
            bySize = mergeSizeMap(width, height),
        )

    private fun mergeColorMap(color: BallColor): Map<String, Long> {
        val key = color.name.lowercase()
        val oldValue: Long = byColor[key] ?: 0
        return byColor.toMutableMap().apply { this[key] = oldValue + 1L }
    }

    private fun mergeSizeMap(width: Long, height: Long): Map<String, Long> {
        val key = listOf(width, height).sorted().joinToString("x")
        val oldValue: Long = bySize[key] ?: 0
        return bySize.toMutableMap().apply { this[key] = oldValue + 1L }
    }
}
