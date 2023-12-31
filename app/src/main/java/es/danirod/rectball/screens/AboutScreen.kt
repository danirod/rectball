/*
 * This file is part of Rectball
 * Copyright (C) 2015-2023 Dani Rodríguez
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
package es.danirod.rectball.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import es.danirod.rectball.RectballGame
import es.danirod.rectball.android.BuildConfig
import es.danirod.rectball.android.R

/**
 * About screen.
 */
class AboutScreen(game: RectballGame) : MenuScreen(game) {

    private val credits: String by lazy {
        val version = "Rectball ${BuildConfig.VERSION_NAME}"
        val credits = Gdx.files.internal("credits.txt").readString("UTF-8")
        "$version\n$credits"
    }

    override fun getTitle() = game.context.getString(R.string.about_title)

    override fun getRoot() = Label(credits, game.appSkin, "small", "white").apply {
        wrap = true
        setFontScale(0.85f)
    }
}