package org.emf.testutils;

import org.emf.model.schedules.Day;
import org.emf.model.schedules.Flight;
import org.emf.model.schedules.Schedule;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

public class ScheduleMock implements Answer<Schedule> {

    @Override
    public Schedule answer(InvocationOnMock invocationOnMock) throws Throwable {

        int month = invocationOnMock.getArgumentAt(3, Integer.class);

        Schedule schedule = new Schedule();

        schedule.setMonth(month);

        List<Day> days = new ArrayList<>();
        for(int dayNumber=1; dayNumber<=30; dayNumber++) {

            Day day = new Day();
            day.setDay(dayNumber);

            List<Flight> flights = new ArrayList<>();

            Flight flight1 = new Flight();
            flight1.setDepartureTime("07:00");
            flight1.setArrivalTime("09:30");
            flights.add(flight1);

            Flight flight2 = new Flight();
            flight2.setDepartureTime("13:00");
            flight2.setArrivalTime("15:30");
            flights.add(flight2);

            Flight flight3 = new Flight();
            flight3.setDepartureTime("20:00");
            flight3.setArrivalTime("22:30");
            flights.add(flight3);

            day.setFlights(flights);
            days.add(day);
        }

        schedule.setDays(days);

        return schedule;
    }
}
