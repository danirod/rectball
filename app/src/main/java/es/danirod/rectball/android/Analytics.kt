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

package es.danirod.rectball.android

/**
 * This interface provides analytic services integration. Analytic services
 * allow to get information about the game. This can be used, for instance,
 * for getting information about the game during an error report.
 *
 * @author danirod
 * @since 0.4.0
 */
interface Analytics {

    fun sendEvent(category: String, action: String)

    fun sendEvent(category: String, action: String, label: String)

    fun sendScreen(screenID: String)

    fun sendEventWithDimensions(category: String, action: String, dimensions: Map<Int, String>)

    fun sendEventWithMetrics(category: String, action: String, metrics: Map<Int, Float>)

    fun sendEventWithDimensionsAndMetrics(category: String, action: String, dimensions: Map<Int, String>, metrics: Map<Int, Float>)
}
