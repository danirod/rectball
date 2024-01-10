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
package es.danirod.rectball;

/**
 * Class for holding constant values to keep them centralized.
 */
public class Constants {

    /**
     * The width of the Scene2D stage. The bigger this value is, the greater
     * the space is going to be for child elements of this screen. However,
     * the smallest everything is going to be seen, specially in small screen.s
     */
    public static final int VIEWPORT_WIDTH = 480;

    /**
     * The height of the Scene2D stage. The bigger this value is, the greater
     * the space is going to be for child elements of this screen. However,
     * the smallest everything is going to be seen, specially in small screens.
     */
    public static final int VIEWPORT_HEIGHT = 768;

    /**
     * The padding for the table in every stage.
     */
    public static final int STAGE_PADDING = 20;

    public static final int SECONDS = 30;
}
