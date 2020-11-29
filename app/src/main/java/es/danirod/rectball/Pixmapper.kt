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
package es.danirod.rectball

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.utils.ScreenUtils
import es.danirod.rectball.android.R
import java.io.File

object Pixmapper {

    fun captureScreenshot(x: Int = 0, y: Int = 0, width: Int = Gdx.graphics.width, height: Int = Gdx.graphics.height): Pixmap {
        val screenshot = ScreenUtils.getFrameBufferPixmap(x, y, width, height)
        flipBuffer(screenshot)
        return screenshot
    }

    fun exportPixmap(context: Context, image: Pixmap): Uri {
        val filename = "screenshot_${System.currentTimeMillis()}.png"
        val handle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val rootPath = File(context.filesDir, "rectball-screenshots")
            val screenshot = File(rootPath, filename)
            Gdx.files.absolute(screenshot.absolutePath)
        } else {
            val rootPath = Gdx.files.external("rectball-screenshots")
            rootPath.mkdirs()
            rootPath.child(filename)
        }
        PixmapIO.writePNG(handle, image)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            FileProvider.getUriForFile(context, context.getString(R.string.provider), handle.file())
        else
            Uri.fromFile(handle.file())
    }

    private fun flipBuffer(image: Pixmap) {
        // Reverse the lines on this image.
        val flippedBuffer = ByteArray(4 * image.width * image.height)
        val bytesPerLine = 4 * image.width
        for (j in 0 until image.height) {
            image.pixels.position(bytesPerLine * (image.height - j - 1))
            image.pixels.get(flippedBuffer, j * bytesPerLine, bytesPerLine)
        }

        // Then update the ByteBuffer on the image.
        image.pixels.clear()
        image.pixels.put(flippedBuffer)
        image.pixels.clear()
    }

}