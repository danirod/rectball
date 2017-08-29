/*
 * This file is part of Rectball
 * Copyright (C) 2015-2017 Dani Rodríguez
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

package es.danirod.rectball.scene2d.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import es.danirod.rectball.RectballGame
import es.danirod.rectball.SettingsManager
import java.util.*

class StatsTable(private val game: RectballGame, private val title: LabelStyle, private val data: LabelStyle) : Table() {

    init {
        defaults().padBottom(30f).padRight(10f)
        add(addBestScores()).fillX().expandX().row()
        add(addTotalData()).fillX().expandX().row()
        add(addColorData()).fillX().expandX().row()
        add(addSizesData()).fillX().expandX().row()
    }

    private fun addBestScores(): Table {
        val best = Table()

        best.add(Label(game.locale.get("statistics.best"), this.title)).colspan(2).row()

        if (game.settings.preferences.getInt(SettingsManager.TAG_HIGH_SCORE, 0) == 0 &&
                game.settings.preferences.getInt(SettingsManager.TAG_HIGH_TIME, 0) == 0) {
            val noData = Label(game.locale.get("statistics.noData"), game.skin)
            noData.setAlignment(Align.center)
            best.add(noData).colspan(2).fillX().expandX().padTop(10f).padBottom(10f).row()
            return best
        }

        val highScore = game.settings.preferences.getInt(SettingsManager.TAG_HIGH_SCORE, 0)
        if (highScore > 0) append(best, game.locale["statistics.best.score"], highScore.toString())

        val highTime = game.settings.preferences.getInt(SettingsManager.TAG_HIGH_TIME, 0)
        if (highTime > 0) append(best, game.locale["statistics.best.time"], secondsToTime(highTime))

        return best
    }

    private fun addTotalData(): Table {
        val total = Table()
        val set = game.statistics.totalData

        total.add(Label(game.locale["statistics.total"], title)).colspan(2).row()

        /* No data case. */
        if (set.stats.isEmpty()) {
            val noData = Label(game.locale["statistics.noData"], game.skin)
            noData.setAlignment(Align.center)
            total.add(noData).colspan(2).fillX().expandX().padTop(10f).padBottom(10f).row()
            return total
        }

        /* This short lambda function appends a statistic to the table provided it is positive. */
        val appendStat: (String, Int?) -> Unit = { key, value ->
            value?.let { if (it > 0) append(total, key, it.toString()) }
        }

        /* Add all the possible statistics. */
        appendStat(game.locale["statistics.total.score"], set.stats["score"])
        appendStat(game.locale["statistics.total.combinations"], set.stats["combinations"])
        appendStat(game.locale["statistics.total.balls"], set.stats["balls"])
        appendStat(game.locale["statistics.total.games"], set.stats["games"])
        appendStat(game.locale["statistics.total.time"], set.stats["time"])
        appendStat(game.locale["statistics.total.cheats"], set.stats["cheats"])
        appendStat(game.locale["statistics.total.perfect"], set.stats["perfect"])

        return total
    }

    private fun addColorData(): Table {
        val color = Table()
        val stats = sortStatsMap(game.statistics.colorData.stats)

        color.add(Label(game.locale["statistics.color"], title)).colspan(stats.size).row()
        color.defaults().expandX().fillX().align(Align.center).size(60f).padTop(5f)

        if (stats.isEmpty()) {
            val noData = Label(game.locale["statistics.noData"], game.skin)
            noData.setAlignment(Align.center)
            color.add(noData).colspan(stats.size).fillX().expandX().padTop(10f).padBottom(10f).row()
            return color
        }

        /* Row 1: ball images. */
        for ((key, _) in stats) { color.add(Image(game.ballAtlas.findRegion("ball_$key"))) }

        color.row()

        /* Row 2: ball settings. */
        for ((_, value) in stats) {
            val label = Label(value.toString(), data)
            label.setAlignment(Align.center)
            color.add(label)
        }

        color.row()

        return color
    }

    private fun addSizesData(): Table {
        val sizes = Table()
        val stats = sortStatsMap(game.statistics.sizesData.stats)

        sizes.add(Label(game.locale.get("statistics.sizes"), this.title)).colspan(3).row()

        /* No data. */
        if (stats.isEmpty()) {
            val noData = Label(game.locale.get("statistics.noData"), game.skin)
            noData.setAlignment(Align.center)
            sizes.add(noData).colspan(3).fillX().expandX().padTop(10f).padBottom(10f).row()
            return sizes
        }

        val bar = game.skin.newDrawable("pixel", Color.WHITE)

        // Highest value is used to calculate the relative percentage of each row.
        val highestValue = stats.values.max() ?: 0

        for ((key, value) in stats) {
            val percentage = value.toFloat() / highestValue
            sizes.add(Label(key, data)).align(Align.left).fillX()
            sizes.add(Label(value.toString(), data)).align(Align.right).padRight(10f).expandX()
            sizes.add(Image(bar)).width(240 * percentage).padLeft(240 * (1 - percentage)).padBottom(5f).fill().row()
        }

        return sizes
    }

    /** Converts the decimal [seconds] number of seconds to a sexagesimal value. */
    private fun secondsToTime(seconds: Int): String {
        val hrs = seconds / 3600
        val min = seconds % 3600 / 60
        val sec = seconds % 3600 % 60
        return when {
            hrs != 0 -> String.format(Locale.getDefault(), "%d:%02d:%02d", hrs, min, sec)
            min != 0 -> String.format(Locale.getDefault(), "%d:%02d", min, sec)
            else -> String.format(Locale.getDefault(), "%d", sec)
        }
    }

    /** Sorts a [statistics] map by score, highest scores come first. */
    private fun sortStatsMap(statistics: Map<String, Int>): Map<String, Int> =
            statistics.toList().sortedBy { it.second }.reversed().toMap()

    /** Appends a statistic composed by the label [name] and the value [value] to a [table]. */
    private fun append(table: Table, name: String, value: String) {
        table.add(Label(name, data)).align(Align.left).fillX()
        table.add(Label(value, data)).align(Align.right).expandX().row()
    }
}
