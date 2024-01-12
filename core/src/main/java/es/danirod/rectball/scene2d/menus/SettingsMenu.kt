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
package es.danirod.rectball.scene2d.menus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Timer
import es.danirod.rectball.RectballGame
import es.danirod.rectball.SoundPlayer.SoundCode
import es.danirod.rectball.scene2d.ui.SwitchActor
import es.danirod.rectball.screens.AboutScreen
import es.danirod.rectball.screens.LicenseScreen
import es.danirod.rectball.screens.TutorialScreen

class SettingsMenu(private val game: RectballGame) : VerticalGroup() {

    private val soundSwitch = game.locale["settings.sound"].let { label ->
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

    private val vibrationSwitch = game.locale["settings.vibration"].let { label ->
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

    private val keepScreenOn = game.locale["settings.keep_screen_on"].let { label ->
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

    private val colorSwitch = game.locale["settings.colorblind"].let { label ->
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

    private val doTutorialButton = game.locale["settings.play_tutorial"].let { label ->
        TextButton(label, game.appSkin).apply {
            pad(10f)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    game.player.playSound(SoundCode.SUCCESS)
                    game.pushScreen(TutorialScreen(game));
                }
            })
        }
    }

    private val doGameServicesLogin by lazy {
        game.locale.format("settings.game_services_log_in", game.context.gameServices.name).let { label ->
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
    }

    private val doGameServicesLogout by lazy {
        game.locale.format("settings.game_services_log_out", game.context.gameServices.name).let { label ->
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
    }

    private val doOpenInMarketplace by lazy {
        game.locale.format("settings.view_in_marketplace", game.context.marketplace.name).let { label ->
            TextButton(label, game.appSkin).apply {
                pad(10f)
                addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        game.context.marketplace.open()
                    }
                })
            }
        }
    }

    private val doShowInfo = game.locale["settings.info_and_credits"].let { label ->
        TextButton(label, game.appSkin).apply {
            pad(10f)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    game.player.playSound(SoundCode.SUCCESS)
                    game.pushScreen(AboutScreen(game));
                }
            })
        }
    }

    private val doShowLicense = game.locale["settings.view_licenses"].let { label ->
        TextButton(label, game.appSkin).apply {
            pad(10f)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.player.playSound(SoundCode.SUCCESS)
                    game.pushScreen(LicenseScreen(game));
                }
            })
        }
    }

    private fun gameServicesButton() = if (game.context.gameServices.signedIn())
        doGameServicesLogout
    else
        doGameServicesLogin

    init {
        grow()
        space(25f)
        addActor(soundSwitch)
        if (game.haptics.supported) {
            addActor(vibrationSwitch)
        }
        if (game.context.wakelock.supported) {
            addActor(keepScreenOn)
        }
        addActor(colorSwitch)
        addActor(doTutorialButton)
        if (game.context.gameServices.supported) {
            addActor(gameServicesButton())
        }
        if (game.context.marketplace.supported) {
            addActor(doOpenInMarketplace)
        }
        addActor(doShowInfo)
        addActor(doShowLicense)
    }

    private fun gameServicesButtonCallback(requestedStatus: Boolean, button: TextButton, text: String) {
        button.setText(game.locale["settings.game_services_logging_in"])
        button.isDisabled = true
        game.player.playSound(SoundCode.SELECT)

        Gdx.app.postRunnable {
            if (requestedStatus) {
                if (!game.context.gameServices.signedIn()) {
                    game.context.gameServices.signIn()
                }
            } else {
                if (game.context.gameServices.signedIn()) {
                    game.context.gameServices.signOut()
                }
            }
        }

        lateinit var recheckTask: Timer.Task
        recheckTask = Timer.schedule(object : Timer.Task() {
            override fun run() {
                if (!game.context.gameServices.loggingIn()) {
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
