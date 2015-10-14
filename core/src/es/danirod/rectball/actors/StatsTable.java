package es.danirod.rectball.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import java.util.*;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.statistics.StatisticSet;
import es.danirod.rectball.statistics.Statistics;

public class StatsTable extends Table {

    private RectballGame game;

    private Statistics stats;

    private LabelStyle title;

    private LabelStyle data;

    public StatsTable(RectballGame game, LabelStyle title, LabelStyle data) {
        this.game = game;
        this.stats = game.statistics;
        this.title = title;
        this.data = data;

        defaults().padBottom(30);
        add(addBestScores()).fillX().expandX().row();
        add(addTotalData()).fillX().expandX().row();
        add(addColorData()).fillX().expandX().row();
        add(addSizesData()).fillX().expandX().row();
    }

    private Table addBestScores() {
        Table best = new Table();
        best.add(new Label(game.getLocale().get("statistics.best"), this.title)).colspan(2).row();

        boolean printedSomething = false;
        // Add best score.
        if (game.scores.getHighestScore() != 0) {
            String bestScore = Long.toString(game.scores.getHighestScore());
            best.add(new Label(game.getLocale().get("statistics.best.score"), data)).align(Align.left).fillX();
            best.add(new Label(bestScore, data)).align(Align.right).expandX().row();
            printedSomething = true;
        }

        // Add best time.
        if (game.scores.getHighestTime() != 0) {
            String bestTime = secondsToTime(game.scores.getHighestTime());
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

        StatisticSet set = game.statistics.getTotalData();
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

        if (game.statistics.getColorData().getStats().isEmpty()) {
            Label noData = new Label(game.getLocale().get("statistics.noData"), game.getSkin());
            noData.setAlignment(Align.center);
            color.add(noData).colspan(4).fillX().expandX().padTop(10).padBottom(10).row();
            return color;
        }

        // Put the data in a TreeMap. TreeMaps keep order. Use reverse to show biggers on left.
        Map<Integer, String> colorScore = new TreeMap<>(Collections.reverseOrder());
        colorScore.put(game.statistics.getColorData().getValue("red"), "red");
        colorScore.put(game.statistics.getColorData().getValue("green"), "green");
        colorScore.put(game.statistics.getColorData().getValue("blue"), "blue");
        colorScore.put(game.statistics.getColorData().getValue("yellow"), "yellow");

        color.defaults().expandX().fillX().align(Align.center).size(60).padTop(5);
        for (Map.Entry<Integer, String> entry : colorScore.entrySet()) {
            color.add(new Image(game.getBallAtlas().findRegion("ball_" + entry.getValue())));
        }
        color.row();
        for (Map.Entry<Integer, String> entry : colorScore.entrySet()) {
            Label label = new Label(entry.getKey().toString(), data);
            label.setAlignment(Align.center);
            color.add(label);
        }
        color.row();

        return color;
    }

    private Table addSizesData() {
        Table sizes = new Table();
        sizes.add(new Label(game.getLocale().get("statistics.sizes"), this.title)).colspan(3).row();

        Map<String, Integer> unsortedScores = game.statistics.getSizesData().getStats();
        if (unsortedScores.isEmpty()) {
            Label noData = new Label(game.getLocale().get("statistics.noData"), game.getSkin());
            noData.setAlignment(Align.center);
            sizes.add(noData).colspan(3).fillX().expandX().padTop(10).padBottom(10).row();
            return sizes;
        }

        Map<Integer, String> sizesScore = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, Integer> score : unsortedScores.entrySet()) {
            sizesScore.put(score.getValue(), score.getKey());
        }

        int highestValue = -1;
        Drawable bar = game.getSkin().newDrawable("pixel", Color.WHITE);
        for (Map.Entry<Integer, String> score : sizesScore.entrySet()) {
            // Put the highest value. This should be done using the first item.
            if (highestValue == -1) {
                highestValue = score.getKey();
            }

            sizes.add(new Label(score.getValue(), data)).align(Align.left).fillX();
            sizes.add(new Label(score.getKey().toString(), data)).align(Align.right).padRight(10).expandX();

            // Add a bar to the right
            float percentage = (float) score.getKey() / highestValue;
            sizes.add(new Image(bar)).width(240 * percentage).padLeft(240 * (1 - percentage)).padBottom(5).fill().row();
        }

        return sizes;
    }

    private static String secondsToTime(int seconds) {
        if (seconds < 60) {
            return Integer.toString(seconds);
        } else {
            String min = Integer.toString(seconds / 60);
            String sec = Integer.toString(seconds % 60);
            if (sec.length() == 1) {
                sec = "0" + sec;
            }
            return min + ":" + sec;
        }
    }
}
