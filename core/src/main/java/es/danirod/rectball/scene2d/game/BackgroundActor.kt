/* This file is part of Rectball
 * Copyright (C) 2015-2024  Dani Rodríguez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.scene2d.game

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
import es.danirod.rectball.model.Ball
import es.danirod.rectball.model.BallColor
import es.danirod.rectball.utils.AlwaysGrowingMatrix

class BackgroundActor(private val atlas: TextureAtlas) : Group() {

    private val size = 48f

    private val actors = AlwaysGrowingMatrix { x, y ->
        val ball = Ball(x, y).apply {
            val randomColor = MathUtils.random(BallColor.entries.size - 1)
            color = BallColor.entries[randomColor]
        }
        BallActor(ball, atlas).apply {
            setColoured(true)
            setSize(size, size)
            setPosition(size * x, size * y)
            layout()
        }
    }

    private val elements = Group()

    init {
        addActor(elements)
        elements.addAction(Actions.forever(moveAction()))
    }

    private fun moveAction(): Action = object : TemporalAction(2f) {
        override fun update(percent: Float) {
            actor.y = -size / 2 + size * percent
        }

        override fun begin() {
            swapLines()
        }
    }

    override fun sizeChanged() {
        invalidate()
        elements.setSize(width, height)
    }

    private fun test() {
        println("$rows x $cols")
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                print(actors.get(x, y).ball.color.name[0])
            }
            print("\n")
        }
    }

    fun swapLines() {
        /* Take elements from the last line. */
        val lastLine = (0 until cols).map { i -> actors.get(i, rows - 1).ball.color }

        /* Move items one line up. */
        for (y in (rows - 1) downTo 1 ) {
            for (x in 0 until cols) {
                actors.get(x, y).ball.color = actors.get(x, y - 1).ball.color
                actors.get(x, y).syncColor()
            }
        }

        /* Then the last line. */
        (0 until cols).map { i ->
            actors.get(i, 0).ball.color = lastLine[i]
            actors.get(i, 0).syncColor()
        }
    }

    /** How many rows should there be in this actor according to its size. */
    private val rows
        get() = MathUtils.ceil(height / size)

    /** How many columsn should there be in this actor according to its size. */
    private val cols
        get() = MathUtils.ceil(width / size)

    private fun invalidate() {
        if (rows == 0 || cols == 0) {
            return
        }

        elements.clearChildren()
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                elements.addActor(actors.get(x, y))
            }
        }
    }

    override fun act(delta: Float) {
        super.act(delta)

        /* Update the heartbeat effect of the children. (I am not using actions
         * for this, since the background could be invalidated and new children
         * could be added at any time and I found too difficult to synchronize
         * new actions with existing ones.) */
        beatTicks += delta
        while (beatTicks >= 4)
            beatTicks -= 4
        val scale = Interpolation.sine.apply(0.75f, 0.95f, beatTicks / 2f)
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                actors.get(x, y).setScale(scale)
            }
        }
    }

    private var beatTicks = 0f
}
