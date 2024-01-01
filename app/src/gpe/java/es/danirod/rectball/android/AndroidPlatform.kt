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
import de.golfgl.gdxgamesvcs.GpgsClient

/**
 * This contains code for the Android platform. Here code that uses Android
 * SDK or Android API might be used. This code won't run on other platforms
 * than Android.
 *
 * @author danirod
 * @since 0.4.0
 */
internal class AndroidPlatform(context: AndroidLauncher) : AbstractPlatform(context) {

    private val gpg: GpgsClient = GpgsClient().initialize(context, false)

    private val services: GameServices = GsvcsGameServices(gpg, GooglePlayConstants(context))

    override val gameServices: GameServices
        get() = services

    override fun onStart() {
        gpg.resumeSession()
    }

    override fun onStop() {
        gpg.pauseSession()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       // gpg.gameHelper.onActivityResult(requestCode, resultCode, data)
    }
}
