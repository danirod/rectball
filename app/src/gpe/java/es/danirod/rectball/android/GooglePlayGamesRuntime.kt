/*
 * This file is part of Rectball
 * Copyright (C) 2015-2017 Dani Rodríguez
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.danirod.rectball.android

import android.widget.Toast
import com.badlogic.gdx.Gdx
import com.google.android.gms.games.Games
import com.google.games.basegameutils.GameHelper

/**
 * Android implementation for the Google Play services.
 *
 * @author danirod
 * @since 0.4.0
 */
internal class GooglePlayGamesRuntime(private val app: AndroidLauncher) : GameServices {

    var gameHelper = GameHelper(app, GameHelper.CLIENT_GAMES)

    init {
        this.gameHelper.setup(object : GameHelper.GameHelperListener {
            override fun onSignInFailed() {
                app.runOnUiThread { Toast.makeText(app.context, "Cannot sign in to Google Play Services", Toast.LENGTH_LONG).show() }
            }

            override fun onSignInSucceeded() {

            }
        })
    }

    override fun signIn() {
        try {
            app.runOnUiThread { gameHelper.beginUserInitiatedSignIn() }
        } catch (ex: Exception) {
            Gdx.app.error("GameServices", "Google Play login failed", ex)
        }

    }

    override fun signOut() {
        try {
            app.runOnUiThread {
                if (gameHelper.isSignedIn) {
                    gameHelper.signOut()
                }
            }
        } catch (ex: Exception) {
            Gdx.app.error("GameServices", "Google Play log out failed", ex)
        }

    }

    override val isSignedIn: Boolean
        get() = gameHelper.isSignedIn

    override fun uploadScore(score: Int, time: Int) {
        Gdx.app.debug("GameServices", "Submitting score: " + score)
        Gdx.app.debug("GameServices", "Submitting time: " + time)

        if (gameHelper.isSignedIn) {
            // Submit information about scores and time for the leaderboards.
            Games.Leaderboards.submitScore(gameHelper.apiClient,
                    app.getString(R.string.leaderboard_highest_score), score.toLong())
            Games.Leaderboards.submitScore(gameHelper.apiClient,
                    app.getString(R.string.leaderboard_highest_length), time.toLong())

            // Increment the achievements.
            if (score > 250) {
                Games.Achievements.unlock(gameHelper.apiClient,
                        app.getString(R.string.achievement_starter))
            }
            if (score > 2500) {
                Games.Achievements.unlock(gameHelper.apiClient,
                        app.getString(R.string.achievement_experienced))
            }
            if (score > 7500) {
                Games.Achievements.unlock(gameHelper.apiClient,
                        app.getString(R.string.achievement_master))
            }

            if (score > 10000) {
                Games.Achievements.unlock(gameHelper.apiClient,
                        app.getString(R.string.achievement_score_breaker))
            }

            if (score > 500) {
                Games.Achievements.increment(gameHelper.apiClient,
                        app.getString(R.string.achievement_casual_player), 1)
                Games.Achievements.increment(gameHelper.apiClient,
                        app.getString(R.string.achievement_perseverant), 1)
                Games.Achievements.increment(gameHelper.apiClient,
                        app.getString(R.string.achievement_commuter), 1)
            }
        }
    }

    override fun showLeaderboards() {
        app.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(gameHelper.apiClient), 0)
    }

    override fun showAchievements() {
        app.startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.apiClient), 1)
    }
}
