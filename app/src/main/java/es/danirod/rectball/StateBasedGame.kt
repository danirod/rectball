/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2017 Dani Rodríguez.
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
package es.danirod.rectball

import com.badlogic.gdx.Game
import es.danirod.rectball.screens.AbstractScreen
import java.util.*

abstract class StateBasedGame: Game() {

    /** A map of the screens in use for this game. */
    protected val screens = mutableMapOf<Int, AbstractScreen>()

    /** Screen stack allows managing a FIFO list of visible screens. */
    private val stack = ArrayDeque<AbstractScreen>()

    /** The default screen to use when an empty stack is popped or cleared. */
    abstract val emptyStackScreen: Int

    /** Returns the registered screen whose identifier is [id]. */
    fun getScreen(id: Int): AbstractScreen? = screens[id]

    /** Returns a list containing all the registered screens. */
    fun getAllScreens() = screens.values.toList()

    /** Add a [screen] to the screen map. */
    fun addScreen(screen: AbstractScreen) {
        screens[screen.id] = screen
    }

    /** Pushes the screen identified by [id] onto the screen stack and enables it. */
    fun pushScreen(id: Int) {
        stack.push(screens[id])
        setScreen(stack.peek())
    }

    /** Removes the current screen from the stack and access the next one. */
    fun popScreen() {
        stack.removeFirst()
        if (stack.isEmpty()) {
            pushScreen(emptyStackScreen)
        } else {
            setScreen(stack.peek())
        }
    }

    /** Clears the stack screen and sets the default one. */
    fun clearStack() {
        stack.clear()
        setScreen(screens[emptyStackScreen])
    }

    override fun create() {

    }

}