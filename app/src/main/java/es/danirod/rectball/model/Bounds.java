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
package es.danirod.rectball.model;

import java.util.List;

public class Bounds {

    public final int minX;
    public final int minY;
    public final int maxX;
    public final int maxY;

    public Bounds() {
        // No arg constructor.
        this.minX = this.minY = this.maxX = this.maxY = 0;
    }

    public Bounds(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public Bounds(Coordinate c1, Coordinate c2) {
        this.minX = Math.min(c1.x, c2.x);
        this.maxX = Math.max(c1.x, c1.x);
        this.minY = Math.min(c1.y, c2.y);
        this.maxY = Math.max(c1.y, c2.y);
    }

    public int rows() {
        return maxY - minY + 1;
    }

    public int cols() {
        return maxX - minX + 1;
    }

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

    public boolean inBounds(int x, int y) {
        return (x >= minX && x <= maxX) && (y >= minY && y <= maxY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bounds bounds = (Bounds) o;

        return minX == bounds.minX && minY == bounds.minY && maxX == bounds.maxX && maxY == bounds.maxY;

    }

    @Override
    public int hashCode() {
        int result = minX;
        result = 31 * result + minY;
        result = 31 * result + maxX;
        result = 31 * result + maxY;
        return result;
    }
}
