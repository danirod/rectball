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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Timer
import es.danirod.rectball.RectballGame
import es.danirod.rectball.SoundPlayer.SoundCode
import es.danirod.rectball.android.BuildConfig
import es.danirod.rectball.android.R
import es.danirod.rectball.android.settings.SettingsManager
import es.danirod.rectball.scene2d.listeners.ScreenJumper
import es.danirod.rectball.scene2d.ui.SwitchActor
import es.danirod.rectball.screens.Screens

class SettingsMenu(private val game: RectballGame) : ScrollPane(null, ScrollPaneStyle()) {

    private val soundSwitch = createSettingToggle(game.context.getString(R.string.settings_sound), SettingsManager.TAG_ENABLE_SOUND, true)

    private val colorSwitch = createSettingToggle(game.context.getString(R.string.settings_colorblind), SettingsManager.TAG_ENABLE_COLORBLIND, false) {
        game.updateBallAtlas()
    }

    private val fullscreenSwitch = createSettingToggle(game.context.getString(R.string.settings_fullscreen), SettingsManager.TAG_ENABLE_FULLSCREEN, false) {
        game.context.runOnUiThread { game.context.toggleFullscreen() }
    }

    private val doTutorialButton = TextButton(game.context.getString(R.string.settings_play_tutorial), game.skin).apply {
        addListener(ScreenJumper(game, Screens.TUTORIAL))
    }

    private val doGameServicesLogin = game.context.getString(R.string.settings_play_log_in).let { label ->
        TextButton(label, game.skin).apply {
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    val button = actor as TextButton
                    gameServicesButtonCallback(true, button, label)
                }
            })
        }
    }

    private val doGameServicesLogout = game.context.getString(R.string.settings_play_log_out).let { label ->
        TextButton(label, game.skin).apply {
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    val button = actor as TextButton
                    gameServicesButtonCallback(false, button, label)
                    event.cancel()
                }
            })
        }
    }

    private fun gameServicesButton() = if (game.context.gameServices.isSignedIn)
        doGameServicesLogout
    else
        doGameServicesLogin

    @Suppress("KotlinConstantConditions")
    private val settingsGroup = VerticalGroup().apply {
        setFillParent(true)
        expand()
        fill()
        space(25f)
        addActor(soundSwitch)
        addActor(colorSwitch)
        addActor(fullscreenSwitch)
        addActor(doTutorialButton)
        if (BuildConfig.FLAVOR == "gpe") {
            addActor(gameServicesButton())
        }
    }

    private val settingsScrollPane = ScrollPane(settingsGroup, ScrollPaneStyle()).apply {
        setFillParent(true)
    }

    init {
        actor = settingsScrollPane
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
                    val target = settingsGroup.children.indexOf(button)
                    settingsGroup.removeActor(button)
                    settingsGroup.addActorAt(target, gameServicesButton())
                    recheckTask.cancel()
                }
            }
        }, 0f, 1f)
    }

    private fun createSettingToggle(label: String, key: String, defaultValue: Boolean, callback: () -> Unit = {}) =
        SwitchActor(label, game.skin).apply {
            isChecked = game.context.settings.preferences.getBoolean(key, defaultValue)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    val editor = game.context.settings.preferences.edit()
                    editor.putBoolean(key, isChecked)
                    editor.apply()
                    callback()
                    game.player.playSound(SoundCode.SELECT)
                }
            })
        }

}