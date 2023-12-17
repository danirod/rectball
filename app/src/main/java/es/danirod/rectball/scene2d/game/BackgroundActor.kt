package es.danirod.rectball.scene2d.game

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import es.danirod.rectball.model.Ball
import es.danirod.rectball.model.BallColor

class BackgroundActor(private val atlas: TextureAtlas) : Group() {

    override fun sizeChanged() {
        val rows = height.toInt() / 64 + 1
        val cols = width.toInt() / 64
        invalidate(rows, cols)
    }

    private fun randomColor(): BallColor {
        val randomColor = MathUtils.random(BallColor.entries.size - 1)
        return BallColor.entries[randomColor]
    }

    private fun invalidate(rows: Int, cols: Int) {
        // Remove all the existing balls
        clearChildren()

        for (j in -1..rows) {
            for (i in -1..cols) {
                val ball = Ball(i, j).apply { color = randomColor() }
                val actor = BallActor(ball, atlas).apply {
                    setColoured(true)
                }
                actor.setBounds(64f * i, 64f * j, 64f, 64f)
                actor.setScale(0.85f)
                actor.addAction(beatAction())
                actor.addAction(moveAction())
                actor.addAction(repositionAction(actor, 64f * rows, 64f * (rows + 1)))
                addActor(actor)
            }
        }
    }

    private fun beatAction(): Action = Actions.forever(
            Actions.sequence(
                    Actions.scaleTo(0.75f, 0.75f,2f),
                    Actions.scaleTo(0.95f, 0.95f, 2f),
            )
    )

    private fun moveAction(): Action = Actions.forever(
            Actions.moveBy(0f, 64f, 4f)
    )

    private fun repositionAction(target: BallActor, maxHeight: Float, jumpBy: Float) = Actions.forever(
            Actions.run {
                // Also, change the color
                target.ball.color = randomColor()
                if (target.y >= maxHeight)
                    target.y -= jumpBy
            }
    )
}