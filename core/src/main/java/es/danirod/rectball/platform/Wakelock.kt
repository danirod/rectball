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
package es.danirod.rectball.platform

import com.badlogic.gdx.Gdx

/**
 * Wakelocks prevent the device from entering sleep mode. They are used in the game to keep
 * the screen on while the user is playing, so that the player can focus on the game without
 * being distracted by the screen powering off.
 *
 * Android supports wakelocks but it is up to other platforms to support them. The game will
 * use the [supported] attribute to check at any time if the platform supports wakelocks,
 * and if supported, may call [request()] and [clear()] to set or unset a wakelock.
 */
interface Wakelock {

    /** Indicates whether this platform supports wakelock. */
    val supported: Boolean

    /** Request a wakelock, so that the screen stays on. */
    fun request()

    /** Clears a wakelock, so that the screen can enter sleep mode. */
    fun clear()

    /**
     * A null wakelock that does not support this feature and provides placeholder methods.
     * Use this class whenever the platform does not support wakelocks, to avoid duplicating
     * more code.
     */
    class NullWakelock : Wakelock {
        override val supported = false

        override fun request() = Gdx.app.log("NullWakelock", "Ignoring wakelock set request")

        override fun clear() = Gdx.app.log("NullWakelock", "Ignoring wakelock clear request")
    }
}