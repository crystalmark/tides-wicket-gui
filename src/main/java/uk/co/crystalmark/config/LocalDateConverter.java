package uk.co.crystalmark.config;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateConverter implements IConverter<LocalDate> {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate convertToObject(String s, Locale locale) throws ConversionException {
        return LocalDate.parse(s, FORMATTER);
    }

    @Override
    public String convertToString(LocalDate localDate, Locale locale) {
        return localDate.format(FORMATTER);
    }
}
