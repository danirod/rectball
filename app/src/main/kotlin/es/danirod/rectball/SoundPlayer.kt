package es.danirod.rectball

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils

class SoundPlayer(private val game: RectballGame) {

    fun playSound(code: SoundCode) {
        if(canPlaySound()) {
            val sound: Sound = game.manager.get(code.internalPath)
            val pitch = MathUtils.random(0.7f, 1.3f)
            sound.play(0.7f, pitch, 0f)
        }
    }

    fun canPlaySound() = game.platform.preferences().getBoolean("sound", true)

    enum class SoundCode constructor(val internalPath: String) {
        GAME_OVER("sound/game_over.ogg"),
        SELECT("sound/select.ogg"),
        UNSELECT("sound/unselect.ogg"),
        SUCCESS("sound/success.ogg"),
        FAIL("sound/fail.ogg"),
        PERFECT("sound/perfect.ogg");
    }
}