/*
 * This file is part of Rectball
 * Copyright (C) 2015-2017 Dani Rodríguez
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

package es.danirod.rectball.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.platform.StatisticsIO;

public class StatsTable extends Table {

    private final RectballGame game;

    private final LabelStyle title;

    private final LabelStyle data;

    public StatsTable(RectballGame game, LabelStyle title, LabelStyle data) {
        this.game = game;
        this.title = title;
        this.data = data;

        defaults().padBottom(30).padRight(10);
        add(addBestScores()).fillX().expandX().row();
        add(addTotalData()).fillX().expandX().row();
        add(addColorData()).fillX().expandX().row();
        add(addSizesData()).fillX().expandX().row();
    }

    private static String secondsToTime(int seconds) {
        int hrs = seconds / 3600;
        int min = (seconds % 3600) / 60;
        int sec = (seconds % 3600) % 60;

        if (hrs != 0) {
            return String.format("%d:%02d:%02d", hrs, min, sec);
        } else if (min != 0) {
            return String.format("%d:%02d", min, sec);
        } else {
            return String.format("%d", sec);
        }
    }

    private Table addBestScores() {
        Table best = new Table();
        best.add(new Label(game.getLocale().get("statistics.best"), this.title)).colspan(2).row();

        boolean printedSomething = false;
        // Add best score.
        if (game.getScores().getHighScore() != 0) {
            String bestScore = Long.toString(game.getScores().getHighScore());
            best.add(new Label(game.getLocale().get("statistics.best.score"), data)).align(Align.left).fillX();
            best.add(new Label(bestScore, data)).align(Align.right).expandX().row();
            printedSomething = true;
        }

        // Add best time.
        if (game.getScores().getHighTime() != 0) {
            String bestTime = secondsToTime(game.getScores().getHighTime());
            best.add(new Label(game.getLocale().get("statistics.best.time"), data)).align(Align.left).fillX();
            best.add(new Label(bestTime, data)).align(Align.right).expandX().row();
            printedSomething = true;
        }

        // If everything is 0, don't print anything.
        if (!printedSomething) {
            Label noData = new Label(game.getLocale().get("statistics.noData"), game.getSkin());
            noData.setAlignment(Align.center);
            best.add(noData).colspan(2).fillX().expandX().padTop(10).padBottom(10).row();
        }

        return best;
    }

    private Table addTotalData() {
        Table total = new Table();
        total.add(new Label(game.getLocale().get("statistics.total"), this.title)).colspan(2).row();

        StatisticsIO.StatisticSet set = game.getStatistics().getTotalData();
        if (set.getStats().isEmpty()) {
            Label noData = new Label(game.getLocale().get("statistics.noData"), game.getSkin());
            noData.setAlignment(Align.center);
            total.add(noData).colspan(2).fillX().expandX().padTop(10).padBottom(10).row();
            return total;
        }

        for (Map.Entry<String, Integer> stat : set.getStats().entrySet()) {
            String statName = game.getLocale().get("statistics.total." + stat.getKey());
            String statValue = Integer.toString(stat.getValue());

            if (stat.getKey().equals("time")) {
                statValue = secondsToTime(stat.getValue());
            }

            total.add(new Label(statName, data)).align(Align.left).fillX();
            total.add(new Label(statValue, data)).align(Align.right).expandX().row();
        }

        return total;
    }

    private Table addColorData() {
        Table color = new Table();
        color.add(new Label(game.getLocale().get("statistics.color"), this.title)).colspan(4).row();

        if (game.getStatistics().getColorData().getStats().isEmpty()) {
            Label noData = new Label(game.getLocale().get("statistics.noData"), game.getSkin());
            noData.setAlignment(Align.center);
            color.add(noData).colspan(4).fillX().expandX().padTop(10).padBottom(10).row();
            return color;
        }

        // Put the data in a TreeMap. TreeMaps keep order. Use reverse to show bigger values on left.
        Map<Integer, List<String>> colorScore = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, Integer> stat : game.getStatistics().getColorData().getStats().entrySet()) {
            if (!colorScore.containsKey(stat.getValue())) {
                colorScore.put(stat.getValue(), new ArrayList<String>());
            }
            colorScore.get(stat.getValue()).add(stat.getKey());
        }

        color.defaults().expandX().fillX().align(Align.center).size(60).padTop(5);
        for (Map.Entry<Integer, List<String>> entry : colorScore.entrySet()) {
            for (String ballColor : entry.getValue()) {
                color.add(new Image(game.getBallAtlas().findRegion("ball_" + ballColor)));
            }
        }
        color.row();
        for (Map.Entry<Integer, List<String>> entry : colorScore.entrySet()) {
            for (String ballColor : entry.getValue()) {
                Label label = new Label(entry.getKey().toString(), data);
                label.setAlignment(Align.center);
                label.setUserObject(ballColor);
                color.add(label);
            }
        }
        color.row();

        return color;
    }

    private Table addSizesData() {
        Table sizes = new Table();
        sizes.add(new Label(game.getLocale().get("statistics.sizes"), this.title)).colspan(3).row();

        Map<String, Integer> unsortedScores = game.getStatistics().getSizesData().getStats();
        if (unsortedScores.isEmpty()) {
            Label noData = new Label(game.getLocale().get("statistics.noData"), game.getSkin());
            noData.setAlignment(Align.center);
            sizes.add(noData).colspan(3).fillX().expandX().padTop(10).padBottom(10).row();
            return sizes;
        }

        Map<Integer, List<String>> sizesScore = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, Integer> score : unsortedScores.entrySet()) {
            if (!sizesScore.containsKey(score.getValue())) {
                sizesScore.put(score.getValue(), new ArrayList<String>());
            }
            sizesScore.get(score.getValue()).add(score.getKey());
        }

        int highestValue = -1;
        Drawable bar = game.getSkin().newDrawable("pixel", Color.WHITE);
        for (Map.Entry<Integer, List<String>> score : sizesScore.entrySet()) {
            // Put the highest value. This should be done using the first item.
            if (highestValue == -1) {
                highestValue = score.getKey();
            }

            for (String size : score.getValue()) {
                float percentage = (float) score.getKey() / highestValue;
                sizes.add(new Label(size, data)).align(Align.left).fillX();
                sizes.add(new Label(score.getKey().toString(), data)).align(Align.right).padRight(10).expandX();
                sizes.add(new Image(bar)).width(240 * percentage).padLeft(240 * (1 - percentage)).padBottom(5).fill().row();
            }
        }

        return sizes;
    }
}
