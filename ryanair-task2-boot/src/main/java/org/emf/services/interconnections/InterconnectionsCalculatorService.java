package org.emf.services.interconnections;

import org.emf.exceptions.InterconnectionsInternalException;
import org.emf.model.interconnections.Interconnection;

import java.time.LocalDateTime;
import java.util.List;

public interface InterconnectionsCalculatorService {

    public List<Interconnection> getDirectFlights
            (String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime)
            throws InterconnectionsInternalException;

    public List<Interconnection> getConnectedFlights
            (String departure, String arrival, String connection, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime)
            throws InterconnectionsInternalException;
}
