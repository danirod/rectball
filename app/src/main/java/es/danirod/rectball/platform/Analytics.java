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

import java.util.Map;

/**
 * This interface provides analytic services integration. Analytic services
 * allow to get information about the game. This can be used, for instance,
 * for getting information about the game during an error report.
 *
 * @author danirod
 * @since 0.4.0
 */
public interface Analytics {

    void sendEvent(String category, String action);

    void sendEvent(String category, String action, String label);

    void sendScreen(String screenID);

    void sendEventWithDimensions(String category, String action, Map<Integer, String> dimensions);

    void sendEventWithMetrics(String category, String action, Map<Integer, Float> metrics);

    void sendEventWithDimensionsAndMetrics(String category, String action, Map<Integer, String> dimensions, Map<Integer, Float> metrics);
}
