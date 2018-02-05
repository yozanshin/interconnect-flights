package org.emf.services.interconnections;

import org.emf.model.interconnections.AirportsRoutes;
import org.emf.model.routes.Route;
import org.emf.services.interconnections.RoutesCalculatorService;
import org.emf.services.routes.RoutesService;
import org.emf.testutils.RoutesMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoutesCalculatorServiceTest {

    @MockBean
    private RoutesService routesService;

    @Autowired
    private RoutesCalculatorService routesCalculatorService;

    @Test
    public void calculateValidRoutes() throws Exception{

        Mockito.when(routesService.getRoutes()).thenAnswer(new RoutesMock());

        AirportsRoutes airportsRoutes = this.routesCalculatorService.calculateValidRoutes("DUB", "WRO");

        assertNotNull(airportsRoutes);
        assertTrue(airportsRoutes.isDirectlyConnectable());
        assertTrue(airportsRoutes.getConnectionAirports().contains("STN"));
    }
}
