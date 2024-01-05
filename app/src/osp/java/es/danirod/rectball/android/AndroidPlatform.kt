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
package es.danirod.rectball.android

import android.content.Intent
import com.badlogic.gdx.Gdx
import de.golfgl.gdxgamesvcs.NoGameServiceClient
import es.danirod.rectball.Platform
import es.danirod.rectball.gameservices.GameServices
import es.danirod.rectball.gameservices.GsvcsGameServices

internal class AndroidPlatform(context: AndroidLauncher) : AbstractPlatform(context) {

    override val marketplace = object : Platform.Marketplace {
        override val supported = false

        override fun open() {
            Gdx.app.log("AndroidPlatform", "marketplace.open() was called but this platform does not support it")
        }
    }

    override fun onStart() { }

    override fun onStop() { }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { }

    override val gameServices: GameServices
        get() = GsvcsGameServices(NoGameServiceClient(), NoConstants())
}
