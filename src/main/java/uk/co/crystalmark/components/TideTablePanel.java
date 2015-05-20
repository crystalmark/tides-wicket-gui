package uk.co.crystalmark.components;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import es.tidetim.tideengine.models.TimedValue;

public class TideTablePanel extends Panel {
	
	private static final DecimalFormat HEIGHT_FORMATTER = new DecimalFormat("#.##");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:00");


	public TideTablePanel(String id, IModel<List<TimedValue>> tidesModel) {
		super(id, tidesModel);

        add(new ListView<TimedValue>("times", tidesModel) {
            @Override
            protected void populateItem(ListItem<TimedValue> listItem) {
                listItem.add(new Label("time", TIME_FORMATTER.format(listItem.getModelObject().getCalendar())));
                listItem.add(new Label("height", "" + HEIGHT_FORMATTER.format(listItem.getModelObject().getValue())));
                listItem.add(new Label("type", "" + listItem.getModelObject().getType()));
            }
        });
	}

}
