package uk.co.crystalmark;

import es.tidetim.tideengine.models.TimedValue;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import uk.co.crystalmark.components.TideGraphPanel;
import uk.co.crystalmark.services.TidesService;
import uk.co.crystalmark.wicket.components.BootstrapDropdownPanel;

import java.util.List;

public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;

    @SpringBean
    TidesService tidesService;

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


        LoadableDetachableModel<List<String>> stationsModel = new LoadableDetachableModel<List<String>>() {
            @Override
            protected List<String> load() {
                return tidesService.getStations();
            }
        };

        BootstrapDropdownPanel<String> ddc = new BootstrapDropdownPanel<String>("stations", new PropertyModel<String>(this, "station"), stationsModel) {
            @Override
            public void onChange(AjaxRequestTarget ajaxRequestTarget, String s) {

            }
        };

        add(ddc);

        IModel<List<TimedValue>> hourlyTidesModel = new LoadableDetachableModel<List<TimedValue>>() {

            @Override
            protected List<TimedValue> load() {
                return tidesService.getHourly("Leith");
            }
        };

        add(new TideGraphPanel("chart", hourlyTidesModel));
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
