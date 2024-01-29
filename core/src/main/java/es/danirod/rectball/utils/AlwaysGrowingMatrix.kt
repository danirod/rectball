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
package es.danirod.rectball.utils

class AlwaysGrowingMatrix<T>(private val generator: (x: Int, y: Int) -> T) {

    private var items: MutableList<MutableList<T?>> = mutableListOf()

    fun get(x: Int, y: Int): T {
        while (y >= items.size) {
            items.add(mutableListOf())
        }
        val row = items[y]
        while (x >= row.size) {
            row.add(null)
        }
        return row[x] ?: generator(x, y).also { row[x] = it }
    }

    fun set(x: Int, y: Int, value: T) {
        while (y >= items.size) {
            items.add(mutableListOf())
        }
        val row = items[y]
        while (x >= row.size) {
            row.add(null)
        }
        row[x] = value
    }
}
