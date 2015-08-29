package es.danirod.rectball.utils;

import com.badlogic.gdx.audio.Sound;
import es.danirod.rectball.RectballGame;

public class SoundPlayer {

    private Sound gameover;

    private Sound select;

    private Sound unselect;

    private Sound success;

    private Sound fail;

    private boolean soundEnabled;

    public SoundPlayer(RectballGame game) {
        soundEnabled = game.settings.isSoundEnabled();
        gameover = game.manager.get("sound/gameover.wav");
        select = game.manager.get("sound/select.wav");
        unselect = game.manager.get("sound/unselect.wav");
        success = game.manager.get("sound/success.wav");
        fail = game.manager.get("sound/fail.wav");
    }

    public void playGameOver() {
        if (soundEnabled) {
            gameover.play();
        }
    }

    public void playSelect() {
        if (soundEnabled) {
            select.play();
        }
    }

    public void playUnselect() {
        if (soundEnabled) {
            unselect.play();
        }
    }

    public void playSuccess() {
        if (soundEnabled) {
            success.play();
        }
    }

    public void playFail() {
        if (soundEnabled) {
            fail.play();
        }
    }
}
