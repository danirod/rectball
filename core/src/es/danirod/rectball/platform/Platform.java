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

import es.danirod.rectball.platform.analytics.AnalyticServices;
import es.danirod.rectball.platform.sharing.SharingServices;

/**
 * This is the interface for platform code. Platform code is code that depends
 * on the platform that the application is running. Whenever features can only
 * in a particular platform, platform dependent code should be used. For
 * instance, adding Google APIs or Android APIs must be done in Android code
 * to prevent desktop releases from failing.
 *
 * @author danirod
 * @since 0.4.0
 */
public interface Platform {

    /**
     * Get the sharing services instance attached to this platform.
     * @return  sharing services instance.
     */
    SharingServices sharing();

    AnalyticServices analytic();

}
