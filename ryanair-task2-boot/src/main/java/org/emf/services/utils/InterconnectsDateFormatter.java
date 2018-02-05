package org.emf.services.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Application date formatter
 */
@Component
public class InterconnectsDateFormatter {

    private final DateTimeFormatter dateTimeFormatter;

    public InterconnectsDateFormatter(){

        // Sets the application date format to ISO format
        this.dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }

    /**
     * Parses the given string to LocalDateTime with the application date format
     *
     * @param dateTime String date time
     *
     * @return LocalDateTime parsed string
     */
    public LocalDateTime parseDateTime(String dateTime){

        return LocalDateTime.parse(dateTime, this.dateTimeFormatter);
    }

    /**
     * Parses the given LocalDateTime to String with the application date format
     *
     * @param dateTime LocalDateTime date time
     * @return String formatted LocalDateTime
     */
    public String formatDateTime(LocalDateTime dateTime){

        return dateTime.format(this.dateTimeFormatter);
    }
}
