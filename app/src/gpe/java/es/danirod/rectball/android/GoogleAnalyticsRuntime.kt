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

import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker

/**
 * Android implementation for the analytic services. This is a dummy
 * implementation because at the moment no analytic services are being used
 * in this version. However, it's open for anybody to use.
 *
 * @author danirod
 * @since 0.4.0
 */
internal class GoogleAnalyticsRuntime(context: AndroidLauncher) : Analytics {

    private val tracker: Tracker

    init {
        val analytics = GoogleAnalytics.getInstance(context)
        analytics.setDryRun(BuildConfig.ANALYTICS_DRY_RUN)
        tracker = analytics.newTracker(R.xml.global_tracker)
        tracker.enableExceptionReporting(true)
    }

    override fun sendScreen(screenID: String) {
        tracker.setScreenName(screenID)
        tracker.send(HitBuilders.ScreenViewBuilder().build())
    }

    override fun sendEvent(category: String, action: String) {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build())
    }

    override fun sendEvent(category: String, action: String, label: String) {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build())
    }

    override fun sendEventWithDimensions(category: String, action: String, dimensions: Map<Int, String>) {
        val event = HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
        for ((key, value) in dimensions) {
            event.setCustomDimension(key, value)
        }
        tracker.send(event.build())
    }

    override fun sendEventWithMetrics(category: String, action: String, metrics: Map<Int, Float>) {
        val event = HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
        for ((key, value) in metrics) {
            event.setCustomMetric(key, value)
        }
        tracker.send(event.build())
    }

    override fun sendEventWithDimensionsAndMetrics(category: String, action: String, dimensions: Map<Int, String>, metrics: Map<Int, Float>) {
        val event = HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
        for ((key, value) in dimensions) {
            event.setCustomDimension(key, value)
        }
        for ((key, value) in metrics) {
            event.setCustomMetric(key, value)
        }
        tracker.send(event.build())
    }
}
