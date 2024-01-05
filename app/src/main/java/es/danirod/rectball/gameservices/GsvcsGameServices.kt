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

import de.golfgl.gdxgamesvcs.IGameServiceClient
import de.golfgl.gdxgamesvcs.NoGameServiceClient

class GsvcsGameServices(
    private val client: IGameServiceClient,
    private val constants: GameServicesConstants
) : GameServices {

    // If the gameServiceId is set to GS_NOOP, then there are no integrations at all.
    override val supported = client.gameServiceId != NoGameServiceClient.GAMESERVICE_ID

    override fun signIn() {
        client.logIn()
    }

    override fun signOut() {
        client.logOff()
    }

    override val isLoggingIn: Boolean
        get() = client.isConnectionPending

    override val isSignedIn: Boolean
        get() = client.isSessionActive

    override fun uploadScore(score: Int, time: Int) {
        if (!client.isSessionActive) {
            return
        }

        client.submitToLeaderboard(
            constants.leaderboardHighestScore,
            score.toLong(),
            client.gameServiceId
        )
        client.submitToLeaderboard(
            constants.leaderboardHighestLength,
            time.toLong(),
            client.gameServiceId
        )

        if (score > 250) {
            client.unlockAchievement(constants.achievementStarter)
        }
        if (score > 2500) {
            client.unlockAchievement(constants.achievementExperienced)
        }
        if (score > 7500) {
            client.unlockAchievement(constants.achievementMaster)
        }
        if (score > 10000) {
            client.unlockAchievement(constants.achievementScoreBreaker)
        }

        if (score > 500) {
            client.incrementAchievement(constants.achievementCasualPlayer, 1, 1f)
            client.incrementAchievement(constants.achievementPerserverant, 1, 1f)
            client.incrementAchievement(constants.achievementCommuter, 1, 1f)
        }
    }

    override fun showLeaderboards() {
        client.showLeaderboards(constants.leaderboardHighestScore)
    }

    override fun showAchievements() {
        client.showAchievements()
    }

}
