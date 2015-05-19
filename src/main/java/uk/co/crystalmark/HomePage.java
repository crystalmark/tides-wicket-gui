package uk.co.crystalmark;

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
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import uk.co.crystalmark.components.DateLabel;
import uk.co.crystalmark.services.TidesService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;

    @SpringBean
    TidesService tidesService;

    private static final String[] xaxis = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private String station = "Leith, Scotland";

    public HomePage(final PageParameters parameters) {
        super(parameters);

        IModel<List<TimedValue>> tidesModel = new LoadableDetachableModel<List<TimedValue>>() {

            @Override
            protected List<TimedValue> load() {
                return tidesService.getToday("Leith");
            }
        };

        add(new ListView<TimedValue>("times", tidesModel) {
            @Override
            protected void populateItem(ListItem<TimedValue> listItem) {
                listItem.add(new DateLabel("time", listItem.getModelObject().getCalendar()));
                listItem.add(new Label("height", "" + listItem.getModelObject().getValue()));
                listItem.add(new Label("type", "" + listItem.getModelObject().getType()));
            }
        });

        Options options = new Options();

        options
                .setChartOptions(new ChartOptions()
                        .setType(SeriesType.LINE));

        options
                .setTitle(new Title("Tide heights."));

        options
                .setxAxis(new Axis()
                        .setCategories(Arrays
                                .asList(xaxis)));

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
                        .setData(getHeights(tidesService.getHourly("Leith"))
                        ));

        add(new Chart("chart", options));

        Form form = new Form("form");
        add(form);

        DropDownChoice<String> ddc =
                new DropDownChoice<>("station",
                        new PropertyModel<>(this, "station"),
                        new LoadableDetachableModel<List<String>>() {
                            @Override
                            protected List<String> load() {
                                return tidesService.getStations();
                            }
                        }
                );
        form.add(ddc);

    }

    private List<Number> getHeights(List<TimedValue> values) {
        return values.stream().map(TimedValue::getValue).collect(Collectors.toList());
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
