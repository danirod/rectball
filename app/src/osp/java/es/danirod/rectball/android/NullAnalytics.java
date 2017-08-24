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
package es.danirod.rectball.android;

import com.badlogic.gdx.Gdx;

import java.util.Map;

import es.danirod.rectball.platform.Analytics;

class NullAnalytics implements Analytics {
    @Override
    public void sendEvent(String category, String action) {
        Gdx.app.log("NullAnalytics", String.format("Event { category=%s, action=%s }", category, action));
    }

    @Override
    public void sendEvent(String category, String action, String label) {
        Gdx.app.log("NullAnalytics", String.format("Event { category=%s, action=%s, label=%s }", category, action, label));
    }

    @Override
    public void sendScreen(String screenID) {
        Gdx.app.log("NullAnalytics", String.format("Event { screen=%s }", screenID));
    }

    @Override
    public void sendEventWithDimensions(String category, String action, Map<Integer, String> dimensions) {
        Gdx.app.log("NullAnalytics", String.format("Event { category=%s, action=%s, dimensions=%s }", category, action, dimensions));
    }

    @Override
    public void sendEventWithMetrics(String category, String action, Map<Integer, Float> metrics) {
        Gdx.app.log("NullAnalytics", String.format("Event { category=%s, action=%s, metrics=%s }", category, action, metrics));
    }

    @Override
    public void sendEventWithDimensionsAndMetrics(String category, String action, Map<Integer, String> dimensions, Map<Integer, Float> metrics) {
        Gdx.app.log("NullAnalytics", String.format("Event { category=%s, action=%s, dimensions=%s, metrics=%s }", category, action, dimensions, metrics));
    }
}
