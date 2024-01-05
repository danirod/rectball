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

import android.content.SharedPreferences
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import es.danirod.rectball.android.settings.SettingsManager

class AppStatistics(val preferences: SharedPreferences) {

    var highScore: Long
        get() = getLong(SettingsManager.TAG_HIGH_SCORE)
        set(value) = setLong(SettingsManager.TAG_HIGH_SCORE, value)

    var highTime: Long
        get() = getLong(SettingsManager.TAG_HIGH_TIME)
        set(value) = setLong(SettingsManager.TAG_HIGH_TIME, value)

    var totalScore: Long
        get() = getLong(SettingsManager.TAG_TOTAL_SCORE)
        set(value) = setLong(SettingsManager.TAG_TOTAL_SCORE, value)

    var totalCombinations: Long
        get() = getLong(SettingsManager.TAG_TOTAL_COMBINATIONS)
        set(value) = setLong(SettingsManager.TAG_TOTAL_COMBINATIONS, value)

    var totalGems: Long
        get() = getLong(SettingsManager.TAG_TOTAL_BALLS)
        set(value) = setLong(SettingsManager.TAG_TOTAL_BALLS, value)

    var totalGames: Long
        get() = getLong(SettingsManager.TAG_TOTAL_GAMES)
        set(value) = setLong(SettingsManager.TAG_TOTAL_GAMES, value)

    var totalTime: Long
        get() = getLong(SettingsManager.TAG_TOTAL_TIME)
        set(value) = setLong(SettingsManager.TAG_TOTAL_TIME, value)

    var totalPerfects: Long
        get() = getLong(SettingsManager.TAG_TOTAL_PERFECTS)
        set(value) = setLong(SettingsManager.TAG_TOTAL_PERFECTS, value)

    var totalHints: Long
        get() = getLong(SettingsManager.TAG_TOTAL_HINTS)
        set(value) = setLong(SettingsManager.TAG_TOTAL_HINTS, value)

    var colorStatistics: Map<String, Long>
        get() = mapOf(
            "red" to getLong(SettingsManager.TAG_TOTAL_COLOR_RED),
            "green" to getLong(SettingsManager.TAG_TOTAL_COLOR_GREEN),
            "blue" to getLong(SettingsManager.TAG_TOTAL_COLOR_BLUE),
            "yellow" to getLong(SettingsManager.TAG_TOTAL_COLOR_YELLOW),
        ).filterValues { it > 0 }
        set(value) {
            setColorMap(value)
        }

    var sizeStatistics: Map<String, Long>
        get() = decodeSizeStatistics()
        set(value) = updateSizeStatistics(value)


    private fun getLong(key: String) = preferences.getLong(key, 0L)

    private fun getString(key: String) = preferences.getString(key, "{}")

    private fun setLong(key: String, value: Long) {
        val editor = preferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    private fun setString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun setColorMap(colorMap: Map<String, Long>) {
        setLong(SettingsManager.TAG_TOTAL_COLOR_RED, colorMap["red"] ?: 0)
        setLong(SettingsManager.TAG_TOTAL_COLOR_GREEN, colorMap["green"] ?: 0)
        setLong(SettingsManager.TAG_TOTAL_COLOR_YELLOW, colorMap["yellow"] ?: 0)
        setLong(SettingsManager.TAG_TOTAL_COLOR_BLUE, colorMap["blue"] ?: 0)
    }

    private fun decodeSizeStatistics(): Map<String, Long> {
        val content = getString(SettingsManager.TAG_STAT_SIZES)
        Gdx.app.log("AppStatistics", "Requested to decode the following string: $content")
        val decoder = JsonReader().parse(content)
        val map = mutableMapOf<String, Long>()
        if (decoder.isObject) {
            var entry = decoder.child()
            while (entry != null) {
                map[entry.name] = entry.asLong()
                entry = entry.next()
            }
        }
        Gdx.app.log("AppStatistics", "Decoded the following map: $map")
        return map
    }

    private fun updateSizeStatistics(map: Map<String, Long>) {
        Gdx.app.log("AppStatistics", "Requested to save the following map: $map")
        val encoder = JsonValue(JsonValue.ValueType.`object`)
        for ((k, v) in map) {
            encoder.addChild(k, JsonValue(v))
        }
        val content = encoder.toJson(JsonWriter.OutputType.json)
        Gdx.app.log("AppStatistics", "Saving the following size statistics: $content")
        setString(SettingsManager.TAG_STAT_SIZES, content)
    }
}
