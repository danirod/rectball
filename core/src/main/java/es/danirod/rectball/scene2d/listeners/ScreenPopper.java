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
package es.danirod.rectball.scene2d.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.SoundPlayer;

/**
 * @since 0.3.0
 */
public class ScreenPopper extends ChangeListener {

    private final RectballGame game;

    /**
     * If true, the event will clear the stack, returning to the main menu.
     */
    private final boolean popAll;

    public ScreenPopper(RectballGame game) {
        this(game, false);
    }

    public ScreenPopper(RectballGame game, boolean popAll) {
        this.game = game;
        this.popAll = popAll;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        game.player.playSound(SoundPlayer.SoundCode.FAIL);
        if (popAll) {
            game.clearScreenStack();
        } else {
            game.popScreen();
        }

        // Cancel the event to avoid checking the actor
        event.cancel();
    }


}
