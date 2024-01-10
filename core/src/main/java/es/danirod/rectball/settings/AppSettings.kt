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

import es.danirod.rectball.settings.PreferenceContainer.BoolPreference
import es.danirod.rectball.settings.PreferenceContainer.LongPreference
import com.badlogic.gdx.Preferences as GdxPreferences

/**
 * Wrapper so that the game doesn't have to access the settings all the time.
 * Also, important in order to refactor the settings implementation without
 * having to change so much code.
 *
 * @since 0.5
 */
class AppSettings(override val preferences: GdxPreferences) : PreferenceContainer {
    var schema by LongPreference("pragma.schema")
    var soundEnabled by BoolPreference("settings.sound", true)
    var vibrationEnabled by BoolPreference("settings.vibration", true)
    var keepScreenOn by BoolPreference("settings.screen_on", true)
    var colorblindMode by BoolPreference("settings.colorblind", false)
    var tutorialAsked by BoolPreference("main_menu.tutorial_asked", false)
    var newInputMethodAsked by BoolPreference("main_menu.new_input_method", false)
}
