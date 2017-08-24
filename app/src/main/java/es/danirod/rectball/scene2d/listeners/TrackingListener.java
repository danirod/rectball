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
package es.danirod.rectball.scene2d.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import es.danirod.rectball.RectballGame;

/**
 * ChangeListener that dispatches an analytic analyticEvent at the same time it executes
 * another action such as a screen change.
 *
 * @author danirod
 * @since 0.4.0
 */
public class TrackingListener extends ChangeListener {

    private RectballGame game;

    private ChangeListener after;

    private String action, category, label;

    public TrackingListener(RectballGame game, String category,
                            String action, String label, ChangeListener after) {
        this.game = game;
        this.category = category;
        this.action = action;
        this.label = label;
        this.after = after;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        game.getContext().getAnalytics().sendEvent(category, action, label);
        after.changed(event, actor);
    }
}
