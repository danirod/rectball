package es.danirod.rectball.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.Map;

import es.danirod.rectball.statistics.StatisticSet;
import es.danirod.rectball.statistics.Statistics;

public class StatsTable extends Table {

    private Statistics stats;

    private LabelStyle title;

    private LabelStyle data;

    public StatsTable(Statistics stats, LabelStyle title, LabelStyle data) {
        this.stats = stats;
        this.title = title;
        this.data = data;

        addDataSet("Total data", stats.getTotalData());
        addDataSet("Color data", stats.getColorData());
        addDataSet("Sizes data", stats.getSizesData());
    }

    private void addDataSet(String title, StatisticSet set) {
        // Título de la sección.
        add(new Label(title, this.title)).colspan(2).row();

        // Agrega las distintas propiedades.
        for (Map.Entry<String, Integer> stat : set.getStats().entrySet()) {
            add(new Label(stat.getKey(), data)).fillX();
            add(new Label(Integer.toString(stat.getValue()), data))
                    .align(Align.right).expandX().row();
        }
    }

}
