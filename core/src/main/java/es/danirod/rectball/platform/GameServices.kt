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
package es.danirod.rectball.platform

import com.badlogic.gdx.Gdx
import es.danirod.rectball.gameservices.Achievement
import es.danirod.rectball.gameservices.Leaderboard

interface GameServices {

    /** Whether the game services are enabled for this platform. */
    val supported: Boolean

    /** How to name in public the game services (for instance, Google Play Games, Game Center...) */
    val name: String

    /** Returns true if the user is currently logged in to the game services provider. */
    fun signedIn(): Boolean

    /** Returns true if a login request is still in process. */
    fun loggingIn(): Boolean

    /** Processes a login request. */
    fun signIn()

    /** Processes a logout request. */
    fun signOut()

    /** Uploads a score to the game services. */
    fun sendLeaderboard(leaderboard: Leaderboard, value: Long)

    /** Collects an achievement from the platform. */
    fun sendAchievement(achievement: Achievement)

    /** Progresses an incremental achievement (percentage based). */
    fun progressAchievement(achievement: Achievement, count: Int)

    /** Request the game services to display the leaderboards. */
    fun showLeaderboards()

    /** Request the game services to display the achievements. */
    fun showAchievements()

    class NullGameServices : GameServices {
        override val supported = false

        override val name = ""

        override fun signedIn() = false

        override fun loggingIn() = false

        override fun signIn() = Gdx.app.log("NullGameServices", "Ignoring a request to sign in")

        override fun signOut() = Gdx.app.log("NullGameServices", "Ignoring a request to sign out")

        override fun sendAchievement(achievement: Achievement) =
            Gdx.app.log("NullGameServices", "Ignoring a request to submit an achievement (${achievement.name}")

        override fun progressAchievement(achievement: Achievement, count: Int) =
            Gdx.app.log("NullGameServices", "Ignoring a request to progress by $count achievement (${achievement.name}")

        override fun sendLeaderboard(leaderboard: Leaderboard, value: Long) =
            Gdx.app.log("NullGameServices", "Ignoring a request to submit a value ($value) to the leaderboard (${leaderboard.name}")

        override fun showLeaderboards() = Gdx.app.log("NullGameServices", "Ignoring a request to show the leaderboard")

        override fun showAchievements() = Gdx.app.log("NullGameServices", "Ignoring a request to show the achievements")
    }
}