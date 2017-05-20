package es.danirod.rectball;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class SoundPlayer {

    private RectballGame game;

    public SoundPlayer(RectballGame game) {
        this.game = game;
    }

    public void playSound(SoundCode code) {
        if (canPlaySound()) {
            Sound sound = game.manager.get(code.internalPath);
            float pitch = MathUtils.random(0.7f, 1.3f);
            sound.play(0.7f, pitch, 0);
        }
    }

    private boolean canPlaySound() {
        return game.getPlatform().preferences().getBoolean("sound", true);
    }

    public static enum SoundCode {
        GAME_OVER("sound/game_over.ogg"),
        SELECT("sound/select.ogg"),
        UNSELECT("sound/unselect.ogg"),
        SUCCESS("sound/success.ogg"),
        FAIL("sound/fail.ogg"),
        PERFECT("sound/perfect.ogg");

        String internalPath;
        SoundCode(String internalPath) {
            this.internalPath = internalPath;
        }
    }
}