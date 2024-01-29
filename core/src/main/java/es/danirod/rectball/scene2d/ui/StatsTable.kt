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
package es.danirod.rectball.scene2d.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import es.danirod.rectball.RectballGame
import kotlin.time.Duration.Companion.seconds

class StatsTable(private val game: RectballGame, private val title: LabelStyle, private val data: LabelStyle) : Table() {

    private val context = game.context

    init {
        defaults().padBottom(30f)
        add(addBestScores()).fillX().expandX().row()
        add(addTotalData()).fillX().expandX().row()
        add(addColorData()).fillX().expandX().row()
        add(addSizesData()).fillX().expandX().row()
    }

    private fun addBestScores(): Table {
        val best = Table()

        best.add(Label(game.locale["statistics.best_data"], this.title)).colspan(2).row()

        if (game.statistics.highScore == 0L && game.statistics.highTime == 0L) {
            val noData = Label(game.locale["statistics.no_data"], game.appSkin)
            noData.setAlignment(Align.center)
            best.add(noData).colspan(2).fillX().expandX().padTop(10f).padBottom(10f).row()
            return best
        }

        val highScore = DecimalValue(game.statistics.highScore)
        if (highScore.hasValue()) append(best, game.locale["statistics.best_score"], highScore.toString())

        val highTime = TimeValue(game.statistics.highTime)
        if (highTime.hasValue()) append(best, game.locale["statistics.best_time"], highTime.toString())

        return best
    }

    private fun addTotalData(): Table {
        val total = Table()
        total.add(Label(game.locale["statistics.total_data"], title)).colspan(2).row()
        val data = totalStatistics
        if (data.isEmpty()) {
            val noData = Label(game.locale["statistics.no_data"], game.appSkin)
            noData.setAlignment(Align.center)
            total.add(noData).colspan(2).fillX().expandX().padTop(10f).padBottom(10f).row()
        } else {
            data.forEach { (key, value) -> append(total, key, value.toString()) }
        }
        return total
    }

    private fun addColorData(): Table {
        val color = Table()

        val stats = sortStatsMap(game.statistics.colorStatistics)

        if (stats.isEmpty()) {
            color.add(Label(game.locale["statistics.color_data"], title)).row()
            val noData = Label(game.locale["statistics.no_data"], game.appSkin)
            noData.setAlignment(Align.center)
            color.add(noData).fillX().expandX().padTop(10f).padBottom(10f).row()
        } else {
            color.add(Label(game.locale["statistics.color_data"], title)).colspan(stats.size).row()
            color.defaults().expandX().fillX().align(Align.center).size(60f).padTop(5f)

            for ((key, _) in stats) { color.add(Image(game.ballAtlas.findRegion("ball_$key"))) }
            color.row()
            for ((_, value) in stats) {
                val label = Label(value.toString(), data)
                label.setAlignment(Align.center)
                color.add(label)
            }
            color.row()
        }

        return color
    }

    private fun addSizesData(): Table {
        val sizes = Table()
        val stats = sortStatsMap(game.statistics.sizeStatistics)

        sizes.add(Label(game.locale["statistics.size_data"], this.title)).colspan(3).row()

        /* No data. */
        if (stats.isEmpty()) {
            val noData = Label(game.locale["statistics.no_data"], game.appSkin)
            noData.setAlignment(Align.center)
            sizes.add(noData).colspan(3).fillX().expandX().padTop(10f).padBottom(10f).row()
            return sizes
        }

        val bar = game.appSkin.newDrawable("pixel", Color.WHITE)

        // Highest value is used to calculate the relative percentage of each row.
        val highestValue = stats.first().second

        for ((key, value) in stats) {
            val percentage = value.toFloat() / highestValue
            sizes.add(Label(key, data)).align(Align.left).fillX()
            sizes.add(Label(value.toString(), data)).align(Align.right).padRight(10f).expandX()
            sizes.add(Image(bar)).width(240 * percentage).padLeft(240 * (1 - percentage)).padBottom(5f).fill().row()
        }

        return sizes
    }

    /** A map that pairs an statistic label into the value for that statistic label. */
    private val totalStatistics: Map<String, Value>
        get() = mapOf(
            game.locale["statistics.total_score"] to DecimalValue(game.statistics.totalScore),
            game.locale["statistics.total_combinations"] to DecimalValue(game.statistics.totalCombinations),
            game.locale["statistics.total_gems"] to DecimalValue(game.statistics.totalGems),
            game.locale["statistics.total_games"] to DecimalValue(game.statistics.totalGames),
            game.locale["statistics.total_time"] to TimeValue(game.statistics.totalTime),
            game.locale["statistics.total_perfect"] to DecimalValue(game.statistics.totalPerfects),
            game.locale["statistics.total_hints"] to DecimalValue(game.statistics.totalHints),
        ).filterValues { it.hasValue() }

    /** Sorts a [statistics] map by score, highest scores come first. */
    private fun sortStatsMap(statistics: Map<String, Long>) =
            statistics.toList().sortedBy { it.second }.reversed()

    /** Appends a statistic composed by the label [name] and the value [value] to a [table]. */
    private fun append(table: Table, name: String, value: String) {
        table.add(Label(name, data)).align(Align.left).fillX()
        table.add(Label(value, data)).align(Align.right).expandX().row()
    }

    private open class Value(val value: Long) {
        fun hasValue() = value > 0
    }

    private class DecimalValue(value: Long): Value(value) {
        override fun toString() = value.toString()
    }

    private class TimeValue(value: Long): Value(value) {
        override fun toString() = value.seconds.toComponents { hh, mm, ss, _ ->
            when {
                hh > 0 -> String.format("%d:%02d:%02d h", hh, mm, ss)
                mm > 0 -> String.format("%d:%02d m", mm, ss)
                else -> String.format("%d s", ss)
            }
        }
    }
}
