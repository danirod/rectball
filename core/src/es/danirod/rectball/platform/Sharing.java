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

package es.danirod.rectball.platform;

import com.badlogic.gdx.graphics.Pixmap;

/**
 * Sharing services are services designed to share information about the
 * game. For instance, sharing screenshots on the final score, or sharing
 * the score through an online scoreboard such as Google Play Services.
 *
 * @author danirod
 * @since 0.4.0
 */
public interface Sharing {

    /**
     * Share a screenshot from the game. The platform should decide what to
     * do with the screenshot. On Android, this could ask the user to share
     * the screenshot in services such as Twitter or WhatsApp. On desktop
     * the user might want to store the screenshot in a folder.
     *
     * @param pixmap the pixmap with the image data.
     */
    void shareScreenshot(Pixmap pixmap);

    /**
     * Share a screenshot from the game using the score and time. This is
     * useful when the user has lost the game and want to share their score
     * with other people using a tweet or sending it via WhatsApp.
     *
     * @param pixmap the pixmap with the image data.
     * @param score  the score the player has or had.
     * @param time   the time the player has lasted.
     */
    void shareGameOverScreenshot(Pixmap pixmap, int score, int time);

}
