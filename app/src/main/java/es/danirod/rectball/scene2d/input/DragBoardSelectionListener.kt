package es.danirod.rectball.scene2d.input

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import es.danirod.rectball.model.Bounds
import es.danirod.rectball.model.Coordinate
import es.danirod.rectball.scene2d.game.BallActor
import es.danirod.rectball.scene2d.game.BoardActor
import java.lang.Math.min

class DragBoardSelectionListener(val board: BoardActor) : InputListener() {

    private var active: Boolean = false

    private var startX: Int = 0

    private var startY: Int = 0

    private var minX: Int = 0

    private var minY: Int = 0

    private var maxX: Int = 0

    private var maxY: Int = 0

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        val touched = board.hit(x, y, true)
        return if (pointer == 0 && button == 0 && touched is BallActor) {
            startX = touched.ball.x
            startY = touched.ball.y
            true
        } else {
            super.touchDown(event, x, y, pointer, button)
        }
    }

    override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
        val touched = board.hit(x, y, true)
        if (touched is BallActor) {
            if (active) {
                tintSelection(Color.WHITE)
            }
            computeBounds(touched)
            tintSelection(Color.GRAY)
        }
    }

    override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
        val touched = board.hit(x, y, true)
        if (touched is BallActor) {
            computeBounds(touched)
        }
        if (active) {
            tintSelection(Color.WHITE)
            board.select(Bounds(minX, minY, maxX, maxY))
            active = false
        }
    }

    private fun computeBounds(touched: BallActor) {
        val endX: Int = touched.ball.x
        val endY: Int = touched.ball.y
        minX = startX.coerceAtMost(endX)
        maxX = startX.coerceAtLeast(endX)
        minY = startY.coerceAtMost(endY)
        maxY = startY.coerceAtLeast(endY)
        active = true
    }

    private fun tintSelection(color: Color) {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                board.getBall(x, y).color = color
            }
        }
    }
}