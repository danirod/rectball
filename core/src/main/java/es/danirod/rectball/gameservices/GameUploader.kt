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
package es.danirod.rectball.gameservices

import es.danirod.rectball.model.GameState
import es.danirod.rectball.platform.GameServices

class GameUploader(state: GameState, private val services: GameServices) {

    // Google Play: score has to be submitted in milliseconds
    private val score = state.score.toLong()

    private val time = state.elapsedTime.toLong() * 1000L

    fun submit() {
        submitLeaderboard()
        submitAchievements()
    }

    private fun submitLeaderboard() {
        services.sendLeaderboard(Leaderboard.HighestScore, score)
        services.sendLeaderboard(Leaderboard.HighestLength, time)
    }

    private fun submitAchievements() {
        if (score > 250)
            services.sendAchievement(Achievement.Starter)
        if (score > 2500)
            services.sendAchievement(Achievement.Experienced)
        if (score > 7500)
            services.sendAchievement(Achievement.Master)
        if (score > 10000)
            services.sendAchievement(Achievement.ScoreBreaker)
        if (score > 500) {
            services.progressAchievement(Achievement.CasualPlayer, 1)
            services.progressAchievement(Achievement.Perseverant, 1)
            services.progressAchievement(Achievement.Commuter, 1)
        }
    }

}