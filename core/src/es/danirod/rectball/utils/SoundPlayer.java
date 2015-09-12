/*
 * This file is part of Rectball.
 * Copyright (C) 2015 Dani Rodríguez.
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
package es.danirod.rectball.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import es.danirod.rectball.RectballGame;

public class SoundPlayer {

    public enum SoundCode {
        GAME_OVER("sound/gameover.ogg"),
        SELECT("sound/select.ogg"),
        UNSELECT("sound/unselect.ogg"),
        SUCCESS("sound/success.ogg"),
        FAIL("sound/fail.ogg");

        protected final String internalPath;

        SoundCode(String path) {
            internalPath = path;
        }
    }

    private RectballGame game;

    public SoundPlayer(RectballGame game) {
        this.game = game;
    }

    public void playSound(SoundCode code) {
        if (!game.settings.isSoundEnabled()) {
            return;
        }

        Sound sound = game.manager.get(code.internalPath, Sound.class);
        float randomPitch = MathUtils.random(0.7f, 1.3f);
        sound.play(0.7f, randomPitch, 0);
    }
}
