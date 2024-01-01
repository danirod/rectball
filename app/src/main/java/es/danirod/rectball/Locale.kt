/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2024 Dani Rodríguez.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.danirod.rectball

import com.badlogic.gdx.utils.I18NBundle

class Locale(private val bundle: I18NBundle, private val extra: I18NBundle) {

    constructor(bundle: I18NBundle) : this(bundle, I18NBundle())

    operator fun get(key: String): String = bundle[key]

    fun extra(key: String): String = extra[key]

}