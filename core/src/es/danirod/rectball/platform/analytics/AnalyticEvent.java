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

package es.danirod.rectball.platform.analytics;

import java.util.*;

/**
 * Event tracked in the analytic services.
 *
 * @author danirod
 * @since 0.4.0
 */
public class AnalyticEvent {

    /** Information stored by the user. */
    private final Map<String, Object> userData;

    /**
     * Create a new analytic event.
     */
    public AnalyticEvent() {
        userData = new HashMap<>();
    }

    /**
     * Add some user data to the event using a key. If any data was previously
     * stored in this event with the same key, the old value is replaced by
     * the new value.
     * @param key  the key to use for this data.
     * @param value  the value for this data.
     */
    public void setUserData(String key, Object value) {
        userData.put(key, value);
    }

    /**
     * Get the value for a particular data stored in this event given the key.
     * @param key  the key for the data to be retrieved.
     * @return  the value for the requested data.
     */
    public Object getUserData(String key) {
        return userData.get(key);
    }

    /**
     * This method returns a map with all the data available in this event.
     * The returned map is a read-only map where every entry is a user data
     * previously placed in the event.
     * @return  user data for this event.
     */
    public Map<String, Object> getAllUserData() {
        return Collections.unmodifiableMap(userData);
    }

}
