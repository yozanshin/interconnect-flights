package org.emf.services.interconnections;

import lombok.extern.slf4j.Slf4j;
import org.emf.model.interconnections.AirportsRoutes;
import org.emf.model.routes.Route;
import org.emf.services.routes.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoutesCalculatorServiceImpl implements RoutesCalculatorService {

    private final RoutesService routesService;

    @Autowired
    public RoutesCalculatorServiceImpl(RoutesService routesService) {
        this.routesService = routesService;
    }

    /**
     * Gets routes info between departure and arrival airports
     *
     * @param departure Departure airport code
     * @param arrival Arrival airport code
     *
     * @return AirportsRoutes with all routes info between the given airports
     */
    @Override
    public AirportsRoutes calculateValidRoutes(String departure, String arrival) {

        AirportsRoutes airportsRoutes = new AirportsRoutes();

        List<Route> routes = this.routesService.getRoutes();

        // Checks if exists a direct route between departure and arrival airports
        airportsRoutes.setDirectlyConnectable(routes.stream().filter(route -> (departure.equals(route.getAirportFrom()) && arrival.equals(route.getAirportTo())))
                .findAny().isPresent());

        // Gets routes from departure airport (but not to arrival airport)
        Set<String> fromDeparture = routes.stream()
                .filter(route -> departure.equals(route.getAirportFrom()) && !arrival.equals(route.getAirportTo()))
                .map(route -> {
                    return route.getAirportTo();
                })
                .collect(Collectors.toSet());

        // Gets routes to arrival airport (but not from departure airport)
        Set<String> toArrival = routes.stream()
                .filter(route -> arrival.equals(route.getAirportTo()) && !departure.equals(route.getAirportFrom()))
                .map(route -> {
                    return route.getAirportFrom();
                })
                .collect(Collectors.toSet());

        // Gets the intersection set between airports in routes from departure airport and routes to arrival airport
        Set<String> validStops = new HashSet<>(fromDeparture);
        validStops.retainAll(toArrival);

        airportsRoutes.setConnectionAirports(validStops);

        return airportsRoutes;
    }
}
