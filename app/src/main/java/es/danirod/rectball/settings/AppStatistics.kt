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

import es.danirod.rectball.settings.PreferenceContainer.LongPreference
import es.danirod.rectball.settings.PreferenceContainer.MapStringLongPreference
import com.badlogic.gdx.Preferences as GdxPreferences

class AppStatistics(override val preferences: GdxPreferences) : PreferenceContainer {
    var highScore by LongPreference("statistics.high_score")
    var highTime by LongPreference("statistics.high_time")
    var totalScore by LongPreference("statistics.total_score")
    var totalCombinations by LongPreference("statistics.total_movements")
    var totalGems by LongPreference("statistics.total_gems")
    var totalGames by LongPreference("statistics.total_games")
    var totalTime by LongPreference("statistics.total_time")
    var totalPerfects by LongPreference("statistics.total_perfects")
    var totalHints by LongPreference("statistics.total_hints")
    var colorStatistics by MapStringLongPreference("statistics.by_color")
    var sizeStatistics by MapStringLongPreference("statistics.by_size")
}
