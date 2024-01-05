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

import es.danirod.rectball.model.GameState

class StatSerializer(private val state: GameState, private val appStats: AppStatistics) {

    private val localStats = state.localStatistics

    fun combine() {
        combineNumerical()
        combineColor()
        combineSize()
    }

    private fun combineNumerical() {
        if (state.score > appStats.highScore)
            appStats.highScore = state.score.toLong()
        if (state.elapsedTime > appStats.highTime)
            appStats.highTime = state.elapsedTime.toLong()
        appStats.totalScore += state.score
        appStats.totalHints += state.localStatistics.hints
        appStats.totalGems += state.localStatistics.gems
        appStats.totalPerfects += state.localStatistics.perfects
        appStats.totalCombinations += state.localStatistics.combinations
        appStats.totalTime += state.elapsedTime.toLong()
        appStats.totalGames++
    }

    private fun mergeMaps(a: Map<String, Long>, b: Map<String, Long>): Map<String, Long> {
        val next = a.toMutableMap()
        for ((key, value) in b) {
            val oldValue = next[key] ?: 0L
            next[key] = oldValue + value
        }
        return next
    }

    private fun combineColor() {
        appStats.colorStatistics = mergeMaps(appStats.colorStatistics, localStats.byColor)
    }

    private fun combineSize() {
        appStats.sizeStatistics = mergeMaps(appStats.sizeStatistics, localStats.bySize)
    }

    companion object {
        fun combine(state: GameState, into: AppStatistics) = StatSerializer(state, into).combine()
    }
}
