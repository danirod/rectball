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
package es.danirod.rectball.settings

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceContainer {

    val preferences: Preferences

    class BoolPreference(
        private val key: String,
        private val defValue: Boolean
    ) : ReadWriteProperty<PreferenceContainer, Boolean> {
        override fun getValue(thisRef: PreferenceContainer, property: KProperty<*>) = thisRef.preferences.getBoolean(key, defValue)

        override fun setValue(thisRef: PreferenceContainer, property: KProperty<*>, value: Boolean) {
            thisRef.preferences.putBoolean(key, value)
            thisRef.preferences.flush()
        }
    }

    class LongPreference(private val key: String) : ReadWriteProperty<PreferenceContainer, Long> {
        override fun getValue(thisRef: PreferenceContainer, property: KProperty<*>) = thisRef.preferences.getLong(key)

        override fun setValue(thisRef: PreferenceContainer, property: KProperty<*>, value: Long) {
            thisRef.preferences.putLong(key, value)
            thisRef.preferences.flush()
        }
    }

    class MapStringLongPreference(private val key: String) :
        ReadWriteProperty<PreferenceContainer, Map<String, Long>> {
        override fun getValue(thisRef: PreferenceContainer, property: KProperty<*>): Map<String, Long> =
            thisRef.preferences.getString(key, "{}").let { json ->
                val decoder = JsonReader().parse(json)
                val map = mutableMapOf<String, Long>()
                if (decoder.isObject) {
                    var entry = decoder.child()
                    while (entry != null) {
                        map[entry.name] = entry.asLong()
                        entry = entry.next()
                    }
                }
                map
            }

        override fun setValue(
            thisRef: PreferenceContainer,
            property: KProperty<*>,
            value: Map<String, Long>
        ) {
            val encoder = JsonValue(JsonValue.ValueType.`object`)
            for ((k, v) in value) {
                encoder.addChild(k, JsonValue(v))
            }
            val content = encoder.toJson(JsonWriter.OutputType.json)
            thisRef.preferences.putString(key, content)
            thisRef.preferences.flush()
        }

    }

}