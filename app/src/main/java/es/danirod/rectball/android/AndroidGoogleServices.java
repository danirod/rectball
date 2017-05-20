/*
 * This file is part of Rectball
 * Copyright (C) 2015 Dani Rodríguez
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

package es.danirod.rectball.android;

import com.google.games.basegameutils.GameHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.games.Games;
import es.danirod.rectball.platform.GoogleServices;

/**
 * Android implementation for the Google Play services.
 *
 * @author danirod
 * @since 0.4.0
 */
public class AndroidGoogleServices implements GoogleServices {

    private AndroidApplication app;

    private GameHelper helper;

    public AndroidGoogleServices(AndroidLauncher app) {
        this.app = app;
        this.helper = app.getGameHelper();
    }

    @Override
    public void signIn() {
        try {
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    helper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception ex) {
            Gdx.app.error("GoogleServices", "Google Play login failed", ex);
        }
    }

    @Override
    public void signOut() {
        try {
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (helper.isSignedIn()) {
                        helper.signOut();
                    }
                }
            });
        } catch (Exception ex) {
            Gdx.app.error("GoogleServices", "Google Play log out failed", ex);
        }
    }

    @Override
    public boolean isSignedIn() {
        return helper.isSignedIn();
    }

    @Override
    public void uploadScore(int score, int time) {
        Gdx.app.debug("GoogleServices", "Submitting score: " + score);
        Gdx.app.debug("GoogleServices", "Submitting time: " + time);

        if (helper.isSignedIn()) {
            // Submit information about scores and time for the leaderboards.
            Games.Leaderboards.submitScore(helper.getApiClient(),
                    app.getString(R.string.leaderboard_highest_score), score);
            Games.Leaderboards.submitScore(helper.getApiClient(),
                    app.getString(R.string.leaderboard_highest_length), time);

            // Increment the achievements.
            if (score > 250) {
                Games.Achievements.unlock(helper.getApiClient(),
                        app.getString(R.string.achievement_starter));
            }
            if (score > 2500) {
                Games.Achievements.unlock(helper.getApiClient(),
                        app.getString(R.string.achievement_experienced));
            }
            if (score > 7500) {
                Games.Achievements.unlock(helper.getApiClient(),
                        app.getString(R.string.achievement_master));
            }

            if (score > 10000) {
                Games.Achievements.unlock(helper.getApiClient(),
                        app.getString(R.string.achievement_score_breaker));
            }

            if (score > 500) {
                Games.Achievements.increment(helper.getApiClient(),
                        app.getString(R.string.achievement_casual_player), 1);
                Games.Achievements.increment(helper.getApiClient(),
                        app.getString(R.string.achievement_perseverant), 1);
                Games.Achievements.increment(helper.getApiClient(),
                        app.getString(R.string.achievement_commuter), 1);
            }
        }
    }

    @Override
    public void showLeaderboards() {
        app.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(helper.getApiClient()), 0);
    }

    @Override
    public void showAchievements() {
        app.startActivityForResult(Games.Achievements.getAchievementsIntent(helper.getApiClient()), 1);
    }
}
