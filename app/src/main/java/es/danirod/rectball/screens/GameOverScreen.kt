/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2023 Dani Rodríguez.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align
import es.danirod.rectball.RectballGame
import es.danirod.rectball.SoundPlayer
import es.danirod.rectball.android.R
import es.danirod.rectball.android.settings.SettingsManager
import es.danirod.rectball.scene2d.listeners.ScreenPopper
import es.danirod.rectball.scene2d.ui.GameSummary

class GameOverScreen(game: RectballGame) : AbstractScreen(game, true) {

    private fun getHighScore() = game.context.settings.preferences.getLong(SettingsManager.TAG_HIGH_SCORE, 0L)

    private fun isHighScore() = game.state.score >= getHighScore()

    public override fun setUpInterface(table: Table) {
        val gameOver = Label(game.context.getString(R.string.game_over_title), game.appSkin, "large", "white").apply {
            setAlignment(Align.center)
            setFontScale(0.8f)
        }
        val summary = GameSummary(game, isHighScore())
        table.add(gameOver).growX().row()
        table.add(summary).grow().row()
        table.add(replayButton).height(80f).growX().space(15f).row()
        table.add(quitButton).height(80f).growX().space(15f).row()
    }

    private val replayButton by lazy {
        ImageTextButton(game.context.getString(R.string.game_over_play_again), game.appSkin, "repeat").apply {
            addListener(ScreenPopper(game))
            label.setFontScale(0.9f)
        }
    }

    private val quitButton by lazy {
        ImageTextButton(game.context.getString(R.string.game_over_menu_screen), game.appSkin, "leave").apply {
            imageCell.height(Value.percentHeight(0.75f, label))
            label.setFontScale(0.9f)
            addListener(ScreenPopper(game, true))
        }
    }

    override fun show() {
        super.show()
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(getStage())
        multiplexer.addProcessor(GameOverInputProcessor())
        Gdx.input.inputProcessor = multiplexer
    }

    override fun getID() = Screens.GAME_OVER

    private inner class GameOverInputProcessor : InputAdapter() {
        override fun keyDown(keycode: Int) = keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE

        override fun keyUp(keycode: Int) = when (keycode) {
            Input.Keys.BACK, Input.Keys.ESCAPE -> {
                game.player.playSound(SoundPlayer.SoundCode.FAIL)
                game.clearStack()
                true
            }
            else -> false
        }
    }
}