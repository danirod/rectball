package es.danirod.rectball.android

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Base64Coder
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import es.danirod.rectball.settings.AppSettings
import es.danirod.rectball.settings.AppStatistics
import java.io.File

/**
 * Parses legacy preference files for the application, so that caller code can safely import them
 * into the v3 preferences format if it is found that v3 is not initialised yet (for instance,
 * on first run or after upgrading from an older version).
 */
class DataMigrator(private val context: Context) {

    private fun log(msg: String) = Log.i("DataMigrator", msg)
    
    fun migrate(settings: AppSettings, statistics: AppStatistics) {
        log("start migrate")
        if (version1.exists()) {
            log("data for v1 is found")
            version1.debug()
            version1.migrate(settings, statistics)
            version1.delete()
        }
        if (version2.exists()) {
            log("data for v2 is found")
            version2.debug()
            version2.migrate(settings, statistics)
            version2.delete()
        }
        log("migrate done")
    }

    interface Migrator {
        /** Returns true if a settings file for that version still exists. */
        fun exists(): Boolean
        /** Deletes the settings file for that version if it still exists. */
        fun delete()
        /** Actually dump the settings from the old schema into the given ones. */
        fun migrate(settings: AppSettings, statistics: AppStatistics)
        /** Calls Log.i with the JSON representation of the file. */
        fun debug()
    }

    val version1 = object : Migrator {
        override fun exists() = (prefsFile()?.exists() ?: false) || scoresFile().exists() || statsFile().exists()

        override fun delete() {
            val files = listOf(prefsFile(), scoresFile().file(), statsFile().file())
            for (file in files) {
                file?.let {
                    if (it.exists()) {
                        log("Trying to delete ${file.absolutePath}...")
                        log("This is a placeholder!")
                    }
                }
            }
        }

        override fun migrate(settings: AppSettings, statistics: AppStatistics) {
            DataSchemaV1(preferences(), scoresFile(), statsFile()).migrate(settings, statistics)
        }

        override fun debug() = DataSchemaV1(preferences(), scoresFile(), statsFile()).debug()

        private fun preferences() = context.getSharedPreferences("rectball", Context.MODE_PRIVATE)

        private fun prefsFile() = context.filesDir.parentFile?.let { root ->
            File("${root}/shared_prefs/rectball.xml")
        }

        private fun scoresFile() = FileHandle(context.filesDir.resolve("scores"))

        private fun statsFile() = FileHandle(context.filesDir.resolve("stats"))

    }

    val version2 = object : Migrator {
        override fun exists() = file()?.exists() ?: false

        override fun debug() = schema().debug()

        override fun migrate(settings: AppSettings, statistics: AppStatistics) = schema().migrate(settings, statistics)

        override fun delete() {
            file()?.let { file ->
                log("Trying to delete ${file.absolutePath}...")
                log("This is a placeholder, deletion will not be done yet")
            } ?: log("v2 SharedPreferences file does not exists")
        }

        private fun schema() = DataSchemaV2(preferences())

        private fun file() = context.filesDir.parentFile?.let { root ->
            File("${root}/shared_prefs/${context.packageName}_preferences.xml")
        }

        /** Get the SharedPreferences file for v2. */
        private fun preferences(): SharedPreferences {
            /* The original code calls PreferenceManager.getDefaultSharedPreferences(context).
             * Here I am unrolling the code to do this manually, since in the future I'll want to
             * delete the SharedPreferences file, and I need the file name to do that. */
            val prefName = "${context.packageName}_preferences"
            return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        }
    }

