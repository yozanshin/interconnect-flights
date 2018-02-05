package org.emf.controllers;

import lombok.extern.slf4j.Slf4j;
import org.emf.exceptions.InterconnectionsNofFoundException;
import org.emf.exceptions.InterconnectionsValidationException;
import org.emf.model.interconnections.AirportsRoutes;
import org.emf.model.interconnections.Interconnection;
import org.emf.services.interconnections.InterconnectionsCalculatorService;
import org.emf.services.utils.InterconnectsDateFormatter;
import org.emf.services.interconnections.RoutesCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
public class InterconnectsController {

    private final InterconnectsDateFormatter dateFormatter;
    private final RoutesCalculatorService routesCalculatorService;
    private final InterconnectionsCalculatorService interconnectionsCalculatorService;

    @Autowired
    public InterconnectsController(
            InterconnectsDateFormatter dateFormatter,
            RoutesCalculatorService routesCalculatorService,
            InterconnectionsCalculatorService interconnectionsCalculatorService) {

        this.dateFormatter = dateFormatter;
        this.routesCalculatorService = routesCalculatorService;
        this.interconnectionsCalculatorService = interconnectionsCalculatorService;
    }

    /**
     * Gets all interconnections from departure to arrival between departure date time and arrival date time
     *
     * @param departure Departure airport code
     * @param arrival Arrival airport code
     * @param departureDateTimeString Departure date time limit
     * @param arrivalDateTimeString Arrival date time limit
     *
     * @return List of {@link Interconnection}
     */
    @RequestMapping(value = "/interconnections", method = RequestMethod.GET, produces = "application/json")
    public List<Interconnection> interconnections(@RequestParam("departure") String departure,
                                                  @RequestParam("arrival") String arrival,
                                                  @RequestParam("departureDateTime") String departureDateTimeString,
                                                  @RequestParam("arrivalDateTime") String arrivalDateTimeString) {

        log.info("Getting interconnections with parameters:[{},{},{},{}] ...",
                departure, arrival, departureDateTimeString, arrivalDateTimeString);

        this.validateRequestParameters(departure, arrival, departureDateTimeString, arrivalDateTimeString);

        LocalDateTime departureDateTime = dateFormatter.parseDateTime(departureDateTimeString);
        LocalDateTime arrivalDateTime = dateFormatter.parseDateTime(arrivalDateTimeString);

        List<Interconnection> interconnections = new ArrayList<>();

        AirportsRoutes airportsRoutes = routesCalculatorService.calculateValidRoutes(departure, arrival);

        if (!airportsRoutes.isDirectlyConnectable() && airportsRoutes.getConnectionAirports().isEmpty()) {

            throw new InterconnectionsNofFoundException("Interconnections not found with the given parameters");
        }

        if (airportsRoutes.isDirectlyConnectable()) {

            interconnections.addAll(interconnectionsCalculatorService.getDirectFlights(
                    departure, arrival, departureDateTime, arrivalDateTime));
        }

        for (String connection : airportsRoutes.getConnectionAirports()) {

            interconnections.addAll(interconnectionsCalculatorService.getConnectedFlights
                    (departure, arrival, connection, departureDateTime, arrivalDateTime));
        }

        log.info("{} interconnections found", interconnections.size());

        if(interconnections.isEmpty()){
            throw new InterconnectionsNofFoundException("Interconnections not found with the given parameters");
        }

        return interconnections;
    }

    /**
     * Validates the given request paameters
     *
     * @param departure 'departure' request parameter
     * @param arrival 'arrival' request parameter
     * @param departureDateTime 'departureDateTime' request parameter
     * @param arrivalDateTime 'arrivalDateTime' request parameter
     */
    private void validateRequestParameters(String departure, String arrival, String departureDateTime, String arrivalDateTime) {

        log.info("Validating request ...");

        if (StringUtils.isEmpty(departure)) {
            log.error("Empty 'departure' param");
            throw new InterconnectionsValidationException("'departure' can not be empty");
        }
        if (StringUtils.isEmpty(arrival)) {
            log.error("Empty 'arrival' param");
            throw new InterconnectionsValidationException("'arrival' can not be empty");
        }
        if (StringUtils.isEmpty(departureDateTime)) {
            log.error("Empty 'departureDateTime' param");
            throw new InterconnectionsValidationException("'departureDateTime' can not be empty");
        }
        try {
            dateFormatter.parseDateTime(departureDateTime);
        } catch (Exception ex) {
            log.error("Invalid 'departureDateTime' param date time format");
            throw new InterconnectionsValidationException("'departureDateTime' invalid date time format");
        }
        if (StringUtils.isEmpty(arrivalDateTime)) {
            log.error("Empty 'arrivalDateTime' param");
            throw new InterconnectionsValidationException("'arrivalDateTime' can not be empty");
        }
        try {
            dateFormatter.parseDateTime(arrivalDateTime);
        } catch (Exception ex) {
            log.error("Invalid 'arrivalDateTime' param date time format");
            throw new InterconnectionsValidationException("'arrivalDateTime' invalid date time format");
        }

        log.info("Request successfully validated");
    }
}
