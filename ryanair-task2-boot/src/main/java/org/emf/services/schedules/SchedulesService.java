package org.emf.services.schedules;

import org.emf.exceptions.InterconnectionsInternalException;
import org.emf.model.schedules.Schedule;

public interface SchedulesService {

    public Schedule getSchedule(String departure, String arrival, int year, int month) throws InterconnectionsInternalException;
}
