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
package es.danirod.rectball.android

import android.content.Context
import com.badlogic.gdx.Gdx
import de.golfgl.gdxgamesvcs.GpgsClient
import es.danirod.rectball.gameservices.Achievement
import es.danirod.rectball.gameservices.Leaderboard
import es.danirod.rectball.platform.GameServices

class GooglePlayGamesIntegration(private val context: Context, private val client: GpgsClient) : GameServices {

    private fun log(string: String) = Gdx.app.log(javaClass.simpleName, string)

    override val supported = true

    override val name = "Google Play Games"

    override fun signedIn() = client.isSessionActive

    override fun loggingIn() = client.isConnectionPending

    override fun signIn() {
        client.logIn()
    }

    override fun signOut() {
        client.logOff()
    }

    private fun getLeaderboardId(key: Leaderboard) = when (key) {
        Leaderboard.HighestLength -> R.string.leaderboard_highest_length
        Leaderboard.HighestScore -> R.string.leaderboard_highest_score
    }

    private fun getAchievementId(key: Achievement) = when (key) {
        Achievement.Starter -> R.string.achievement_starter
        Achievement.Experienced -> R.string.achievement_experienced
        Achievement.Master -> R.string.achievement_master
        Achievement.CasualPlayer -> R.string.achievement_casual_player
        Achievement.Perseverant -> R.string.achievement_perseverant
        Achievement.Commuter -> R.string.achievement_commuter
        Achievement.ScoreBreaker -> R.string.achievement_score_breaker
    }

    override fun sendLeaderboard(leaderboard: Leaderboard, value: Long) {
        log("Sending $value to leaderboard ${leaderboard.name}")
        val string = context.getString(getLeaderboardId(leaderboard))
        client.submitToLeaderboard(string, value, client.gameServiceId)
    }

    override fun sendAchievement(achievement: Achievement) {
        log("Marking achievement ${achievement.name}")
        val string = context.getString(getAchievementId(achievement))
        client.unlockAchievement(string)
    }

    override fun progressAchievement(achievement: Achievement, count: Int) {
        log("Incrementing achievement ${achievement.name} by $count")
        val string = context.getString(getAchievementId(achievement))
        client.incrementAchievement(string, 1, 1f)
    }

    override fun showLeaderboards() {
        log("Requested to show leaderboards")
        val string = context.getString(getLeaderboardId(Leaderboard.HighestScore))
        client.showLeaderboards(string)
    }

    override fun showAchievements() {
        log("Requested to show achievements")
        client.showAchievements()
    }
}