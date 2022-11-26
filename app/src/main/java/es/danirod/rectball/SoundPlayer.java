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
package es.danirod.rectball;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

import es.danirod.rectball.android.settings.SettingsManager;

public class SoundPlayer {

    private RectballGame game;

    public SoundPlayer(RectballGame game) {
        this.game = game;
    }

    public void playSound(SoundCode code) {
        if (canPlaySound()) {
            Sound sound = game.manager.get(code.internalPath);
            float pitch = MathUtils.random(0.7f, 1.3f);
            sound.play(1f, pitch, 0);
        }
    }

    private boolean canPlaySound() {
        return game.getContext().getSettings().getPreferences().getBoolean(SettingsManager.TAG_ENABLE_SOUND, true);
    }

    public static enum SoundCode {
        GAME_OVER("sound/game_over.ogg"),
        SELECT("sound/select.ogg"),
        UNSELECT("sound/unselect.ogg"),
        SUCCESS("sound/success.ogg"),
        FAIL("sound/fail.ogg"),
        PERFECT("sound/perfect.ogg");

        String internalPath;
        SoundCode(String internalPath) {
            this.internalPath = internalPath;
        }
    }
}