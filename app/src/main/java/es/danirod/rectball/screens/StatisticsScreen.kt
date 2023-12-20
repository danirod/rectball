/*
 * This file is part of Rectball
 * Copyright (C) 2015-2017 Dani Rodríguez
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
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import es.danirod.rectball.Pixmapper.captureScreenshot
import es.danirod.rectball.RectballGame
import es.danirod.rectball.SoundPlayer
import es.danirod.rectball.android.PixmapSharer
import es.danirod.rectball.android.R
import es.danirod.rectball.scene2d.listeners.ScreenPopper
import es.danirod.rectball.scene2d.ui.StatsTable

/**
 * Statistics screen.
 */
class StatisticsScreen(game: RectballGame) : AbstractScreen(game) {
    private lateinit var statsTable: StatsTable
    private lateinit var pane: ScrollPane
    private lateinit var backButton: TextButton
    private lateinit var shareButton: ImageButton

    private fun statsTable(): StatsTable {
        val bold = game.skin.get("bold", LabelStyle::class.java)
        val normal = game.skin.get("small", LabelStyle::class.java)
        return StatsTable(game, bold, normal)
    }

    public override fun setUpInterface(table: Table) {
        statsTable = statsTable()

        pane = ScrollPane(statsTable, game.skin).apply {
            fadeScrollBars = false
        }

        backButton = TextButton(game.context.getString(R.string.core_back), game.skin).apply {
            addListener(ScreenPopper(game))
        }

        shareButton = ImageButton(game.skin, "share").apply {
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    game.player.playSound(SoundPlayer.SoundCode.SELECT)
                    val pmap = buildOffscreenTable()
                    PixmapSharer(game).share(game.context.getString(R.string.sharing_intent_title), "", pmap)
                    event.cancel()
                }
            })
        }

        table.apply {
            add(pane).colspan(2).align(Align.topLeft).expand().fill().row()
            add(backButton).fillX().expandX().expandY().height(80f).padTop(20f).align(Align.bottom)
            add(shareButton).height(80f).padTop(20f).padLeft(10f).align(Align.bottomRight).row()
        }
    }

    override fun getID(): Int = Screens.STATISTICS

    private fun buildOffscreenTable(): Pixmap {
        /* Build an FBO object where the offscreen table will be rendered. */
        val aspectRatio = statsTable.width / statsTable.height
        val width = 480
        val height = (480 / aspectRatio).toInt()
        val fbo = FrameBuffer(Pixmap.Format.RGBA8888, width, height, false)

        /* Build a stage and add the virtual new table. */
        val stage = Stage(FitViewport(480f, (480f / aspectRatio)))
        val table = statsTable()
        table.setFillParent(true)
        table.pad(10f)
        stage.addActor(table)

        /* Then virtually render the stage. */
        fbo.begin()
        Gdx.gl.glClearColor(RectballGame.BG_COLOR.r, RectballGame.BG_COLOR.g, RectballGame.BG_COLOR.b, RectballGame.BG_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
        val pmap = captureScreenshot(0, 0, width, height)
        fbo.end()
        fbo.dispose()

        return pmap
    }
}