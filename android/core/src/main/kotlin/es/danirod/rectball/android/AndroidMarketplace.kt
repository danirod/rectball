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

import android.content.Context
import android.content.Intent
import android.net.Uri
import es.danirod.rectball.platform.Marketplace

/**
 * Implementation for an Android marketplace. A request to open the marketplace will simply point
 * the browser to the URL described by the given [url], which may be detected by the phone as a
 * deep link and opened in the appropriate application store.
 */
class AndroidMarketplace(
    private val context: Context,
    override val name: String,
    private val url: String
) : Marketplace {

    override val supported = true

    override fun open() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }

}