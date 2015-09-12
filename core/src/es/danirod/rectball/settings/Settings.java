/*
 * This file is part of Rectball.
 * Copyright (C) 2015 Dani Rodríguez.
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
package es.danirod.rectball.settings;

import com.badlogic.gdx.Preferences;

public class Settings {

    /** The preferences instance for saving these preferences. */
    private Preferences prefs;

    /** Whether music is enabled or not. */
    private boolean musicEnabled;

    /** Whether sound is enabled or not. */
    private boolean soundEnabled;

    /** Whether colorblind mode is on or not. */
    private boolean colorblind;

    public Settings(Preferences prefs) {
        this.prefs = prefs;
        this.musicEnabled = prefs.getBoolean("music", true);
        this.soundEnabled = prefs.getBoolean("sound", true);
        this.colorblind = prefs.getBoolean("colorblind", false);
    }

    public void save() {
        prefs.putBoolean("music", musicEnabled);
        prefs.putBoolean("sound", soundEnabled);
        prefs.putBoolean("colorblind", colorblind);
        prefs.flush();
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setMusicEnabled(boolean musicEnabled) {
        this.musicEnabled = musicEnabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public boolean isColorblind() {
        return colorblind;
    }

    public void setColorblind(boolean colorblind) {
        this.colorblind = colorblind;
    }
}
