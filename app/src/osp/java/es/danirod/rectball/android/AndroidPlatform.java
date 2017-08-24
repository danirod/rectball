/*
 * This file is part of Rectball.
 * Copyright (C) 2015-2017 Dani Rodríguez.
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

class AndroidPlatform extends AbstractPlatform {

    private NullAnalytics analytics = new NullAnalytics();

    private NullGameServices services = new NullGameServices();

    protected AndroidPlatform(AndroidLauncher context) {
        super(context);
    }

    @Override
    public Analytics getAnalytics() {
        return analytics;
    }

    @Override
    public GameServices getGameServices() {
        return services;
    }
}
