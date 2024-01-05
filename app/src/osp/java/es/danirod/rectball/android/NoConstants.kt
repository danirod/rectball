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
package es.danirod.rectball.android

class NoConstants: GameServicesConstants {
    override val leaderboardHighestScore: String
        get() = "null"
    override val leaderboardHighestLength: String
        get() = "null"
    override val achievementStarter: String
        get() = "null"
    override val achievementExperienced: String
        get() = "null"
    override val achievementMaster: String
        get() = "null"
    override val achievementScoreBreaker: String
        get() = "null"
    override val achievementCasualPlayer: String
        get() = "null"
    override val achievementPerserverant: String
        get() = "null"
    override val achievementCommuter: String
        get() = "null"
}
