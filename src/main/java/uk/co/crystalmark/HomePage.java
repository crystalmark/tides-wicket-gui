package uk.co.crystalmark;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.crystalmark.components.BootstrapDropdownPanel;
import uk.co.crystalmark.components.TideGraphPanel;
import uk.co.crystalmark.components.TideTablePanel;
import uk.co.crystalmark.config.LocalDateConverter;
import uk.co.crystalmark.services.TidesService;
import es.tidetim.tideengine.models.TimedValue;

public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = LoggerFactory.getLogger(HomePage.class);

    @SpringBean
    TidesService tidesService;

    private String station = "Leith, Scotland";

    private LocalDate date = LocalDate.now();
    
    public HomePage(final PageParameters parameters) {
        super(parameters);

        setStation(parameters);
        setDate(parameters);    	

        LoadableDetachableModel<List<String>> stationsModel = new LoadableDetachableModel<List<String>>() {
			@Override
            protected List<String> load() {
                return tidesService.getStations().stream().filter(s -> s.matches(".*(England|Scotland|Wales).*")).collect(Collectors.toList());
            }
        };

        IModel<String> stationModel = new PropertyModel<>(this, "station");

        IModel<Date> dateModel = new DateToLocalDateModel(new PropertyModel<LocalDate>(this, "date"));

        IModel<List<TimedValue>> tidesModel = new LoadableDetachableModel<List<TimedValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
            protected List<TimedValue> load() {
                return tidesService.getToday(HomePage.this.station);
            }
        };

        add(new TideTablePanel("times", tidesModel));


        final IModel<List<TimedValue>> hourlyTidesModel = new LoadableDetachableModel<List<TimedValue>>() {

            @Override
            protected List<TimedValue> load() {
                return tidesService.getHourly(HomePage.this.station);
            }
        };

        add(new TideGraphPanel("chart", hourlyTidesModel, stationModel));

        BootstrapDropdownPanel<String> ddc = new BootstrapDropdownPanel<String>("stations", stationModel, stationsModel) {
            @Override
            public void onChange(AjaxRequestTarget target, String station) {
                PageParameters parameters = new PageParameters();
                parameters.set("location", station);
                parameters.set("date", date);
                setResponsePage(HomePage.class, parameters);
            }
        };
        add(ddc);
        
        
		add(DateTextField.forDatePattern("date", dateModel, "yyyy-MM-dd" ).add(new AjaxFormComponentUpdatingBehavior("onupdate"){

            @Override
            protected void onUpdate(final AjaxRequestTarget target){
                PageParameters parameters = new PageParameters();
                parameters.set("location", station);
                parameters.set("date", LocalDateConverter.FORMATTER.format(date));
                setResponsePage(HomePage.class, parameters);
            }
            
            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e){
            	LOG.error("Unable to proccess new date", e);
            }
        }));

    }

    private void setDate(PageParameters parameters) {
        StringValue dateValue = parameters.get("date");
        if (!dateValue.isNull()) {
        	String dateString = dateValue.toString();
            setDate(LocalDate.parse(dateString, LocalDateConverter.FORMATTER));
        }		
	}

	private void setStation(PageParameters parameters) {
        StringValue location = parameters.get("location");
        if (!location.isNull()) {
            setStation(location.toOptionalString());
        }
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
