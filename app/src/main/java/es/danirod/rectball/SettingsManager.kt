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

package es.danirod.rectball

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Base64Coder
import com.badlogic.gdx.utils.JsonReader
import es.danirod.rectball.android.AndroidLauncher

class SettingsManager(private val context: Context) {

    val preferences: SharedPreferences by lazy {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        while (prefs.getInt(TAG_SCHEMA_VERSION, 0) < SCHEMA_VERSION) {
            val version = prefs.getInt(TAG_SCHEMA_VERSION, 0)
            Gdx.app.log(LOG_TAG, "Preferences migration required (found $version, expected $SCHEMA_VERSION")
            migratePreferences(prefs)
        }
        prefs
    }

    private fun migratePreferences(prefs: SharedPreferences) {
        /* Check if there is a legacy preferences file. */
        val legacyPrefs = context.getSharedPreferences("rectball", Context.MODE_PRIVATE)
        if (legacyPrefs.all.isNotEmpty()) {
            /* Move preferences from that file to the default one. */
            val editor = prefs.edit()
            editor.putBoolean(TAG_ENABLE_SOUND, legacyPrefs.getBoolean("sound", true))
            editor.putBoolean(TAG_ENABLE_COLORBLIND, legacyPrefs.getBoolean("colorblind", false))
            editor.putBoolean(TAG_ENABLE_FULLSCREEN, legacyPrefs.getBoolean("fullscreen", false))
            editor.putBoolean(TAG_ASKED_TUTORIAL, legacyPrefs.getBoolean("tutorialAsked", false))
            editor.apply()

            /* Then remove the legacy preferences file. */
            val legacyEditor = legacyPrefs.edit()
            legacyEditor.clear()
            legacyEditor.apply()
        }

        /* Check if a legacy scores file exists. */
        if (Gdx.files.local("scores").exists()) {
            /* Decode old scores file. */
            val rawScoresJSON = Base64Coder.decodeString(Gdx.files.local("scores").readString())
            val jsonReader = JsonReader()
            val jsonData = jsonReader.parse(rawScoresJSON)

            /* Migrate data to the preferences file. */
            val editor = prefs.edit()
            editor.putInt(TAG_HIGH_SCORE, jsonData.getInt("highestScore", 0))
            editor.putInt(TAG_HIGH_TIME, jsonData.getInt("highestTime", 0))
            editor.apply()

            /* Burn legacy file. */
            Gdx.files.local("scores").delete()
        }

        /* Bump the schema version. */
        if (prefs.getInt(TAG_SCHEMA_VERSION, 0) < SCHEMA_VERSION) {
            val editor = prefs.edit()
            editor.putInt(TAG_SCHEMA_VERSION, SCHEMA_VERSION)
            editor.apply()
        }
    }

    companion object {
        const val LOG_TAG = "SettingsManager"

        const val SCHEMA_VERSION = 1

        const val TAG_SCHEMA_VERSION = "${AndroidLauncher.PACKAGE}.SCHEMA_VERSION"

        const val TAG_ENABLE_SOUND = "${AndroidLauncher.PACKAGE}.ENABLE_SOUND"
        const val TAG_ENABLE_COLORBLIND = "${AndroidLauncher.PACKAGE}.ENABLE_COLORBLIND"
        const val TAG_ENABLE_FULLSCREEN = "${AndroidLauncher.PACKAGE}.ENABLE_FULLSCREEN"
        const val TAG_ASKED_TUTORIAL = "${AndroidLauncher.PACKAGE}.ASKED_TUTORIAL"

        const val TAG_HIGH_SCORE = "${AndroidLauncher.PACKAGE}.HIGH_SCORE"
        const val TAG_HIGH_TIME = "${AndroidLauncher.PACKAGE}.HIGH_TIME"
    }

}