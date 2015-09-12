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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class KonamiCodeProcessor extends InputAdapter {

    private static final int[] KEYS = {
            Input.Keys.UP,
            Input.Keys.UP,
            Input.Keys.DOWN,
            Input.Keys.DOWN,
            Input.Keys.LEFT,
            Input.Keys.RIGHT,
            Input.Keys.LEFT,
            Input.Keys.RIGHT,
            Input.Keys.B,
            Input.Keys.A
    };

    private int nextKey = 0;

    private KonamiCodeListener subscriber = null;

    public KonamiCodeProcessor(KonamiCodeListener subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == KEYS[nextKey]) {
            if (++nextKey == KEYS.length) {
                if (subscriber != null) {
                    subscriber.onKonamiCodePressed();
                    nextKey = 0;
                } else {
                    Gdx.app.log("KCP", "Konami Code pressed, nobody cares.");
                }
            }
        } else {
            nextKey = 0;
        }
        return true;
    }
}
