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

import es.danirod.rectball.scene2d.game.BallActor;

import java.util.List;

/**
 * Interface used to trigger events related to ball selection and board.
 * Events such as: a ball has been selected or unselected, or a combination
 * has been made. Possible uses: play a sound when a ball is selected,
 * check if a combination is valid...
 */
public interface BallSelectionListener {
    /**
     * This event is triggered when a selection is made and the selection has
     * already be checked to be valid. All the selected balls form a rectangle
     * whose four corners are made of the same color.
     *
     * @param selection balls that form this selection.
     */
    void onSelectionSucceeded(List<BallActor> selection);

    /**
     * This event is triggered when a selection is made and the selection is
     * checked not to be valid. Any of the conditions required to be a valid
     * selection is not happening.
     *
     * @param selection balls that don't form this selection.
     */
    void onSelectionFailed(List<BallActor> selection);

    /**
     * This event is triggered when the board is forced to unselect every ball.
     * This happens, for instance, when the game is over, so that no selected
     * balls remain on the screen while the animations start.
     *
     * @param selection the list of balls that has been unselected.
     */
    void onSelectionCleared(List<BallActor> selection);
}
