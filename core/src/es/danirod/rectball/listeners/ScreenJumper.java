/*
 * This file is part of Rectball
 * Copyright (C) 2015 Dani Rodríguez
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

package es.danirod.rectball.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.utils.SoundPlayer;

public class ScreenJumper extends ChangeListener {

    private final RectballGame game;

    private final int target;

    public ScreenJumper(RectballGame game, int target) {
        this.game = game;
        this.target = target;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        game.player.playSound(SoundPlayer.SoundCode.SUCCESS);
        game.pushScreen(target);

        // Cancel the event to avoid checking the actor
        event.cancel();
    }


}
