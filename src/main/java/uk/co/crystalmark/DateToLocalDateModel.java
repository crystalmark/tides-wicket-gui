package uk.co.crystalmark;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class DateToLocalDateModel implements IModel<Date> {

	private IModel<LocalDate> parentModel;
	
	public DateToLocalDateModel(PropertyModel<LocalDate> model) {
		this.parentModel = model;
	}

	@Override
	public void detach() {
		parentModel.detach();
	}

	@Override
	public Date getObject() {
		LocalDate date = parentModel.getObject();
		if (date == null) {
			return null;
		} else {
			Instant instant = date.atStartOfDay()
					.atZone(ZoneId.systemDefault()).toInstant();
			return Date.from(instant);
		}
	}

	@Override
	public void setObject(Date date) {
		if (date != null) {
			Instant instant = Instant.ofEpochMilli(date.getTime());
			parentModel.setObject(LocalDate.from(instant));
		} else {
			parentModel.setObject(null);
		}
	}

}
