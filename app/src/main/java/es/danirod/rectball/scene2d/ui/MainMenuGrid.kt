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
package es.danirod.rectball.scene2d.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import es.danirod.rectball.RectballGame
import es.danirod.rectball.SoundPlayer
import es.danirod.rectball.android.BuildConfig
import es.danirod.rectball.android.R
import es.danirod.rectball.scene2d.listeners.ScreenJumper
import es.danirod.rectball.screens.Screens

class MainMenuGrid(private val game: RectballGame) : Table() {

    private val play = Button(game.skin, "green").apply {
        defaults().space(15f).padTop(15f).padBottom(15f)

        val drawable = game.skin.getDrawable("iconPlay")
        val playButton = Image(drawable)
        add(playButton)

        val text = game.context.getString(R.string.main_play)
        val label = Label(text, game.skin, "big")
        add(label)

        addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.getScreen(Screens.ABOUT)?.dispose()
                game.getScreen(Screens.SETTINGS)?.dispose()
                game.getScreen(Screens.STATISTICS)?.dispose()
                game.player.playSound(SoundPlayer.SoundCode.SELECT)
                game.pushScreen(Screens.GAME)
            }
        })
    }

    private val settings = ImageButton(game.skin, "settings").apply {
        pad(5f)
        addListener(ScreenJumper(game, Screens.SETTINGS))
    }

    private val statistics = ImageButton(game.skin, "charts").apply {
        pad(5f)
        addListener(ScreenJumper(game, Screens.STATISTICS))
    }

    private val leaderboard by lazy {
        ImageButton(game.skin, "leaderboard").apply {
            pad(15f)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.player.playSound(SoundPlayer.SoundCode.SELECT)
                    if (!game.context.gameServices.isSignedIn) {
                        game.context.gameServices.signIn()
                    } else {
                        game.context.gameServices.showLeaderboards()
                    }
                }
            })
        }
    }

    private val achievements by lazy {
        ImageButton(game.skin, "achievements").apply {
            pad(15f)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.player.playSound(SoundPlayer.SoundCode.SELECT)
                    if (!game.context.gameServices.isSignedIn) {
                        game.context.gameServices.signIn()
                    } else {
                        game.context.gameServices.showAchievements()
                    }
                }
            })
        }
    }

    init {
        defaults().space(30f)
        add(play).colspan(2).expandX().fillX().padBottom(20f).row()
        add(settings).expandX().fillX().align(Align.left)
        add(statistics).expandX().fillX().align(Align.right)
        if (BuildConfig.FLAVOR.equals("gpe")) {
            row()
            add(leaderboard).expandX().align(Align.left)
            add(achievements).expandX().align(Align.right)
        }
        row()
    }

}