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
package es.danirod.rectball.screens

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align
import es.danirod.rectball.RectballGame
import es.danirod.rectball.SoundPlayer
import es.danirod.rectball.scene2d.listeners.ScreenPopper
import es.danirod.rectball.scene2d.ui.GameSummary

class GameOverScreen(game: RectballGame) : AbstractScreen(game) {

    private fun getHighScore() = game.statistics.highScore

    private fun isHighScore() = game.state.score >= getHighScore()

    private val replayButton by lazy {
        ImageTextButton(game.locale["game_over.play_again"], game.appSkin, "repeat").apply {
            addListener(ScreenPopper(game))
            label.setFontScale(0.9f)
        }
    }

    private val quitButton by lazy {
        ImageTextButton(game.locale["game_over.menu_screen"], game.appSkin, "leave").apply {
            imageCell.height(Value.percentHeight(0.75f, label))
            label.setFontScale(0.9f)
            addListener(ScreenPopper(game, true))
        }
    }

    override fun show() {
        super.show()

        table.setFillParent(false)

        val gameOver = Label(game.locale["game_over.time_up"], game.appSkin, "large", "white").apply {
            setAlignment(Align.center)
            setFontScale(0.8f)
        }
        val summary = GameSummary(game, isHighScore())
        table.add(gameOver).growX().row()
        table.add(summary).grow().row()
        table.add(replayButton).height(80f).minWidth(440f).maxWidth(640f).space(15f).row()
        table.add(quitButton).height(80f).minWidth(440f).maxWidth(640f).space(15f).row()
    }

    override fun escape() {
        game.player.playSound(SoundPlayer.SoundCode.FAIL)
        game.clearScreenStack()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        table.setSize(480f, 640f)
        table.setPosition(stage.width / 2, stage.height / 2, Align.center)
    }
}
