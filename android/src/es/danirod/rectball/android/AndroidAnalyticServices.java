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

package es.danirod.rectball.android;

import com.badlogic.gdx.Gdx;
import es.danirod.rectball.platform.AnalyticServices;
import es.danirod.rectball.platform.analytics.AnalyticEvent;

/**
 * Android implementation for the analytic services. This is a dummy
 * implementation because at the moment no analytic services are being used
 * in this version. However, it's open for anybody to use.
 *
 * @author danirod
 * @since 0.4.0
 */
public class AndroidAnalyticServices implements AnalyticServices {

    @Override
    public void sendEvent(AnalyticEvent event) {
        Gdx.app.log("AnalyticServices", "Received an event");
    }
}
