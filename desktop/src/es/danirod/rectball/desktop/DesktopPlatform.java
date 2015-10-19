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

package es.danirod.rectball.desktop;

import es.danirod.rectball.platform.*;
import es.danirod.rectball.platform.analytics.AnalyticServices;
import es.danirod.rectball.platform.sharing.SharingServices;

/**
 * This contains code for desktop platform. Here code that uses desktop JRE
 * or APIs such as Swing can be used. This code won't run on any other
 * platform.
 *
 * @author danirod
 * @since 0.4.0
 */
public class DesktopPlatform implements Platform {

    private SharingServices sharing;

    private AnalyticServices analytic;

    protected DesktopPlatform() {
        sharing = new DesktopSharingServices();
        analytic = new DesktopAnalyticServices();
    }

    @Override
    public SharingServices sharing() {
        return sharing;
    }

    @Override
    public AnalyticServices analytic() {
        return analytic;
    }
}
