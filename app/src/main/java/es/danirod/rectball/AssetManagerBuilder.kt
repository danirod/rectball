/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2023 Dani Rodríguez.
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

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.PixmapPacker
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import es.danirod.rectball.android.BuildConfig
import kotlin.math.ceil

object AssetManagerBuilder {

    fun build(): AssetManager {
        val manager = AssetManager()
        registerFreetypeLoaders(manager)
        loadTextures(manager)
        loadFonts(manager)
        loadSounds(manager)
        return manager
    }

    @Suppress("ConstantConditionIf")
    private fun loadTextures(manager: AssetManager) {
        // SkinComposer
        manager.load("skin/rectball.json", Skin::class.java, SkinParameter("skin/rectball.atlas"))

        // Graphic assets.
        manager.load("logo.png", Texture::class.java, textureParameters)

        // User interface.
        manager.load("ui/icons.png", Texture::class.java, textureParameters)
        manager.load("ui/progress.png", Texture::class.java, textureParameters)
        manager.load("ui/switch.png", Texture::class.java, textureParameters)
        manager.load("ui/yellow_patch.png", Texture::class.java, textureParameters)

        // Hands
        manager.load("hand-normal.png", Texture::class.java, textureParameters)
        manager.load("hand-hover.png", Texture::class.java, textureParameters)

        // Google Play Games integration.
        if (BuildConfig.FLAVOR == "gpe") {
            manager.load("google/gpg_achievements.png", Texture::class.java, textureParameters)
            manager.load("google/gpg_leaderboard.png", Texture::class.java, textureParameters)
        }
    }

    private fun loadFonts(manager: AssetManager) {
        // Load BMF fonts.
        manager.load("fonts/monospace.fnt", BitmapFont::class.java)
        manager.load("fonts/monospaceOutline.fnt", BitmapFont::class.java)

        // Load TrueType fonts
        manager.load("fonts/normal.ttf", BitmapFont::class.java,
                fontParameters("fonts/Coda-Regular.ttf", Texture.TextureFilter.Linear, 28))
        manager.load("fonts/small.ttf", BitmapFont::class.java,
                fontParameters("fonts/Coda-Regular.ttf", Texture.TextureFilter.Linear, 23))
        manager.load("fonts/large.ttf", BitmapFont::class.java,
                fontParameters("fonts/Coda-Regular.ttf", Texture.TextureFilter.Linear, 28 * 2))
    }

    private fun loadSounds(manager: AssetManager) {
        manager.load("sound/fail.ogg", Sound::class.java)
        manager.load("sound/game_over.ogg", Sound::class.java)
        manager.load("sound/perfect.ogg", Sound::class.java)
        manager.load("sound/select.ogg", Sound::class.java)
        manager.load("sound/success.ogg", Sound::class.java)
        manager.load("sound/unselect.ogg", Sound::class.java)
    }

    private val freeTypePacker = PixmapPacker(1024, 1024, Pixmap.Format.RGBA8888, 2, false)

    private fun fontParameters(name: String, filter: Texture.TextureFilter, size: Int) = FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
        fontFileName = name
        fontParameters.minFilter = filter
        fontParameters.magFilter = filter
        fontParameters.size = ceil(size.toDouble() * Gdx.graphics.density).toInt()
        fontParameters.packer = freeTypePacker
    }

    private fun registerFreetypeLoaders(manager: AssetManager) {
        val resolver = InternalFileHandleResolver()
        manager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        manager.setLoader(BitmapFont::class.java, ".ttf", object: FreetypeFontLoader(resolver) {
            override fun loadSync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: FreeTypeFontLoaderParameter?): BitmapFont {
                val font = super.loadSync(manager, fileName, file, parameter)
                font.data.setScale(1f / Gdx.graphics.density)
                return font
            }
        })
    }

    private val textureParameters by lazy {
        TextureLoader.TextureParameter().apply {
            minFilter = Texture.TextureFilter.Linear
            magFilter = Texture.TextureFilter.Linear
        }
    }
}