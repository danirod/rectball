package es.danirod.rectball.scene2d.ui

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import es.danirod.rectball.RectballGame
import es.danirod.rectball.android.R
import kotlin.time.Duration.Companion.seconds

class GameSummary(game: RectballGame, highScore: Boolean) : Table() {

    private val lastScoreValue = game.state.score.toString()

    private val stateDuration = game.state.elapsedTime.toInt().seconds.toComponents { mm, ss, _ -> String.format("%d:%02d", mm, ss)}

    private val clock = Image(game.appSkin, "icon_time")
    private val crown = Image(game.appSkin, "icon_crown").apply {
        setScaling(Scaling.fit)
    }

    private val lastScore = Label(lastScoreValue, game.appSkin, "monospace2").apply {
        setFontScale(10f)
    }

    private val duration = Label(stateDuration, game.appSkin, "large", "white").apply {
        setFontScale(0.9f)
    }

    val newRecord = Label(game.context.getString(R.string.game_over_you_made_high_score), game.appSkin, "normal", "white").apply {
        wrap = true
        setAlignment(Align.center)
    }

    init {
        defaults().space(10f)
        add(lastScore).colspan(2).center().row()
        add(clock).size(30f).expandX().right()
        add(duration).expandX().left().row()

        if (highScore) {
            add(crown).colspan(2).expandX().spaceTop(30f).row()
            add(newRecord).colspan(2).growX().row()
        }
    }

}