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
package es.danirod.rectball.screens

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import es.danirod.rectball.Constants
import es.danirod.rectball.RectballGame
import es.danirod.rectball.scene2d.listeners.ScreenPopper

/**
 * Base screen used to render a full screen window, with title, a back button to pop this screen,
 * and a main scroll pane where the contents will be added. This is a transitional window, I am
 * sure that in further releases it will change.
 */
abstract class MenuScreen(game: RectballGame) : AbstractScreen(game) {

    private val titleLabel by lazy {
        Label(getTitle(), game.appSkin, "large", "white").apply {
            setFontScale(0.6f)
        }
    }

    private val backButton by lazy {
        ImageButton(game.appSkin, "back").apply {
            pad(10f)
            image.setScaling(Scaling.fit)
            imageCell.height(Value.percentHeight(1f, titleLabel))
            addListener(ScreenPopper(game))
        }
    }

    /* It uses a function so that it can be lazily computed on screen load. */
    private val scrollPane by lazy {
        ScrollPane(rootView, game.appSkin).apply {
            fadeScrollBars = false
        }
    }

    private val rootView by lazy {
        Container(getRoot()).apply {
            fill()
            padLeft(25f)
            padRight(25f)
            padTop(10f)
        }
    }

    override fun show() {
        super.show()

        table.setFillParent(false)

        table.top()
        table.row().uniformY()
        table.add(backButton).height(30f).padBottom(15f)
        table.add(titleLabel).height(30f).padBottom(15f).expandX()
        table.row()
        table.add(scrollPane).colspan(2).grow().align(Align.top)
    }

    /** The title to present in the top bar. */
    abstract fun getTitle(): String

    /** The main widget to place in the application. */
    abstract fun getRoot(): Actor

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)

        val windowArea = viewport.getWindowArea()
        val safeArea = viewport.getSafeArea()
        val desiredArea = viewport.getDesiredArea()
        val marginTop = game.marginTop * viewport.unitsPerPixel

        val realWidth = MathUtils.clamp(safeArea.width, desiredArea.width, desiredArea.width * 1.5f)

        table.setSize(realWidth, windowArea.height)
        table.setPosition(safeArea.x + (safeArea.width - realWidth) / 2, 0f)
        table.getCell(backButton).padTop(marginTop + 15f)
        table.getCell(titleLabel).padTop(marginTop + 15f)
        rootView.padBottom(safeArea.y)
    }
}
