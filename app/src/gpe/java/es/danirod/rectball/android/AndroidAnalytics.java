/*
 * This file is part of Rectball
 * Copyright (C) 2015 Dani Rodríguez
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

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import es.danirod.rectball.platform.Analytics;

import java.util.Map;

/**
 * Android implementation for the analytic services. This is a dummy
 * implementation because at the moment no analytic services are being used
 * in this version. However, it's open for anybody to use.
 *
 * @author danirod
 * @since 0.4.0
 */
class AndroidAnalytics implements Analytics {

    private AndroidLauncher app;

    private Tracker tracker;

    public AndroidAnalytics(AndroidLauncher app) {
        this.app = app;

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(app);
        analytics.setDryRun(BuildConfig.ANALYTICS_DRY_RUN);
        tracker = analytics.newTracker(R.xml.global_tracker);
        tracker.enableExceptionReporting(true);
    }

    @Override
    public void sendScreen(String screenID) {
        tracker.setScreenName(screenID);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void sendEvent(String category, String action) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }

    @Override
    public void sendEvent(String category, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    @Override
    public void sendEventWithDimensions(String category, String action, Map<Integer, String> dimensions) {
        HitBuilders.EventBuilder event = new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action);
        for (Map.Entry<Integer, String> dimension : dimensions.entrySet()) {
            event.setCustomDimension(dimension.getKey(), dimension.getValue());
        }
        tracker.send(event.build());
    }

    @Override
    public void sendEventWithMetrics(String category, String action, Map<Integer, Float> metrics) {
        HitBuilders.EventBuilder event = new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action);
        for (Map.Entry<Integer, Float> dimension : metrics.entrySet()) {
            event.setCustomMetric(dimension.getKey(), dimension.getValue());
        }
        tracker.send(event.build());
    }

    @Override
    public void sendEventWithDimensionsAndMetrics(String category, String action, Map<Integer, String> dimensions, Map<Integer, Float> metrics) {
        HitBuilders.EventBuilder event = new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action);
        for (Map.Entry<Integer, String> dimension : dimensions.entrySet()) {
            event.setCustomDimension(dimension.getKey(), dimension.getValue());
        }
        for (Map.Entry<Integer, Float> dimension : metrics.entrySet()) {
            event.setCustomMetric(dimension.getKey(), dimension.getValue());
        }
        tracker.send(event.build());
    }
}
