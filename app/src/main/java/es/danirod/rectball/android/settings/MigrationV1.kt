package es.danirod.rectball.android.settings

import android.content.Context
import android.content.SharedPreferences
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Base64Coder
import com.badlogic.gdx.utils.JsonReader
import org.json.JSONObject

/** Migration class for updating the schema to v1 from v0 (pre-0.4.10). */
internal class MigrationV1(private val context: Context, private val prefs: SharedPreferences) {

    fun migrate() {
        val editor = prefs.edit()
        migratePreferences(editor)
        migrateScoresFile(editor)
        migrateStatsFile(editor)
        bumpSchemaVersion(editor)
        editor.apply()
    }

    private fun migratePreferences(editor: SharedPreferences.Editor) {
        val legacyPrefs = context.getSharedPreferences("rectball", Context.MODE_PRIVATE)

        /* If there are no keys on this file, it's empty already. */
        if (legacyPrefs.all.isNotEmpty()) {
            editor.putBoolean(SettingsManager.TAG_ENABLE_SOUND, legacyPrefs.getBoolean("sound", true))
            editor.putBoolean(SettingsManager.TAG_ENABLE_COLORBLIND, legacyPrefs.getBoolean("colorblind", false))
            editor.putBoolean(SettingsManager.TAG_ENABLE_FULLSCREEN, legacyPrefs.getBoolean("fullscreen", false))
            editor.putBoolean(SettingsManager.TAG_ASKED_TUTORIAL, legacyPrefs.getBoolean("tutorialAsked", false))

            /* Then remove the legacy preferences file. */
            val legacyEditor = legacyPrefs.edit()
            legacyEditor.clear()
            legacyEditor.apply()
        }
    }

    private fun migrateScoresFile(editor: SharedPreferences.Editor) {
        if (Gdx.files.local("scores").exists()) {
            /* Decode old scores file. */
            val jsonData = decodeJSON("scores")

            /* Migrate data to the preferences file. */
            editor.putLong(SettingsManager.TAG_HIGH_SCORE, jsonData.getLong("highestScore", 0L))
            editor.putLong(SettingsManager.TAG_HIGH_TIME, jsonData.getLong("highestTime", 0L))

            /* Save changes and burn old file. */
            Gdx.files.local("scores").delete()
        }
    }

    private fun migrateStatsFile(editor: SharedPreferences.Editor) {
        if (Gdx.files.local("stats").exists()) {
            /* Decode old statistics file. */
            val jsonData = decodeJSON("stats")

            /* Migrate total data to the preferences file. */
            val total = jsonData["total"]["values"]
            editor.putLong(SettingsManager.TAG_TOTAL_SCORE, total.getLong("score", 0L))
            editor.putLong(SettingsManager.TAG_TOTAL_COMBINATIONS, total.getLong("combinations", 0L))
            editor.putLong(SettingsManager.TAG_TOTAL_BALLS, total.getLong("balls", 0L))
            editor.putLong(SettingsManager.TAG_TOTAL_GAMES, total.getLong("games", 0L))
            editor.putLong(SettingsManager.TAG_TOTAL_TIME, total.getLong("time", 0L))
            editor.putLong(SettingsManager.TAG_TOTAL_PERFECTS, total.getLong("perfects", 0L))
            editor.putLong(SettingsManager.TAG_TOTAL_HINTS, total.getLong("cheats", 0L))

            /* Migrate color data to the preferences file. */
            val colors = jsonData["colors"]["values"]
            editor.putLong(SettingsManager.TAG_TOTAL_COLOR_RED, colors.getLong("red", 0L))
            editor.putLong(SettingsManager.TAG_TOTAL_COLOR_GREEN, colors.getLong("green", 0L))
            editor.putLong(SettingsManager.TAG_TOTAL_COLOR_BLUE, colors.getLong("blue", 0L))
            editor.putLong(SettingsManager.TAG_TOTAL_COLOR_YELLOW, colors.getLong("yellow", 0L))

            /* Migrate sizes data to the preferences file. */
            val sizes = jsonData["sizes"]["values"]
            sizes.remove("class") // This legacy field is sponsored by reflected serialization
            val sizesMap = sizes.map { Pair<String, Long>(it.name, it.asLong()) }.toMap()
            editor.putString(SettingsManager.TAG_STAT_SIZES, serializeSizes(sizesMap))

            /* Save changes and burn old file. */
            Gdx.files.local("stats").delete()
        }
    }

    private fun bumpSchemaVersion(editor: SharedPreferences.Editor) {
        if (prefs.getInt(SettingsManager.TAG_SCHEMA_VERSION, 0) < SettingsManager.SCHEMA_VERSION) {
            editor.putInt(SettingsManager.TAG_SCHEMA_VERSION, SettingsManager.SCHEMA_VERSION)
            editor.apply()
        }
    }

    /** Decodes a Base64 scrambled JSON [file]. */
    private fun decodeJSON(file: String) = JsonReader().parse(Base64Coder.decodeString(Gdx.files.local(file).readString()))

    /** Converts a sizes [map] into a serialized JSON string that can be saved in the settings. */
    private fun serializeSizes(map: Map<String, Long>): String = JSONObject(map).toString()

}