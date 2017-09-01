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
package es.danirod.rectball.android

internal class NullAnalytics : Analytics {

    override fun sendEvent(category: String, action: String) {
        Gdx.app.log("NullAnalytics", String.format("Event { category=%s, action=%s }", category, action))
    }

    override fun sendEvent(category: String, action: String, label: String) {
        Gdx.app.log("NullAnalytics", String.format("Event { category=%s, action=%s, label=%s }", category, action, label))
    }

    override fun sendScreen(screenID: String) {
        Gdx.app.log("NullAnalytics", String.format("Event { screen=%s }", screenID))
    }

    override fun sendEventWithDimensions(category: String, action: String, dimensions: Map<Int, String>) {
        Gdx.app.log("NullAnalytics", String.format("Event { category=%s, action=%s, dimensions=%s }", category, action, dimensions))
    }

    override fun sendEventWithMetrics(category: String, action: String, metrics: Map<Int, Float>) {
        Gdx.app.log("NullAnalytics", String.format("Event { category=%s, action=%s, metrics=%s }", category, action, metrics))
    }

    override fun sendEventWithDimensionsAndMetrics(category: String, action: String, dimensions: Map<Int, String>, metrics: Map<Int, Float>) {
        Gdx.app.log("NullAnalytics", String.format("Event { category=%s, action=%s, dimensions=%s, metrics=%s }", category, action, dimensions, metrics))
    }

}
