/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2017 Dani Rodríguez.
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

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter
import es.danirod.rectball.RectballGame
import es.danirod.rectball.android.settings.SettingsManager
import es.danirod.rectball.model.GameState

class AndroidLauncher : AndroidApplication() {

    private lateinit var platform: AndroidPlatform

    private lateinit var game: RectballGame

    lateinit var settings: SettingsManager

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        // Setup game state
        platform = AndroidPlatform(this)
        settings = SettingsManager(this)
        game = if (savedState != null) buildGameInstance(savedState) else RectballGame(this)

        // Request wakelock
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Set up layout.
        val config = AndroidApplicationConfiguration()
        val rectballView = initializeForView(game, config)
        val layout = RelativeLayout(this)
        layout.addView(rectballView)
        setContentView(layout)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        platform.onActivityResult(requestCode, resultCode, data)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && shouldEnableFullscreenMode()) {
            if (Build.VERSION.SDK_INT >= 19) {
                enableImmersiveMode()
            } else {
                legacyFullscreenMode()
            }
        }
    }

    private fun buildGameInstance(state: Bundle) =
            if (state.getString("state") != null)
                RectballGame(this, deserializeState(state.getString("state")))
            else
                RectballGame(this)

    private fun serializeState(state: GameState): String = Json(JsonWriter.OutputType.json).toJson(state, GameState::class.java)

    private fun deserializeState(payload: String): GameState = Json().fromJson(GameState::class.java, payload)

    private fun shouldEnableFullscreenMode(): Boolean = settings.preferences.getBoolean(SettingsManager.TAG_ENABLE_FULLSCREEN, false)

    /**
     * Enables immersive mode on Android 4.4+. When in fullscreen mode, both status and navigation
     * bar are hidden but the user is still able to temporally access the controls by using a swipe
     * gesture.
     */
    @TargetApi(19)
    private fun enableImmersiveMode() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    /**
     * Legacy fullscreen mode on Android 4.0, 4.1, 4.2 and 4.3. Because these platforms do not have
     * immersive mode, I cannot use the flag. Plus, I cannot hide the navigation and the status
     * bar at the same time so I'm only hiding the status (top) bar. On devices that have hardware
     * buttons, it will actually be real fullscreen.
     */
    @TargetApi(14)
    private fun legacyFullscreenMode() {
        if (Build.VERSION.SDK_INT < 16) {
            // Android ICS doesn't have FULLSCREEN flag.
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
    }

    /** Analytic services sends usage information. */
    val analytics: Analytics
        get() = platform.analytics

    /** Game services sends scores and achievements. */
    val gameServices: GameServices
        get() = platform.gameServices

    companion object {
        const val PACKAGE: String = "es.danirod.rectball.android"
    }
}
