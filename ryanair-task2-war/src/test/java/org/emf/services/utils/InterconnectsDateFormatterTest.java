package org.emf.services.utils;

import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.*;

public class InterconnectsDateFormatterTest {

    @Test
    public void parseStringDateTime() {

        String stringDateTime = "2018-03-01T21:00:00";

        InterconnectsDateFormatter dateFormatter = new InterconnectsDateFormatter();
        LocalDateTime dateTime = dateFormatter.parseDateTime(stringDateTime);
        assertTrue(dateTime.getYear() == 2018);
        assertTrue(dateTime.getMonth().getValue() == 3);
        assertTrue(dateTime.getDayOfMonth() == 1);
        assertTrue(dateTime.getHour() == 21);
        assertTrue(dateTime.getMinute() == 0);
        assertTrue(dateTime.getSecond() ==0);
    }

    @Test(expected = DateTimeParseException.class)
    public void parseBadStringDateTime() {

        String stringDateTime = Mockito.anyString();

        InterconnectsDateFormatter dateFormatter = new InterconnectsDateFormatter();
        dateFormatter.parseDateTime(stringDateTime);
    }

    @Test
    public void parseLocalDateTime1() {

        LocalDateTime dateTime = LocalDateTime.of(2018, 3, 1, 21, 0, 0);

        InterconnectsDateFormatter dateFormatter = new InterconnectsDateFormatter();
        String stringDateTime = dateFormatter.formatDateTime(dateTime);
        assertTrue("2018-03-01T21:00:00".equals(stringDateTime));

    }
}