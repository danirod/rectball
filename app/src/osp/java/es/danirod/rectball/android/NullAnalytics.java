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
