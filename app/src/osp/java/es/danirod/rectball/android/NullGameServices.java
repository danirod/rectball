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
package es.danirod.rectball.android;

import com.badlogic.gdx.Gdx;


public class NullGameServices implements GameServices {

    @Override
    public void signIn() {
        Gdx.app.log("NullGameServices", "signIn()");
    }

    @Override
    public void signOut() {
        Gdx.app.log("NullGameServices", "signOut()");
    }

    @Override
    public boolean isSignedIn() {
        Gdx.app.log("NullGameServices", "isSignedIn()");
        return false;
    }

    @Override
    public void uploadScore(int score, int time) {
        Gdx.app.log("NullGameServices", "uploadScore(score=" + score + ", time=" + time +")");
    }

    @Override
    public void showLeaderboards() {
        Gdx.app.log("NullGameServices", "showLeaderboards()");
    }

    @Override
    public void showAchievements() {
        Gdx.app.log("NullGameServices", "showAchievements()");
    }
}
