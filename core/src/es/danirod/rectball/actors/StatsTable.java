package es.danirod.rectball.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.Map;

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

        addDataSet("statistics.total", stats.getTotalData(), true);
        addDataSet("statistics.color", stats.getColorData(), true);
        addDataSet("statistics.sizes", stats.getSizesData(), false);
    }

    private void addDataSet(String title, StatisticSet set, boolean translateKeys) {
        // Título de la sección.
        add(new Label(game.getLocale().get(title), this.title)).colspan(2).row();

        // Agrega las distintas propiedades.
        for (Map.Entry<String, Integer> stat : set.getStats().entrySet()) {
            String statName = translateKeys ? game.getLocale().get(title + "." + stat.getKey()) : stat.getKey();
            String statValue = Integer.toString(stat.getValue());

            if (title.equals("statistics.total") && stat.getKey().equals("time")) {
                int minutes = stat.getValue() / 60;
                int seconds = stat.getValue() % 60;
                statValue = minutes + ":" + (seconds > 10 ? seconds : "0" + seconds);
            }

            add(new Label(statName, data)).fillX();
            add(new Label(statValue, data)).align(Align.right).expandX().row();
        }
    }

}
