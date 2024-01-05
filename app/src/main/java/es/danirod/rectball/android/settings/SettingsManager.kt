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
package es.danirod.rectball.android.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import es.danirod.rectball.android.AndroidLauncher

class SettingsManager(private val context: Context) {

    /** Preferences, contains settings, high scores and statistics. */
    val preferences: SharedPreferences by lazy {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        if (prefs.getInt(TAG_SCHEMA_VERSION, 0) < SCHEMA_VERSION) MigrationV1(context, prefs).migrate()
        prefs
    }

    companion object {
        /** Current schema version. If the schema is modified, this number should be increased. */
        const val SCHEMA_VERSION = 1

        /** Schema version of the saved preferences. */
        const val TAG_SCHEMA_VERSION = "${AndroidLauncher.PACKAGE}.SCHEMA_VERSION"

        /** Whether to enable sound or not. */
        const val TAG_ENABLE_SOUND = "${AndroidLauncher.PACKAGE}.ENABLE_SOUND"

        /** Whether to enable vibration or not. */
        const val TAG_ENABLE_VIBRATION = "${AndroidLauncher.PACKAGE}.ENABLE_VIBRATION"

        /** Request a wakelock or not. */
        const val TAG_KEEP_SCREEN_ON = "${AndroidLauncher.PACKAGE}.KEEP_SCREEN_ON"

        /** Whether to enable colorblind mode or not. */
        const val TAG_ENABLE_COLORBLIND = "${AndroidLauncher.PACKAGE}.ENABLE_COLORBLIND"

        /** Whether to enable the fullscreen mode or not. */
        const val TAG_ENABLE_FULLSCREEN = "${AndroidLauncher.PACKAGE}.ENABLE_FULLSCREEN"

        /** Whether the user has been already prompted about the tutorial. */
        const val TAG_ASKED_TUTORIAL = "${AndroidLauncher.PACKAGE}.ASKED_TUTORIAL"

        /** Whether the user was notified that we changed the selection mode. */
        const val TAG_NEW_SELECTION_MODE_NOTIFIED = "${AndroidLauncher.PACKAGE}.NEW_SELECTION_MODE_NOTIFIED"

        /** Current highest score reached in the game. */
        const val TAG_HIGH_SCORE = "${AndroidLauncher.PACKAGE}.HIGH_SCORE"

        /** Current highest time reached in the game. */
        const val TAG_HIGH_TIME = "${AndroidLauncher.PACKAGE}.HIGH_TIME"

        /** Total score made through all the games. */
        const val TAG_TOTAL_SCORE = "${AndroidLauncher.PACKAGE}.TOTAL_SCORE"

        /** Total number of combinations made through all the games. */
        const val TAG_TOTAL_COMBINATIONS = "${AndroidLauncher.PACKAGE}.TOTAL_COMBINATIONS"

        /** Total number of balls removed through all the games. */
        const val TAG_TOTAL_BALLS = "${AndroidLauncher.PACKAGE}.TOTAL_BALLS"

        /** Total number fo the games played all the time. */
        const val TAG_TOTAL_GAMES = "${AndroidLauncher.PACKAGE}.TOTAL_GAMES"

        /** Total number of seconds spent playing this game. */
        const val TAG_TOTAL_TIME = "${AndroidLauncher.PACKAGE}.TOTAL_TIME"

        /** Total number of perfect combinations made through all the games. */
        const val TAG_TOTAL_PERFECTS = "${AndroidLauncher.PACKAGE}.TOTAL_PERFECTS"

        /** Total number of hints used through all the games. */
        const val TAG_TOTAL_HINTS = "${AndroidLauncher.PACKAGE}.TOTAL_HINTS"

        /** Total number of red combinations made through all the games. */
        const val TAG_TOTAL_COLOR_RED = "${AndroidLauncher.PACKAGE}.TOTAL_COLOR_RED"

        /** Total number of green combinations made through all the games. */
        const val TAG_TOTAL_COLOR_GREEN = "${AndroidLauncher.PACKAGE}.TOTAL_COLOR_GREEN"

        /** Total number of blue combinations made through all the games. */
        const val TAG_TOTAL_COLOR_BLUE = "${AndroidLauncher.PACKAGE}.TOTAL_COLOR_BLUE"

        /** Total number of yellow combinations made through all the games. */
        const val TAG_TOTAL_COLOR_YELLOW = "${AndroidLauncher.PACKAGE}.TOTAL_COLOR_YELLOW"

        /** Maps every combination size with the amount of times it has been done. */
        const val TAG_STAT_SIZES = "${AndroidLauncher.PACKAGE}.STAT_SIZES"
    }
}
