package es.danirod.rectball.scene2d.input

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import es.danirod.rectball.scene2d.game.BallActor
import es.danirod.rectball.scene2d.game.BoardActor

class ClassicBoardSelectionListener(val board: BoardActor) : InputListener() {

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        val targetActor = board.hit(x, y, true)
        return if (targetActor is BallActor) {
            targetActor.isSelected = !targetActor.isSelected
            true
        } else false
    }

}