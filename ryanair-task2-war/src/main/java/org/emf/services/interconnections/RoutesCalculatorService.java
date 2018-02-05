package org.emf.services.interconnections;

import org.emf.model.interconnections.AirportsRoutes;

public interface RoutesCalculatorService {

    public AirportsRoutes calculateValidRoutes(String departure, String arrival);
}
