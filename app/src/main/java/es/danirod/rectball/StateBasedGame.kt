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
package es.danirod.rectball

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import es.danirod.rectball.screens.AbstractScreen

abstract class StateBasedGame: Game() {
    private val stack = ArrayDeque<Screen>()

    abstract fun getFallbackScreen(): AbstractScreen

    fun pushScreen(screen: Screen) {
        val oldScreen = getScreen()
        Gdx.app.debug("StateBasedGame", "push: ${oldScreen.javaClass.canonicalName} => ${screen.javaClass.canonicalName}")
        setScreen(screen)
        oldScreen?.let { stack.add(it) }
    }

    fun popScreen() {
        val nextScreen = stack.removeLastOrNull() ?: getFallbackScreen()
        Gdx.app.debug("StateBasedGame", "pop: ${nextScreen.javaClass.canonicalName}")
        setScreen(nextScreen)
    }

    /** Clears the stack screen and sets the default one. */
    fun clearScreenStack() {
        Gdx.app.debug("StateBasedGame", "clear")
        stack.clear()
        setScreen(getFallbackScreen())
    }

    override fun setScreen(screen: Screen?) {
        screen?.let { Gdx.app.debug("StateBasedGame", "setScreen: ${screen.javaClass.canonicalName}") }
        super.setScreen(screen)
    }
}
