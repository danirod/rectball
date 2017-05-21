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

import android.content.Intent;

import es.danirod.rectball.platform.Analytics;
import es.danirod.rectball.platform.GoogleServices;

/**
 * This contains code for the Android platform. Here code that uses Android
 * SDK or Android API might be used. This code won't run on other platforms
 * than Android.
 *
 * @author danirod
 * @since 0.4.0
 */
class AndroidPlatform extends AndroidPlatformBase {

    private final AndroidGoogleServices google;

    private final AndroidAnalytics analytic;

    private final AndroidLauncher application;

    protected AndroidPlatform(AndroidLauncher app) {
        super(app);
        this.application = app;
        google = new AndroidGoogleServices(app);
        analytic = new AndroidAnalytics(app);
    }

    @Override
    public GoogleServices google() {
        return google;
    }

    @Override
    public Analytics analytic() {
        return analytic;
    }

    @Override
    public void onStart() {
        google.gameHelper.onStart(this.application);
    }

    @Override
    public void onStop() {
        google.gameHelper.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        google.gameHelper.onActivityResult(requestCode, resultCode, data);
    }
}
