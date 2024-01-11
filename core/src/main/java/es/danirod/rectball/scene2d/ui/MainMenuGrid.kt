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

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Scaling
import es.danirod.rectball.RectballGame
import es.danirod.rectball.SoundPlayer
import es.danirod.rectball.screens.GameScreen
import es.danirod.rectball.screens.SettingsScreen
import es.danirod.rectball.screens.StatisticsScreen

class MainMenuGrid(private val game: RectballGame) : Table() {

    private val logo = Image(game.appSkin, "logo").apply {
        setScaling(Scaling.contain)
    }

    private val play = ImageTextButton(game.locale["main.play"], game.appSkin, "play").apply {
        pad(30f)
        imageCell.spaceRight(20f)
        addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.player.playSound(SoundPlayer.SoundCode.SELECT)
                game.pushScreen(GameScreen(game));
            }
        })
    }

    private val settings = ImageButton(game.appSkin, "settings").apply {
        pad(0f)
        addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.player.playSound(SoundPlayer.SoundCode.SUCCESS)
                game.pushScreen(SettingsScreen(game));
            }
        })
    }

    private val statistics = ImageButton(game.appSkin, "statistics").apply {
        pad(0f)
        addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.player.playSound(SoundPlayer.SoundCode.SUCCESS)
                game.pushScreen(StatisticsScreen(game));
            }
        })
    }

    /* This is lazy so that it doesn't break if game services are not enabled. */
    private val leaderboards by lazy {
        ImageButton(game.appSkin, "leaderboard").apply {
            pad(0f)
            image.color.set(game.appSkin.getColor("black"))
            image.color.a = 0.8f
            imageCell.size(Value.percentWidth(0.7f, this)).center()
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.player.playSound(SoundPlayer.SoundCode.SELECT)
                    if (!game.context.gameServices.signedIn()) {
                        game.context.gameServices.signIn()
                    } else {
                        game.context.gameServices.showLeaderboards()
                    }
                }
            })
        }
    }

    /* This is lazy so that it doesn't break if game services are not enabled. */
    private val achievements by lazy {
        ImageButton(game.appSkin, "achievements").apply {
            pad(0f)
            image.color.set(game.appSkin.getColor("black"))
            image.color.a = 0.8f
            imageCell.size(Value.percentWidth(0.6f, this)).center()
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.player.playSound(SoundPlayer.SoundCode.SELECT)
                    if (!game.context.gameServices.signedIn()) {
                        game.context.gameServices.signIn()
                    } else {
                        game.context.gameServices.showAchievements()
                    }
                }
            })
        }
    }

    init {
        defaults().space(25f).growX()
        add(logo).prefHeight(100f).minHeight(100f).spaceBottom(40f).colspan(2).expand().top().row()
        add(play).colspan(2).minHeight(Value.percentHeight(0.5f)).row()
        add(settings).height(80f)
        add(statistics).height(80f)
        row()

        if (game.context.gameServices.supported) {
            add(leaderboards).expandX().fill(false).left().size(80f)
            add(achievements).expandX().fill(false).right().size(80f)
            row()
        }
    }

}
