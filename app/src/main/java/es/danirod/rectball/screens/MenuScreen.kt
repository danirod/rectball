package es.danirod.rectball.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import es.danirod.rectball.Constants
import es.danirod.rectball.RectballGame
import es.danirod.rectball.scene2d.listeners.ScreenPopper
import kotlin.math.max

/**
 * Base screen used to render a fullscreen window, with title, a back button to pop this screen,
 * and a main scrollpane where the contents will be added. This is a transitional window, I am
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

    override fun setUpInterface(table: Table) {
        table.top()
        table.row().padTop(5f).padBottom(10f).uniformY()
        table.add(backButton).padLeft(Constants.STAGE_PADDING.toFloat())
        table.add(titleLabel).padRight(Constants.STAGE_PADDING.toFloat()).expandX()
        table.row()
        table.add(getScrollPane()).colspan(2).grow().align(Align.top).padTop(5f)
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
        // Use the insets for the menu table, but don't pad more than necessary.
        val pixelsPerViewport = Gdx.graphics.width / Constants.VIEWPORT_WIDTH
        val outerPadTop = max(game.marginTop / pixelsPerViewport, 5f)
        val outerPadBottom = game.marginBottom / pixelsPerViewport
        val outerPadLeft = game.marginLeft / pixelsPerViewport
        val outerPadRight = game.marginRight / pixelsPerViewport
        table.pad(outerPadTop, outerPadLeft, outerPadBottom, outerPadRight)
    }
}