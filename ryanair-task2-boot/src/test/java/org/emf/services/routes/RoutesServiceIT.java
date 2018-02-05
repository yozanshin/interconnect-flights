package org.emf.services.routes;

import org.emf.InterconnectFlightsApp;
import org.emf.model.routes.Route;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * RyanAir Routes API integration test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterconnectFlightsApp.class)
public class RoutesServiceIT {

    @Autowired
    private RoutesService routesService;

    @Test
    public void getRoutes() throws Exception{

        List<Route> routes  = routesService.getRoutes();
        assertFalse(routes.isEmpty());
    }
}
