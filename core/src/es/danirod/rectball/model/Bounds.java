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
package es.danirod.rectball.model;

import java.util.List;

public class Bounds {

    public static Bounds fromBallList(List<Ball> balls) {
        int minX, minY, maxX, maxY;
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;
        for (Ball ball : balls) {
            minX = Math.min(minX, ball.getX());
            minY = Math.min(minY, ball.getY());
            maxX = Math.max(maxX, ball.getX());
            maxY = Math.max(maxY, ball.getY());
        }
        return new Bounds(minX, minY, maxX, maxY);
    }

    public int minX, minY, maxX, maxY;

    public Bounds(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public boolean inBounds(int x, int y) {
        return (x >= minX && x <= maxX) && (y >= minY && y <= maxY);
    }
}
