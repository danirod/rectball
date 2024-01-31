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
package es.danirod.rectball.scene2d.ui

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import es.danirod.rectball.RectballGame
import es.danirod.rectball.model.GameState
import kotlin.time.Duration.Companion.seconds

class GameSummary(game: RectballGame, state: GameState, highScore: Boolean) : Table() {

    private val lastScoreValue = state.score.toString()

    private val stateDuration = state.elapsedTime.toInt().seconds.toComponents { mm, ss, _ -> String.format("%d:%02d", mm, ss)}

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

    val newRecord = Label(game.locale["game_over.new_high_score"], game.appSkin, "normal", "white").apply {
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
