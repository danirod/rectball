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
            pad(0f)
            image.setScaling(Scaling.fit)
            imageCell.height(Value.percentHeight(1f, titleLabel))
            addListener(ScreenPopper(game))
        }
    }

    /* It uses a function so that it can be lazily computed on screen load. */
    private fun getScrollPane() = ScrollPane(getRootView(), game.appSkin).apply {
        fadeScrollBars = false
    }

    override fun show() {
        super.show()

        table.top()
        table.row().padTop(20f).padBottom(20f).uniformY()
        table.add(backButton).padLeft(Constants.STAGE_PADDING.toFloat())
        table.add(titleLabel).padRight(Constants.STAGE_PADDING.toFloat()).expandX()
        table.row()
        table.add(getScrollPane()).maxWidth(480f * 1.5f).colspan(2).grow().align(Align.top)
        table.layout()
    }

    /** The title to present in the top bar. */
    abstract fun getTitle(): String

    /** The main widget to place in the application. */
    abstract fun getRoot(): Actor

    private fun getRootView() = Container(getRoot()).apply {
        fill()
        pad(Constants.STAGE_PADDING.toFloat()).padTop(0f)
    }

    override fun updateTablePadding() {
        val pixelsPerViewport = viewport.unitsPerPixel
        val paddingTop = game.marginTop * pixelsPerViewport
        val paddingBottom = game.marginBottom * pixelsPerViewport
        val paddingLeft = game.marginLeft * pixelsPerViewport
        val paddingRight = game.marginRight * pixelsPerViewport
        table.pad(paddingTop, paddingLeft, paddingBottom, paddingRight)
    }
}
