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
