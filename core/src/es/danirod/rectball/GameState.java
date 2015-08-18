package es.danirod.rectball;

import com.badlogic.gdx.Preferences;

public class GameState {

    public boolean soundEnabled;

    public boolean musicEnabled;

    public boolean colorblindMode;

    public long highScore;

    public long lastScore;

    public GameState(Preferences preferences) {
        soundEnabled = preferences.getBoolean("soundEnabled", true);
        musicEnabled = preferences.getBoolean("musicEnabled", true);
        colorblindMode = preferences.getBoolean("colorblindMode", false);
        highScore = preferences.getLong("highScore", 0);
        lastScore = preferences.getLong("lastScore", 0);
    }

    public void save(Preferences preferences) {
        preferences.putBoolean("soundEnabled", soundEnabled);
        preferences.putBoolean("musicEnabled", musicEnabled);
        preferences.putBoolean("colorblindMode", colorblindMode);
        preferences.putLong("highScore", highScore);
        preferences.putLong("lastScore", lastScore);
        preferences.flush();
    }
}
