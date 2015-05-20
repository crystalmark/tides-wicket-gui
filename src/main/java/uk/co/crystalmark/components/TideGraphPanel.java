package uk.co.crystalmark.components;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.HorizontalAlignment;
import com.googlecode.wickedcharts.highcharts.options.Legend;
import com.googlecode.wickedcharts.highcharts.options.LegendLayout;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.VerticalAlignment;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;
import es.tidetim.tideengine.models.TimedValue;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TideGraphPanel extends Panel {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_TIME;

    public TideGraphPanel(String id, IModel<List<TimedValue>> model) {
        super(id, model);

        Options options = new Options();

        options
                .setChartOptions(new ChartOptions()
                        .setType(SeriesType.AREASPLINE));

        options
                .setTitle(new Title("Tide heights."));

        options
                .setxAxis(new Axis()
                        .setCategories(getTimes(model.getObject())));

        options
                .setyAxis(new Axis()
                        .setTitle(new Title("Tide Height (m)")));

        options
                .setLegend(new Legend()
                        .setLayout(LegendLayout.VERTICAL)
                        .setAlign(HorizontalAlignment.RIGHT)
                        .setVerticalAlign(VerticalAlignment.TOP)
                        .setX(-10)
                        .setY(100)
                        .setBorderWidth(0));

        options
                .addSeries(new SimpleSeries()
                        .setName("Height (m)")
                        .setData(getHeights(model.getObject())
                        ));

        add(new Chart("chart", options));


    }

    private List<Number> getHeights(List<TimedValue> values) {
        return values.stream().map(TimedValue::getValue).collect(Collectors.toList());

    }

    private List<String> getTimes(List<TimedValue> values) {
        return values.stream().map(station -> station.getCalendar().format(TIME_FORMATTER)).collect(Collectors.toList());

    }

}
