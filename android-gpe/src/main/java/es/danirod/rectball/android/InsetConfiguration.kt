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

import android.annotation.SuppressLint
import android.os.Build
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.danirod.rectball.RectballGame


/**
 * This class delegates into the appropriate code for setting the game insets
 * depending on the Android version of the device. The code will be removed
 * and reversed once support for Android 4.x is removed from the game.
 *
 * This class exists mostly because AndroidX does not work anymore with
 * Android 4.4, so the WindowInsetCompat actually does not do anything. Since
 * Android 4.4 still requests translucent status bars, not setting the insets
 * causes the game to render behind the status bar. A legacy and deprecated
 * code for setting the insets is used in that platform.
 *
 * Android 4.3 and below are not a problem since translucent status bar is not
 * enabled on those platforms, thus it is not needed to configure the margins
 * and paddings since the window is compressed anyway.
 */
class InsetConfiguration(private val view: AndroidLauncher, private val game: RectballGame) {

    private val strategy: InsetConfigurationStrategy = when {
        // On anything under 19, there are no insets to apply
        Build.VERSION.SDK_INT < 19 -> NoopInsetConfigurationStrategy()

        // Use a very deprecated code to set the insets on Android 4.4
        Build.VERSION.SDK_INT == 19 -> LegacyInsetConfigurationStrategy()

        // On any other Android version, just delegate into AndroidX
        else -> StandardInsetConfigurationStrategy()
    }

    /** Delegates into the appropriate apply() method. */
    fun apply() = strategy.apply()

    /** The common interface to use this pattern. */
    interface InsetConfigurationStrategy {
        fun apply()
    }

    /** Uses AndroidX to set the insets, padding the system bars. */
    inner class StandardInsetConfigurationStrategy : InsetConfigurationStrategy {
        override fun apply() {
            ViewCompat.setOnApplyWindowInsetsListener(view.window.decorView) { _, windowInsets ->
                val status = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
                val navigation = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
                val gesture = windowInsets.getInsets(WindowInsetsCompat.Type.mandatorySystemGestures())
                game.updateMargin(status.top, navigation.bottom, gesture.left, gesture.right)
                windowInsets
            }
        }
    }

    /** Many deprecated system calls, but they are only run in Android 4.4 */
    inner class LegacyInsetConfigurationStrategy : InsetConfigurationStrategy {
        @SuppressLint("InternalInsetResource", "DiscouragedApi")
        override fun apply() {
            val statusBarHeight = view.resources.getIdentifier("status_bar_height", "dimen", "android").let { rid ->
                if (rid > 0)
                    view.resources.getDimensionPixelSize(rid)
                else
                    0
            }

            game.updateMargin(statusBarHeight, 0, 0, 0)
        }
    }

    /** Empty implementation does nothing. */
    inner class NoopInsetConfigurationStrategy : InsetConfigurationStrategy {
        override fun apply() { }
    }
}
