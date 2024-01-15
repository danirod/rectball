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

class AndroidLauncher : BaseAndroidLauncher() {

    private val gpg by lazy { GpgsClient().initialize(this, false) }

    override val gameServices by lazy { GooglePlayGamesIntegration(this, gpg) }

    override val marketplace = AndroidMarketplace(this, "Google Play", "https://play.google.com/store/apps/details?id=es.danirod.rectball.android")

    override val wakelock = AndroidWakelock(this)
    override fun onStart() {
        super.onStart()
        gpg.resumeSession()
    }

    override fun onStop() {
        super.onStop()
        gpg.pauseSession()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        gpg.onActivityResult(requestCode, resultCode, data)
    }
}
