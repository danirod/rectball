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

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import de.golfgl.gdxgamesvcs.GpgsClient
import es.danirod.rectball.Platform
import es.danirod.rectball.gameservices.GameServices
import es.danirod.rectball.gameservices.GsvcsGameServices

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

    override val marketplace = object : Platform.Marketplace {

        override val supported = true

        override fun open() {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=${AndroidLauncher.PACKAGE}")
                context.startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://play.google.com/store/apps/details?id=${AndroidLauncher.PACKAGE}")
                context.startActivity(intent)
            }
        }

    }

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
