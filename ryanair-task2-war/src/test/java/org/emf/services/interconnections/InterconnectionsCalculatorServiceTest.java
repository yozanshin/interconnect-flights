package org.emf.services.interconnections;

import org.emf.model.interconnections.Interconnection;
import org.emf.model.schedules.Day;
import org.emf.model.schedules.Flight;
import org.emf.model.schedules.Schedule;
import org.emf.services.schedules.SchedulesService;
import org.emf.services.utils.InterconnectsDateFormatter;
import org.emf.testutils.ScheduleMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterconnectionsCalculatorServiceTest {

    @MockBean
    private SchedulesService schedulesService;

    @Autowired
    private InterconnectsDateFormatter dateFormatter;

    @Autowired
    private InterconnectionsCalculatorService interconnectionsCalculatorService;

    @Before
    public void setUp(){

        Mockito.when(schedulesService.getSchedule(anyString(), anyString(), anyInt(), anyInt())).
                thenAnswer(new ScheduleMock());
    }

    @Test
    public void getDirectFlights() throws Exception{

        String departureAirport = "DUB";
        String arrivalAirport = "WRO";
        LocalDateTime departureDateTime = LocalDateTime.of(2018, 3, 1, 7, 0);
        LocalDateTime arrivalDateTime = LocalDateTime.of(2018, 3, 3, 21, 0);

        List<Interconnection> interconnections = interconnectionsCalculatorService.getDirectFlights(
                departureAirport,
                arrivalAirport,
                departureDateTime,
                arrivalDateTime);

        assertNotNull(interconnections);
        if (!interconnections.isEmpty()){

            interconnections.stream().forEach(interconnection -> {

                assertTrue(interconnection.getStops() == 0);
                assertTrue(interconnection.getLegs().size() == 1);
                assertTrue(departureAirport.equals(interconnection.getLegs().get(0).getDepartureAirport()));
                assertTrue(departureDateTime.compareTo(dateFormatter.parseDateTime(interconnection.getLegs().get(0).getDepartureDateTime())) <= 0);
                assertTrue(arrivalAirport.equals(interconnection.getLegs().get(0).getArrivalAirport()));
                assertTrue(arrivalDateTime.compareTo(dateFormatter.parseDateTime(interconnection.getLegs().get(0).getArrivalDateTime())) >= 0);
            });
        }

    }

    @Test
    public void getConnectedFlights() throws Exception{

        String departureAirport = "DUB";
        String arrivalAirport = "WRO";
        String connectionAirport = "STN";
        LocalDateTime departureDateTime = LocalDateTime.of(2018, 3, 1, 7, 0);
        LocalDateTime arrivalDateTime = LocalDateTime.of(2018, 3, 3, 21, 0);

        List<Interconnection> interconnections = interconnectionsCalculatorService.getConnectedFlights(
                departureAirport,
                arrivalAirport,
                connectionAirport,
                departureDateTime,
                arrivalDateTime);

        assertNotNull(interconnections);
        if (!interconnections.isEmpty()){

            interconnections.stream().forEach(interconnection -> {

                assertTrue(interconnection.getStops() == 1);
                assertTrue(interconnection.getLegs().size() == 2);

                assertTrue(departureAirport.equals(interconnection.getLegs().get(0).getDepartureAirport()));
                assertTrue(departureDateTime.compareTo(dateFormatter.parseDateTime(interconnection.getLegs().get(0).getDepartureDateTime())) <= 0);
                assertTrue(connectionAirport.equals(interconnection.getLegs().get(0).getArrivalAirport()));
                assertTrue(arrivalDateTime.compareTo(dateFormatter.parseDateTime(interconnection.getLegs().get(0).getArrivalDateTime())) >= 0);

                assertTrue(connectionAirport.equals(interconnection.getLegs().get(1).getDepartureAirport()));
                assertTrue((dateFormatter.parseDateTime(interconnection.getLegs().get(0).getArrivalDateTime()).plusHours(2L).compareTo(dateFormatter.parseDateTime(interconnection.getLegs().get(1).getDepartureDateTime())) <= 0));
                assertTrue(arrivalAirport.equals(interconnection.getLegs().get(1).getArrivalAirport()));
                assertTrue(arrivalDateTime.compareTo(dateFormatter.parseDateTime(interconnection.getLegs().get(1).getArrivalDateTime())) >= 0);
            });
        }
    }
}