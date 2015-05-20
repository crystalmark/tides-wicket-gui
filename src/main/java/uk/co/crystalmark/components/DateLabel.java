package uk.co.crystalmark.components;

import org.apache.wicket.markup.html.basic.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateLabel extends Label {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    public DateLabel(String id, LocalDateTime time) {
        super(id, time != null ? time.format(formatter) : "No Time");
    }
}
