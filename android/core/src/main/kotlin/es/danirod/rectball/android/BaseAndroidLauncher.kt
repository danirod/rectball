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

import android.os.Bundle
import android.widget.RelativeLayout
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import es.danirod.rectball.Platform
import es.danirod.rectball.RectballGame
import es.danirod.rectball.settings.AppSettings
import es.danirod.rectball.settings.AppStatistics

/**
 * This is the base Android Launcher that each subproject should extend.
 */
abstract class BaseAndroidLauncher : AndroidApplication(), Platform {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create game instance -- TODO: saved state is lost
        val game = RectballGame(this)

        // In order to do things in onCreate before actually starting the game,
        // I am creating a View rather than just calling initialize().
        val rectballView = initializeForView(game, gdxAppConfiguration())

        // Older versions of Rectball used SharedPreferences, check if they have
        // to be migrated to the new version of preferences (use GdxPreferences).
        applyMigrations()

        // Configure insets for those devices that have it.
        InsetConfiguration(this, game).apply()

        // Add the game and present it.
        val layout = RelativeLayout(this)
        layout.addView(rectballView)
        setContentView(layout)
    }

    private fun gdxAppConfiguration() = AndroidApplicationConfiguration().apply {
        useImmersiveMode = false
    }

    private fun applyMigrations() {
        val preferences = Gdx.app.getPreferences("es.danirod.rectball")
        val settings = AppSettings(preferences)

        if (settings.schema < 3) {
            Gdx.app.log("DataMigrator", "schema is not 3, migrations required")
            val statistics = AppStatistics(preferences)
            DataMigrator(this).migrate(settings, statistics)
            settings.schema = 3
        } else {
            Gdx.app.log("DataMigrator", "schema is 3, up to date")
        }
    }
}