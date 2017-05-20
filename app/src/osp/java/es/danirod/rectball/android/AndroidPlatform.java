package es.danirod.rectball.android;

import es.danirod.rectball.platform.Analytics;
import es.danirod.rectball.platform.GoogleServices;

/**
 * Created by danirod on 20/5/17.
 */

class AndroidPlatform extends AndroidPlatformBase {

    private NullAnalytics analytics = new NullAnalytics();

    private NullGameServices services = new NullGameServices();

    protected AndroidPlatform(AndroidLauncher app) {
        super(app);
    }

    @Override
    public Analytics analytic() {
        return analytics;
    }

    @Override
    public GoogleServices google() {
        return services;
    }
}
