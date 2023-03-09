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

package es.danirod.rectball.android.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import es.danirod.rectball.android.AndroidLauncher
import es.danirod.rectball.model.GameState
import org.json.JSONObject

class SettingsManager(private val context: Context) {

    /** Preferences, contains settings, high scores and statistics. */
    val preferences: SharedPreferences by lazy {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        if (prefs.getInt(TAG_SCHEMA_VERSION, 0) < SCHEMA_VERSION) MigrationV1(context, prefs).migrate()
        prefs
    }

    /** Maps combination sizes (2x2, 2x3...) with the amount of times they've been made. */
    val sizeStatistics: Map<String, Long>
        get() = deserializeSizes(preferences.getString(TAG_STAT_SIZES, "{}")!!)

    /**
     * Commit information about a game once finished. This method will extract the scores and other
     * statistics about the game [state] and insert the data into the global statistics structure,
     * as well as update the high scores if they are actually higher.
     */
    fun commitState(state: GameState) {
        val editor = preferences.edit()
        commitHighScore(state.score.toLong(), state.elapsedTime.toLong(), editor)
        commitScore(state.score.toLong(), state.elapsedTime.toLong(), editor)
        commitTotalStatistics(state.totalStatistics, editor)
        commitColorStatistics(state.colorStatistics, editor)
        commitSizesStatistics(state.sizesStatistics, editor)
        editor.apply()
    }

    private fun commitHighScore(score: Long, time: Long, editor: SharedPreferences.Editor) {
        editor.putLong(TAG_HIGH_SCORE, maxOf(preferences.getLong(TAG_HIGH_SCORE, 0L), score))
        editor.putLong(TAG_HIGH_TIME, maxOf(preferences.getLong(TAG_HIGH_TIME, 0L), time))
    }

    private fun commitScore(score: Long, time: Long, editor: SharedPreferences.Editor) {
        increment(editor, TAG_TOTAL_SCORE, score)
        increment(editor, TAG_TOTAL_TIME, time)
    }

    private fun commitTotalStatistics(local: Bundle, editor: SharedPreferences.Editor) {
        increment(editor, TAG_TOTAL_COMBINATIONS, local.getLong(TAG_TOTAL_COMBINATIONS, 0L))
        increment(editor, TAG_TOTAL_BALLS, local.getLong(TAG_TOTAL_BALLS, 0L))
        increment(editor, TAG_TOTAL_GAMES, 1L)
        increment(editor, TAG_TOTAL_PERFECTS, local.getLong(TAG_TOTAL_PERFECTS, 0L))
        increment(editor, TAG_TOTAL_HINTS, local.getLong(TAG_TOTAL_HINTS, 0L))
    }

    private fun commitColorStatistics(local: Bundle, editor: SharedPreferences.Editor) {
        increment(editor, TAG_TOTAL_COLOR_RED, local.getLong("red", 0L))
        increment(editor, TAG_TOTAL_COLOR_GREEN, local.getLong("green", 0L))
        increment(editor, TAG_TOTAL_COLOR_BLUE, local.getLong("blue", 0L))
        increment(editor, TAG_TOTAL_COLOR_YELLOW, local.getLong("yellow", 0L))
    }

    private fun commitSizesStatistics(local: Bundle, editor: SharedPreferences.Editor) {
        val combinedSizes = mergeSizesMaps(sizeStatistics, bundleToMap(local))
        editor.putString(TAG_STAT_SIZES, serializeSizes(combinedSizes))
    }

    /**
     * Increments the value of the preference [key] by the amount [value].
     * @param [editor] the editor instance is currently modifying the preferences.
     */
    private fun increment(editor: SharedPreferences.Editor, key: String, value: Long) {
        editor.putLong(key, preferences.getLong(key, 0L) + value)
    }

    private fun mergeSizesMaps(map1: Map<String, Long>, map2: Map<String, Long>): Map<String, Long> =
            (map1.keys union map2.keys).associate { it to (map1[it] ?: 0L) + (map2[it] ?: 0L) }

    private fun bundleToMap(bundle: Bundle) = bundle.keySet().associate { it to bundle.getLong(it) }

    /** Converts a sizes [map] into a serialized JSON string that can be saved in the settings. */
    private fun serializeSizes(map: Map<String, Long>): String = JSONObject(map).toString()

    /** Converts a serialized [payload] JSON string into a sizes map structure. */
    private fun deserializeSizes(payload: String): Map<String, Long> = JSONObject(payload).let {
        it.keys().iterator().asSequence().associate { k -> k to it.getLong(k) }.toMap()
    }

    companion object {
        /** Current schema version. If the schema is modified, this number should be increased. */
        const val SCHEMA_VERSION = 1

        /** Schema version of the saved preferences. */
        const val TAG_SCHEMA_VERSION = "${AndroidLauncher.PACKAGE}.SCHEMA_VERSION"

        /** Whether to enable sound or not. */
        const val TAG_ENABLE_SOUND = "${AndroidLauncher.PACKAGE}.ENABLE_SOUND"

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