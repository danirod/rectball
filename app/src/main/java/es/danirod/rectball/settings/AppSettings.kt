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

import es.danirod.rectball.RectballGame
import es.danirod.rectball.android.settings.SettingsManager

/**
 * Wrapper so that the game doesn't have to access the settings all the time.
 * Also, important in order to refactor the settings implementation without
 * having to change so much code.
 *
 * @since 0.5
 */
class AppSettings(private val game: RectballGame) {

    var soundEnabled: Boolean
        get() = getSetting(SettingsManager.TAG_ENABLE_SOUND, true)
        set(value) = setSetting(SettingsManager.TAG_ENABLE_SOUND, value)

    var vibrationEnabled: Boolean
        get() = getSetting(SettingsManager.TAG_ENABLE_VIBRATION, true)
        set(value) = setSetting(SettingsManager.TAG_ENABLE_VIBRATION, value)

    var keepScreenOn: Boolean
        get() = getSetting(SettingsManager.TAG_KEEP_SCREEN_ON, true)
        set(value) = setSetting(SettingsManager.TAG_KEEP_SCREEN_ON, value)

    var colorblindMode: Boolean
        get() = getSetting(SettingsManager.TAG_ENABLE_COLORBLIND, false)
        set(value) = setSetting(SettingsManager.TAG_ENABLE_COLORBLIND, value)

    private fun getSetting(key: String, defValue: Boolean) = game.context.settings.preferences.getBoolean(key, defValue)

    private fun setSetting(key: String, value: Boolean) {
        val editor = game.context.settings.preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}