    private inner class DataSchemaV1(preferences: SharedPreferences, scores: FileHandle, stats: FileHandle) {
        private val soundEnabled = preferences.getBoolean("sound", true)
        private val colorblindMode = preferences.getBoolean("colorblind", false)
        private val askedTutorial = preferences.getBoolean("tutorialAsked", false)

        private val scoresFile = decode(scores)
        private val highScore = scoresFile.getLong("highestScore", 0L)
        private val highTime = scoresFile.getLong("highestTime", 0L)

        private val statsFile = decode(stats)
        private val totalStats = statsFile["total"]["values"]
        private val colorStats = statsFile["colors"]["values"]
        private val sizeStats = statsFile["sizes"]["values"]

        private val totalScore = totalStats.getLong("score", 0L)
        private val totalCombinations = totalStats.getLong("combinations", 0L)
        private val totalBalls = totalStats.getLong("balls", 0L)
        private val totalTime = totalStats.getLong("time", 0L)
        private val totalGames = totalStats.getLong("games", 0L)
        private val totalPerfects = totalStats.getLong("perfect", 0L)
        private val totalHints = totalStats.getLong("cheats", 0L)

        private val byColor = listOf("red", "green", "yellow", "blue").associateWith { c -> colorStats.getLong(c, 0L) }
        private val bySizes: Map<String, Long> = sizeStats.let { json ->
            val map = mutableMapOf<String, Long>()
            if (json.isObject) {
                var child = json.child()
                while (child != null) {
                    /* class key is added because of serialization */
                    if (child.name != "class")
                        map[child.name] = child.asLong()
                    child = child.next()
                }
            }
            map
        }

        private fun decode(file: FileHandle) = file.readString()
                .let { data -> Base64Coder.decodeString(data) }
                .let { json -> JsonReader().parse(json) }

        fun migrate(settings: AppSettings, statistics: AppStatistics) {
            settings.tutorialAsked = askedTutorial
            settings.colorblindMode = colorblindMode
            settings.soundEnabled = soundEnabled

            statistics.highTime = highTime
            statistics.highScore = highScore
            statistics.totalTime = totalTime
            statistics.totalGames = totalGames
            statistics.totalCombinations = totalCombinations
            statistics.totalPerfects = totalPerfects
            statistics.totalHints = totalHints
            statistics.totalGems = totalBalls
            statistics.totalScore = totalScore

            statistics.colorStatistics = byColor
            statistics.sizeStatistics = bySizes
        }

        fun debug() {
            val jsonObject = JsonValue(JsonValue.ValueType.`object`)

            // Settings
            jsonObject.addChild("sound_enabled", JsonValue(soundEnabled))
            jsonObject.addChild("colorblind_mode", JsonValue(colorblindMode))
            jsonObject.addChild("tutorial_asked", JsonValue(askedTutorial))

            // Statistics
            jsonObject.addChild("high_score", JsonValue(highScore))
            jsonObject.addChild("high_time", JsonValue(highTime))
            jsonObject.addChild("total_score", JsonValue(totalScore))
            jsonObject.addChild("total_combinations", JsonValue(totalCombinations))
            jsonObject.addChild("total_balls", JsonValue(totalBalls))
            jsonObject.addChild("total_games", JsonValue(totalGames))
            jsonObject.addChild("total_time", JsonValue(totalTime))
            jsonObject.addChild("total_perfects", JsonValue(totalPerfects))
            jsonObject.addChild("total_hints", JsonValue(totalHints))

            jsonObject.addChild("by_color", mapToJsonValue(byColor))
            jsonObject.addChild("by_size", mapToJsonValue(bySizes))

            val content = jsonObject.prettyPrint(JsonWriter.OutputType.json, 50)
            log("Full schema")
            log(content)
        }
    }

