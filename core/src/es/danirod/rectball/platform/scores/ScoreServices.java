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

package es.danirod.rectball.platform.scores;

/**
 * Score services. Each implementation might do a particular thing. For instance
 * desktop would want to save information about scores in a file located in the
 * hard drive or Android would try to send it to Google Play if it's connected
 * to a Google Play application.
 *
 * @author danirod
 * @since 0.4.0
 */
public interface ScoreServices {

    void registerScore(int score, int time);

    int getHighScore();

    int getHighTime();

    void readData();

    void flushData();

}
