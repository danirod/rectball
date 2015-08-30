package es.danirod.rectball.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import es.danirod.rectball.RectballGame;

public class SoundPlayer {

    public enum SoundCode {
        GAME_OVER("sound/gameover.ogg"),
        SELECT("sound/select.ogg"),
        UNSELECT("sound/unselect.ogg"),
        SUCCESS("sound/success.ogg"),
        FAIL("sound/fail.ogg");

        protected final String internalPath;

        SoundCode(String path) {
            internalPath = path;
        }
    }

    private RectballGame game;

    public SoundPlayer(RectballGame game) {
        this.game = game;
    }

    public void playSound(SoundCode code) {
        if (!game.settings.isSoundEnabled()) {
            return;
        }

        Sound sound = game.manager.get(code.internalPath, Sound.class);
        float randomPitch = MathUtils.random(0.7f, 1.3f);
        sound.play(0.7f, randomPitch, 0);
    }
}
