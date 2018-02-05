package org.emf.testutils;

import org.emf.model.routes.Route;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

public class RoutesMock implements Answer<List<Route>> {

    @Override
    public List<Route> answer(InvocationOnMock invocationOnMock) throws Throwable {

        List<Route> routes = new ArrayList<>();

        Route route1 = new Route();
        route1.setAirportFrom("DUB");
        route1.setAirportTo("WRO");
        route1.setConnectingAirport(null);
        routes.add(route1);

        Route route2 = new Route();
        route2.setAirportFrom("DUB");
        route2.setAirportTo("STN");
        route2.setConnectingAirport(null);
        routes.add(route2);

        Route route3 = new Route();
        route3.setAirportFrom("STN");
        route3.setAirportTo("WRO");
        route3.setConnectingAirport(null);
        routes.add(route3);

        Route route4 = new Route();
        route4.setAirportFrom("LUZ");
        route4.setAirportTo("STN");
        route4.setConnectingAirport(null);
        routes.add(route4);

        Route route5 = new Route();
        route5.setAirportFrom("CHQ");
        route5.setAirportTo("SKG");
        route5.setConnectingAirport(null);
        routes.add(route5);

        return routes;
    }
}
