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
package es.danirod.rectball.scene2d.menus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Timer
import es.danirod.rectball.RectballGame
import es.danirod.rectball.SoundPlayer.SoundCode
import es.danirod.rectball.android.BuildConfig
import es.danirod.rectball.android.R
import es.danirod.rectball.scene2d.listeners.ScreenJumper
import es.danirod.rectball.scene2d.ui.SwitchActor
import es.danirod.rectball.screens.Screens

class SettingsMenu(private val game: RectballGame) : VerticalGroup() {

    private val soundSwitch = game.context.getString(R.string.settings_sound).let { label ->
        SwitchActor(label, game.appSkin).apply {
            isChecked = game.settings.soundEnabled
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.settings.soundEnabled = isChecked
                    game.player.playSound(SoundCode.SELECT)
                }
            })
        }
    }

    private val vibrationSwitch = game.context.getString(R.string.settings_vibration).let { label ->
        SwitchActor(label, game.appSkin).apply {
            isChecked = game.settings.vibrationEnabled
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.settings.vibrationEnabled = isChecked
                    game.player.playSound(SoundCode.SELECT)
                }
            })
        }
    }

    private val keepScreenOn = game.context.getString(R.string.settings_keep_screen_on).let { label ->
        SwitchActor(label, game.appSkin).apply {
            isChecked = game.settings.keepScreenOn
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.settings.keepScreenOn = isChecked
                    game.player.playSound(SoundCode.SELECT)
                }
            })
        }
    }

    private val colorSwitch = game.context.getString(R.string.settings_colorblind).let { label ->
        SwitchActor(label, game.appSkin).apply {
            isChecked = game.settings.colorblindMode
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.settings.colorblindMode = isChecked
                    game.updateBallAtlas()
                    game.player.playSound(SoundCode.SELECT)
                }
            })
        }
    }

    private val doTutorialButton = game.context.getString(R.string.settings_play_tutorial).let { label ->
        TextButton(label, game.appSkin).apply {
            pad(10f)
            addListener(ScreenJumper(game, Screens.TUTORIAL))
        }
    }

    private val doGameServicesLogin = game.context.getString(R.string.settings_play_log_in).let { label ->
        TextButton(label, game.appSkin).apply {
            pad(10f)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    val button = actor as TextButton
                    gameServicesButtonCallback(true, button, label)
                }
            })
        }
    }

    private val doGameServicesLogout = game.context.getString(R.string.settings_play_log_out).let { label ->
        TextButton(label, game.appSkin).apply {
            pad(10f)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    val button = actor as TextButton
                    gameServicesButtonCallback(false, button, label)
                }
            })
        }
    }

    private val doOpenInMarketplace = game.context.getString(R.string.settings_view_in_google_play).let { label ->
        TextButton(label, game.appSkin).apply {
            pad(10f)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.context.openInMarketplace()
                }
            })
        }
    }

    private val doShowInfo = game.context.getString(R.string.settings_info_and_credits).let { label ->
        TextButton(label, game.appSkin).apply {
            pad(10f)
            addListener(ScreenJumper(game, Screens.ABOUT))
        }
    }

    private val doShowLicense = game.context.getString(R.string.settings_view_licenses).let { label ->
        TextButton(label, game.appSkin).apply {
            pad(10f)
            addListener(ScreenJumper(game, Screens.LICENSE))
        }
    }

    private fun gameServicesButton() = if (game.context.gameServices.isSignedIn)
        doGameServicesLogout
    else
        doGameServicesLogin

    init {
        grow()
        space(25f)
        addActor(soundSwitch)
        addActor(vibrationSwitch)
        addActor(keepScreenOn)
        addActor(colorSwitch)
        addActor(doTutorialButton)
        @Suppress("KotlinConstantConditions")
        if (BuildConfig.FLAVOR == "gpe") {
            addActor(gameServicesButton())
        }
        addActor(doOpenInMarketplace)
        addActor(doShowInfo)
        addActor(doShowLicense)
    }

    private fun gameServicesButtonCallback(requestedStatus: Boolean, button: TextButton, text: String) {
        button.setText(game.context.getString(R.string.settings_play_processing))
        button.isDisabled = true
        game.player.playSound(SoundCode.SELECT)

        Gdx.app.postRunnable {
            if (requestedStatus) {
                if (!game.context.gameServices.isSignedIn) {
                    game.context.gameServices.signIn()
                }
            } else {
                if (game.context.gameServices.isSignedIn) {
                    game.context.gameServices.signOut()
                }
            }
        }

        lateinit var recheckTask: Timer.Task
        recheckTask = Timer.schedule(object : Timer.Task() {
            override fun run() {
                if (!game.context.gameServices.isLoggingIn) {
                    button.setText(text)
                    button.isDisabled = false
                    val target = children.indexOf(button)
                    removeActor(button)
                    addActorAt(target, gameServicesButton())
                    recheckTask.cancel()
                }
            }
        }, 0f, 1f)
    }
}