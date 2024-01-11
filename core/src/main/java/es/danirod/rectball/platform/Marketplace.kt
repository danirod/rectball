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
 * Marketplace services used to make the user visit the game marketing page or app store.
 * On some devices this will be the application store, on other devices it may be opening
 * a link to the game homepage.
 *
 * The game will use the [supported] method to check if the platform provides a marketplace.
 * For instance, if the platform does not provide a marketplace a button to visit the game
 * in the marketplace will not be present in the settings menu.
 */
interface Marketplace {

    /** Whether the marketplace is enabled for this platform. */
    val supported: Boolean

    /** How to name in public the marketplace (for instance, "Google Play", "Steam"...) */
    val name: String

    /** Request the marketplace page to be opened. */
    fun open()

    /**
     * An empty implementation of Marketplace that does not support this feature. Placeholder
     * when the platform will not support marketplaces, so that it is not required to write
     * the code from scratch.
     */
    class NullMarketplace : Marketplace {
        override val supported = false

        override val name = "(null)"

        override fun open() = Gdx.app.log("NullMarketplace", "Ignoring request to open marketplace")

    }

}