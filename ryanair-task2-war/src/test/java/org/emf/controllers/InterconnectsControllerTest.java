package org.emf.controllers;

import org.emf.exceptions.InterconnectionsValidationException;
import org.emf.model.interconnections.Interconnection;
import org.emf.services.routes.RoutesService;
import org.emf.services.schedules.SchedulesService;
import org.emf.testutils.RoutesMock;
import org.emf.testutils.ScheduleMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterconnectsControllerTest {

    @MockBean
    private RoutesService routesService;

    @MockBean
    private SchedulesService schedulesService;

    @Autowired
    private InterconnectsController interconnectsController;

    @Test
    public void interconnections() {

        Mockito.when(routesService.getRoutes()).thenAnswer(new RoutesMock());
        Mockito.when(schedulesService.getSchedule(anyString(), anyString(), anyInt(), anyInt()))
                .thenAnswer(new ScheduleMock());

        List<Interconnection> interconnections =
                interconnectsController.interconnections("DUB", "WRO", "2018-03-01T07:00:00", "2018-03-01T22:00:00");

        assertNotNull(interconnections);
        assertFalse(interconnections.isEmpty());
    }

    @Test
    public void interconnectionsBadRequest(){

        try{
            interconnectsController.interconnections(null, "WRO", "2018-03-01T07:00:00", "2018-03-01T22:00:00");
            fail();
        }catch (InterconnectionsValidationException ex){
            assertTrue(ex.getMessage().contains("'departure' can not be empty"));
        }
        try{
            interconnectsController.interconnections("", "WRO", "2018-03-01T07:00:00", "2018-03-01T22:00:00");
            fail();
        }catch (InterconnectionsValidationException ex){
            assertTrue(ex.getMessage().contains("'departure' can not be empty"));
        }
        try{
            interconnectsController.interconnections("DUB", null, "2018-03-01T07:00:00", "2018-03-01T22:00:00");
            fail();
        }catch (InterconnectionsValidationException ex){
            assertTrue(ex.getMessage().contains("'arrival' can not be empty"));
        }
        try{
            interconnectsController.interconnections("DUB", "", "2018-03-01T07:00:00", "2018-03-01T22:00:00");
            fail();
        }catch (InterconnectionsValidationException ex){
            assertTrue(ex.getMessage().contains("'arrival' can not be empty"));
        }
        try{
            interconnectsController.interconnections("DUB", "WRO", null, "2018-03-01T22:00:00");
            fail();
        }catch (InterconnectionsValidationException ex){
            assertTrue(ex.getMessage().contains("'departureDateTime' can not be empty"));
        }
        try{
            interconnectsController.interconnections("DUB", "WRO", "", "2018-03-01T22:00:00");
            fail();
        }catch (InterconnectionsValidationException ex){
            assertTrue(ex.getMessage().contains("'departureDateTime' can not be empty"));
        }
        try{
            interconnectsController.interconnections("DUB", "WRO", "bad_format_datetime", "2018-03-01T22:00:00");
            fail();
        }catch (InterconnectionsValidationException ex){
            assertTrue(ex.getMessage().contains("'departureDateTime' invalid date time format"));
        }
        try{
            interconnectsController.interconnections("DUB", "WRO", "2018-03-01T07:00:00", "");
            fail();
        }catch (InterconnectionsValidationException ex){
            assertTrue(ex.getMessage().contains("'arrivalDateTime' can not be empty"));
        }
        try{
            interconnectsController.interconnections("DUB", "WRO", "2018-03-01T07:00:00", null);
            fail();
        }catch (InterconnectionsValidationException ex){
            assertTrue(ex.getMessage().contains("'arrivalDateTime' can not be empty"));
        }
        try{
            interconnectsController.interconnections("DUB", "WRO", "2018-03-01T07:00:00", "bad_format_datetime");
            fail();
        }catch (InterconnectionsValidationException ex){
            assertTrue(ex.getMessage().contains("'arrivalDateTime' invalid date time format"));
        }
    }
}