    private inner class DataSchemaV2(preferences: SharedPreferences) {
        private val schemaVersion =
            preferences.getInt("es.danirod.rectball.android.SCHEMA_VERSION", 0)

        private val soundEnabled =
            preferences.getBoolean("es.danirod.rectball.android.ENABLE_SOUND", true)
        private val vibrationEnabled =
            preferences.getBoolean("es.danirod.rectball.android.ENABLE_VIBRATION", true)
        private val keepScreenOn =
            preferences.getBoolean("es.danirod.rectball.android.KEEP_SCREEN_ON", true)
        private val colorblindMode =
            preferences.getBoolean("es.danirod.rectball.android.ENABLE_COLORBLIND", false)
        private val tutorialAsked =
            preferences.getBoolean("es.danirod.rectball.android.ASKED_TUTORIAL", false)
        private val newInputMethodAsked =
            preferences.getBoolean("es.danirod.rectball.android.NEW_SELECTION_MODE_NOTIFIED", false)

        private val highScore = preferences.getLong("es.danirod.rectball.android.HIGH_SCORE", 0L)
        private val highTime = preferences.getLong("es.danirod.rectball.android.HIGH_TIME", 0L)
        private val totalScore = preferences.getLong("es.danirod.rectball.android.TOTAL_SCORE", 0L)
        private val totalCombinations =
            preferences.getLong("es.danirod.rectball.android.TOTAL_COMBINATIONS", 0L)
        private val totalBalls = preferences.getLong("es.danirod.rectball.android.TOTAL_BALLS", 0L)
        private val totalGames = preferences.getLong("es.danirod.rectball.android.TOTAL_GAMES", 0L)
        private val totalTime = preferences.getLong("es.danirod.rectball.android.TOTAL_TIME", 0L)
        private val totalPerfects =
            preferences.getLong("es.danirod.rectball.android.TOTAL_PERFECTS", 0L)
        private val totalHints = preferences.getLong("es.danirod.rectball.android.TOTAL_HINTS", 0L)
        private val byColor = mapOf(
            "red" to preferences.getLong("es.danirod.rectball.android.TOTAL_COLOR_RED", 0L),
            "green" to preferences.getLong("es.danirod.rectball.android.TOTAL_COLOR_GREEN", 0L),
            "yellow" to preferences.getLong("es.danirod.rectball.android.TOTAL_COLOR_YELLOW", 0L),
            "blue" to preferences.getLong("es.danirod.rectball.android.TOTAL_COLOR_BLUE", 0L),
        )
        private val bySize: Map<String, Long> =
            preferences.getString("es.danirod.rectball.android.STAT_SIZES", "{}").let { str ->
                val decoder = JsonReader().parse(str)
                mutableMapOf<String, Long>().apply {
                    if (decoder.isObject) {
                        var entry = decoder.child()
                        while (entry != null) {
                            this[entry.name] = entry.asLong()
                            entry = entry.next()
                        }
                    }
                }
            }

        fun migrate(settings: AppSettings, statistics: AppStatistics) {
            settings.soundEnabled = soundEnabled
            settings.vibrationEnabled = vibrationEnabled
            settings.keepScreenOn = keepScreenOn
            settings.colorblindMode = colorblindMode
            settings.tutorialAsked = tutorialAsked
            settings.newInputMethodAsked = newInputMethodAsked

            statistics.highTime = highTime
            statistics.highScore = highScore
            statistics.totalScore = totalScore
            statistics.totalCombinations = totalCombinations
            statistics.totalGems = totalBalls
            statistics.totalTime = totalTime
            statistics.totalPerfects = totalPerfects
            statistics.totalGames = totalGames
            statistics.totalHints = totalHints
            statistics.colorStatistics = byColor
            statistics.sizeStatistics = bySize
        }

        fun debug() {
            val jsonObject = JsonValue(JsonValue.ValueType.`object`)

            // Persist schema version
            jsonObject.addChild("schema", JsonValue(schemaVersion.toLong()))

            // Send statistics
            jsonObject.addChild("sound_enabled", JsonValue(soundEnabled))
            jsonObject.addChild("vibration_enabled", JsonValue(vibrationEnabled))
            jsonObject.addChild("keep_screen_on", JsonValue(keepScreenOn))
            jsonObject.addChild("colorblind_mode", JsonValue(colorblindMode))
            jsonObject.addChild("tutorial_asked", JsonValue(tutorialAsked))
            jsonObject.addChild("new_input_method_asked", JsonValue(newInputMethodAsked))

            // Persist statistics
            jsonObject.addChild("high_score", JsonValue(highScore))
            jsonObject.addChild("high_time", JsonValue(highTime))
            jsonObject.addChild("total_score", JsonValue(totalScore))
            jsonObject.addChild("total_combinations", JsonValue(totalCombinations))
            jsonObject.addChild("total_balls", JsonValue(totalBalls))
            jsonObject.addChild("total_games", JsonValue(totalGames))
            jsonObject.addChild("total_time", JsonValue(totalTime))
            jsonObject.addChild("total_perfects", JsonValue(totalPerfects))
            jsonObject.addChild("total_hints", JsonValue(totalHints))

            jsonObject.addChild("by_color", mapToJsonValue(byColor))
            jsonObject.addChild("by_size", mapToJsonValue(bySize))

            val content = jsonObject.prettyPrint(JsonWriter.OutputType.json, 50)
            log("Full schema")
            log(content)
        }
    }

    private fun mapToJsonValue(map: Map<String, Long>) =
        JsonValue(JsonValue.ValueType.`object`).apply {
            map.entries.forEach { (key, value) -> addChild(key, JsonValue(value)) }
        }
}