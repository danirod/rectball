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
package es.danirod.rectball.lwjgl3

import com.badlogic.gdx.Gdx
import es.danirod.rectball.Platform
import es.danirod.rectball.platform.GameServices
import es.danirod.rectball.platform.Marketplace
import es.danirod.rectball.platform.Wakelock
import java.util.Properties

class Lwjgl3Platform : Platform {

    private val versionAssembly by lazy {
        Properties().apply {
            val file = Gdx.files.internal("version.properties")
            if (file.exists()) {
                this.load(file.read())
            }
        }
    }

    override val gameServices = GameServices.NullGameServices()
    override val marketplace = Marketplace.NullMarketplace()
    override val wakelock = Wakelock.NullWakelock()
    override val version: String
        get() = versionAssembly.getProperty("appVersion") ?: "nightly"
    override val buildNumber: Int
        get() = versionAssembly.getProperty("appBuildNumber")?.toInt() ?: 0
}