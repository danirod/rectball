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

import android.content.Context;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidPreferences;
import com.badlogic.gdx.files.FileHandle;
import es.danirod.rectball.platform.*;
import es.danirod.rectball.platform.LegacyScores;
import es.danirod.rectball.platform.Scores;
import es.danirod.rectball.platform.Sharing;
import es.danirod.rectball.platform.LegacyStatistics;
import es.danirod.rectball.platform.Statistics;

/**
 * This contains code for the Android platform. Here code that uses Android
 * SDK or Android API might be used. This code won't run on other platforms
 * than Android.
 *
 * @author danirod
 * @since 0.4.0
 */
public class AndroidPlatform implements Platform {

    private final Sharing sharing;

    private final Scores score;

    private final Preferences preferences;

    private final Statistics statistics;

    private final AndroidApplication app;

    protected AndroidPlatform(AndroidApplication app) {
        this.app = app;

        sharing = new AndroidSharing(app);
        score = new LegacyScores() {
            @Override
            protected FileHandle getScoresFile() {
                return Gdx.files.local("scores");
            }
        };
        statistics = new LegacyStatistics() {
            @Override
            protected FileHandle getStatistics() {
                return Gdx.files.local("stats");
            }
        };
        preferences = new AndroidPreferences(app.getSharedPreferences("rectball", Context.MODE_PRIVATE));
    }

    @Override
    public Sharing sharing() {
        return sharing;
    }

    @Override
    public Scores score() {
        return score;
    }

    @Override
    public Preferences preferences() {
        return preferences;
    }

    @Override
    public Statistics statistics() {
        return statistics;
    }

    @Override
    public void toast(final CharSequence msg) {
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(app, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
