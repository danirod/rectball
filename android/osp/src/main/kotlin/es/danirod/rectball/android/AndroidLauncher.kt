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

import es.danirod.rectball.platform.GameServices
import es.danirod.rectball.platform.Marketplace

class AndroidLauncher : BaseAndroidLauncher() {

    override val gameServices by lazy { GameServices.NullGameServices() }

    override val marketplace by lazy { Marketplace.NullMarketplace() }

    override val wakelock by lazy { AndroidWakelock(this) }

    override val version = BuildConfig.VERSION_NAME

    override val buildNumber = BuildConfig.VERSION_CODE

}
