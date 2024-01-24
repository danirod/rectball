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

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import es.danirod.rectball.Constants
import es.danirod.rectball.RectballGame

class Hud(game: RectballGame) : Table() {

    val timer: TimerActor = TimerActor(Constants.SECONDS, game.appSkin)

    val score: ScoreActor = ScoreActor(game.appSkin)

    val help: ImageButton = ImageButton(game.appSkin, "help").apply {
        isTransform = true
        pad(5f)
        imageCell.size(35f)
    }

    val pause: ImageButton = ImageButton(game.appSkin, "close").apply {
        isTransform = true
        pad(5f)
        imageCell.size(30f)
    }

    init {
        timer.isRunning = false

        add(help).size(60f)
        add(score).growX().fillY().spaceLeft(30f).spaceRight(30f)
        add(pause).size(60f)
        row()
        add(timer).colspan(3).growX().fillY()
            .width(Value.percentWidth(1f, this)).height(30f).spaceTop(10f)
        pack()
    }
}
