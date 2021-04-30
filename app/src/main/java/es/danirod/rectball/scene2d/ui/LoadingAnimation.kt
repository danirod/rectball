package es.danirod.rectball.scene2d.ui

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import es.danirod.rectball.model.Ball
import es.danirod.rectball.model.BallColor
import es.danirod.rectball.scene2d.game.BallActor

class LoadingAnimation(atlas: TextureAtlas) : Table() {

    val balls = BallColor.values().map {
        val ball = Ball();
        ball.color = it
        BallActor(ball, atlas).apply {
            setColoured(true)
        }
    }

    init {
        add(balls[0])
        add(balls[1])
        row()
        add(balls[2])
        add(balls[3])
    }

    fun animate() {
        stage.addAction(Actions.sequence(moveBalls(), swapBalls(), Actions.run { animate() }))
    }

    fun syncColors() = balls.forEach { it.syncColor() }

    private fun moveBalls() = Actions.run {
        balls[0].addAction(Actions.moveBy(balls[0].width, 0f, 0.15f))
        balls[1].addAction(Actions.moveBy(0f, -balls[1].height, 0.15f))
        balls[2].addAction(Actions.moveBy(0f, balls[2].height, 0.15f))
        balls[3].addAction(Actions.moveBy(-balls[3].width, 0f, 0.15f))
    }

    private fun swapBalls() = Actions.delay(0.3f, Actions.run {
        /* Swap the colors of the balls once they are reset to the old position. */
        val colors = balls.map { it.ball.color }
        balls[0].ball.color = colors[2]
        balls[1].ball.color = colors[0]
        balls[2].ball.color = colors[3]
        balls[3].ball.color = colors[1]

        /* Restore the original position of the balls. */
        balls[0].addAction(Actions.moveTo(0f, balls[0].height))
        balls[1].addAction(Actions.moveTo(balls[1].width, balls[1].height))
        balls[2].addAction(Actions.moveTo(0f, 0f))
        balls[3].addAction(Actions.moveTo(balls[3].width, 0f))

        syncColors()
    })
}