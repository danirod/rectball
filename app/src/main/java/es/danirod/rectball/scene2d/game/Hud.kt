package es.danirod.rectball.scene2d.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import es.danirod.rectball.Constants
import es.danirod.rectball.RectballGame

class Hud(game: RectballGame): Table() {

    val timer: TimerActor = TimerActor(Constants.SECONDS, game.skin)

    val score: ScoreActor = ScoreActor(game.skin)

    val help: ImageButton = ImageButton(game.skin, "blueHelp")

    val pause: ImageButton = ImageButton(game.skin, "blueCross")

    private val timerBorder = BorderedContainer(game.skin, timer)
    private val scoreBorder = BorderedContainer(game.skin, score)
    private val helpBorder = BorderedContainer(game.skin, help)
    private val pauseBorder = BorderedContainer(game.skin, pause)

    init {
        timer.isRunning = false

        composeLayout()
    }

    override fun sizeChanged() {
        super.sizeChanged()
        clear()
        composeLayout()
    }

    /**
     * Fill the HUD, which is the display that appears over the board with
     * all the information. The HUD is generated differently depending on
     * the aspect ratio of the device. If the device is 4:3, the HUD is
     * compressed to avoid making the board small. Otherwise, it's expanded
     * to the usual size.
     */
    private fun composeLayout() {
        if (aspectRatio < 1.5f) {
            add(helpBorder).size(50f).padBottom(10f)
            add(scoreBorder).height(50f).width(Constants.VIEWPORT_WIDTH.toFloat() / 2).space(10f).expandX().fillX()
            add(pauseBorder).size(50f).padBottom(10f)
            row()
            add(timerBorder).colspan(3).fillX().height(40f).padBottom(20f)
            row()
        } else {
            add(helpBorder).size(50f).spaceLeft(10f).padBottom(10f).align(Align.left)
            add(pauseBorder).size(50f).spaceRight(10f).padBottom(10f).align(Align.right)
            row()
            add(timerBorder).colspan(2).fillX().expandX().height(40f).padBottom(10f)
            row()
            add(scoreBorder).colspan(2).height(60f).width(Constants.VIEWPORT_WIDTH.toFloat() / 2).align(Align.center).padBottom(10f)
            row()
        }

        if (aspectRatio > 1.6f) {
            padBottom(20f)
        }
    }

    /** Returns true if the device is in landscape mode (wider than taller). */
    private val isLandscape: Boolean
        get() = Gdx.graphics.width > Gdx.graphics.height

    /** Returns the normalized aspect ratio of the screen, always greater than 1. */
    private val aspectRatio: Float
        get() = if (isLandscape) {
            Gdx.graphics.width.toFloat() / Gdx.graphics.height.toFloat()
        } else {
            Gdx.graphics.height.toFloat() / Gdx.graphics.width.toFloat()
        }
}