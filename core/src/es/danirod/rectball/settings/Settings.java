package es.danirod.rectball.settings;

import com.badlogic.gdx.Preferences;

public class Settings {

    /** The preferences instance for saving these preferences. */
    private Preferences prefs;

    /** Whether music is enabled or not. */
    private boolean music;

    /** Whether sound is enabled or not. */
    private boolean sound;

    /** Whether colorblind mode is on or not. */
    private boolean colorblind;

    public Settings(Preferences prefs) {
        this.prefs = prefs;
        this.music = prefs.getBoolean("music", true);
        this.sound = prefs.getBoolean("sound", true);
        this.colorblind = prefs.getBoolean("colorblind", false);
    }

    public void save() {
        prefs.putBoolean("music", music);
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("colorblind", colorblind);
        prefs.flush();
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isColorblind() {
        return colorblind;
    }

    public void setColorblind(boolean colorblind) {
        this.colorblind = colorblind;
    }
}
