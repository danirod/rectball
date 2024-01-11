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
package es.danirod.rectball

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle

object AssetManagerBuilder {

    fun build(): AssetManager {
        val manager = AssetManager()
        loadTextures(manager)
        loadFonts(manager)
        loadSounds(manager)
        return manager
    }

    fun addGameServices(manager: AssetManager) {
        manager.load("google/gpg_achievements.png", Texture::class.java, textureParameters)
        manager.load("google/gpg_leaderboard.png", Texture::class.java, textureParameters)
    }

    private fun loadTextures(manager: AssetManager) {
        // SkinComposer
        manager.load("skin/rectball.json", Skin::class.java, SkinParameter("skin/rectball.atlas"))

        // User interface.
        manager.load("ui/progress.png", Texture::class.java, textureParameters)
        manager.load("ui/switch.png", Texture::class.java, textureParameters)

        // Hands
        manager.load("hand-normal.png", Texture::class.java, textureParameters)
        manager.load("hand-hover.png", Texture::class.java, textureParameters)

        // Bundles
        manager.load("bundles/strings", I18NBundle::class.java)
    }

    private fun loadFonts(manager: AssetManager) {
        manager.load("fonts/monospace.fnt", BitmapFont::class.java)
    }

    private fun loadSounds(manager: AssetManager) {
        manager.load("sound/fail.ogg", Sound::class.java)
        manager.load("sound/game_over.ogg", Sound::class.java)
        manager.load("sound/perfect.ogg", Sound::class.java)
        manager.load("sound/select.ogg", Sound::class.java)
        manager.load("sound/success.ogg", Sound::class.java)
        manager.load("sound/unselect.ogg", Sound::class.java)
    }

    private val textureParameters by lazy {
        TextureLoader.TextureParameter().apply {
            minFilter = Texture.TextureFilter.Linear
            magFilter = Texture.TextureFilter.Linear
        }
    }
}
