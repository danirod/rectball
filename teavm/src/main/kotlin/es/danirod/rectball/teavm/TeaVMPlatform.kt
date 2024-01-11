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
package es.danirod.rectball.teavm

import com.badlogic.gdx.Gdx
import es.danirod.rectball.Platform
import es.danirod.rectball.platform.GameServices
import es.danirod.rectball.platform.Marketplace
import es.danirod.rectball.platform.Wakelock

class TeaVMPlatform : Platform {

    private val properties: Map<String, String> by lazy {
        val content = Gdx.files.internal("version.properties").readString()
        val map = mutableMapOf<String, String>()
        content.lineSequence().filter { line -> !line.startsWith('#') && line.contains('=') }
            .forEach { line ->
                val parts = line.split('=')
                map[parts[0]] = parts[1]
            }
        map
    }

    override val gameServices = GameServices.NullGameServices()
    override val marketplace = Marketplace.NullMarketplace()
    override val wakelock = Wakelock.NullWakelock()
    override val version: String
        get() = properties["appVersion"] ?: "nightly"
    override val buildNumber: Int
        get() = properties["appBuildNumber"]?.toInt() ?: 0
}