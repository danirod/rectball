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

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.RelativeLayout
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter
import es.danirod.rectball.Platform
import es.danirod.rectball.RectballGame
import es.danirod.rectball.gameservices.GameServices
import es.danirod.rectball.model.GameState
import es.danirod.rectball.settings.AppSettings
import es.danirod.rectball.settings.AppStatistics

class AndroidLauncher : AndroidApplication(), Platform {

    private lateinit var platform: AndroidPlatform

    private lateinit var game: RectballGame

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        // Setup game state
        platform = AndroidPlatform(this)
        game = buildGameInstance(savedState)

        // Set up layout.
        val config = AndroidApplicationConfiguration()
        config.useImmersiveMode = false
        val rectballView = initializeForView(game, config)

        // Migration
        checkMigrate()

        // Configure insets.
        val insets = InsetConfiguration(this, game)
        insets.apply()

        val layout = RelativeLayout(this)
        layout.addView(rectballView)
        setContentView(layout)
    }

    private fun checkMigrate() {
        val settings = AppSettings(Gdx.app.getPreferences("es.danirod.rectball"))
        val statistics = AppStatistics(Gdx.app.getPreferences("es.danirod.rectball"))
        if (settings.schema < 3) {
            Gdx.app.log("DataMigrator", "schema is not 3, migration required")
            val migration = DataMigrator(this)
            migration.migrate(settings, statistics)
            settings.schema = 3
        } else {
            Gdx.app.log("DataMigrator", "schema is already v3, nothing to migrate")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (game.state.isPlaying) {
            outState.putString("state", serializeState(game.state))
        }
    }

    override fun onStart() {
        super.onStart()
        platform.onStart()
    }

    override fun onStop() {
        super.onStop()
        platform.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        platform.onActivityResult(requestCode, resultCode, data)
    }

    private fun buildGameInstance(state: Bundle?) =
        when (val realState = state?.getString("state")) {
            null -> RectballGame(this)
            else -> RectballGame(this, deserializeState(realState))
        }

    private fun serializeState(state: GameState): String = Json(JsonWriter.OutputType.json).toJson(state, GameState::class.java)

    private fun deserializeState(payload: String): GameState = Json().fromJson(GameState::class.java, payload)

    fun requestWakelock() {
        runOnUiThread { window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
    }

    fun clearWakelock() {
        runOnUiThread { window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
    }

    /** Game services sends scores and achievements. */
    override val gameServices: GameServices
        get() = platform.gameServices

    override val marketplace: Platform.Marketplace
        get() = platform.marketplace
    override val version: String
        get() = BuildConfig.VERSION_NAME
    override val buildNumber: Int
        get() = BuildConfig.VERSION_CODE

    companion object {
        const val PACKAGE: String = "es.danirod.rectball.android"
    }
}
