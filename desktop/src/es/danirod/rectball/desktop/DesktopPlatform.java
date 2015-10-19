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

package es.danirod.rectball.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import es.danirod.rectball.platform.*;
import es.danirod.rectball.platform.analytics.AnalyticServices;
import es.danirod.rectball.platform.scores.LegacyScoreServices;
import es.danirod.rectball.platform.scores.ScoreServices;
import es.danirod.rectball.platform.sharing.SharingServices;
import es.danirod.rectball.platform.statistics.LegacyStatisticsServices;
import es.danirod.rectball.platform.statistics.StatisticsServices;

/**
 * This contains code for desktop platform. Here code that uses desktop JRE
 * or APIs such as Swing can be used. This code won't run on any other
 * platform.
 *
 * @author danirod
 * @since 0.4.0
 */
public class DesktopPlatform implements Platform {

    private SharingServices sharing;

    private AnalyticServices analytic;

    private ScoreServices score;

    private Preferences preferences;

    private StatisticsServices statistics;

    protected DesktopPlatform() {
        sharing = new DesktopSharingServices();
        analytic = new DesktopAnalyticServices();
        score = new LegacyScoreServices() {
            @Override
            protected FileHandle getScoresFile() {
                if (SharedLibraryLoader.isWindows) {
                    String location = System.getenv("AppData") + "/rectball/scores";
                    return Gdx.files.absolute(location);
                } else if (SharedLibraryLoader.isLinux) {
                    return Gdx.files.external(".rectball/scores");
                } else if (SharedLibraryLoader.isMac) {
                    return Gdx.files.external("/Library/Application Support/rectball/scores");
                } else {
                    return Gdx.files.local("scores");
                }
            }
        };
        preferences = new LwjglPreferences("rectball", ".prefs/");
        statistics = new LegacyStatisticsServices() {
            @Override
            protected FileHandle getStatistics() {
                if (SharedLibraryLoader.isWindows) {
                    String location = System.getenv("AppData") + "/rectball/stats";
                    return Gdx.files.absolute(location);
                } else if (SharedLibraryLoader.isLinux) {
                    return Gdx.files.external(".rectball/stats");
                } else if (SharedLibraryLoader.isMac) {
                    return Gdx.files.external("/Library/Application Support/rectball/stats");
                } else {
                    return Gdx.files.local("stats");
                }
            }
        };
    }

    @Override
    public SharingServices sharing() {
        return sharing;
    }

    @Override
    public AnalyticServices analytic() {
        return analytic;
    }

    @Override
    public ScoreServices score() {
        return score;
    }

    @Override
    public Preferences preferences() {
        return preferences;
    }

    @Override
    public StatisticsServices statistics() {
        return statistics;
    }
}
