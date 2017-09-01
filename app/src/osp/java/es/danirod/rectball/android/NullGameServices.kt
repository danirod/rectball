/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2017 Dani Rodríguez.
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
package es.danirod.rectball.android

import com.badlogic.gdx.Gdx


class NullGameServices : GameServices {

    override fun signIn() {
        Gdx.app.log("NullGameServices", "signIn()")
    }

    override fun signOut() {
        Gdx.app.log("NullGameServices", "signOut()")
    }

    override val isSignedIn: Boolean
        get() {
            Gdx.app.log("NullGameServices", "isSignedIn()")
            return false
        }

    override fun uploadScore(score: Int, time: Int) {
        Gdx.app.log("NullGameServices", "uploadScore(score=$score, time=$time)")
    }

    override fun showLeaderboards() {
        Gdx.app.log("NullGameServices", "showLeaderboards()")
    }

    override fun showAchievements() {
        Gdx.app.log("NullGameServices", "showAchievements()")
    }
}
