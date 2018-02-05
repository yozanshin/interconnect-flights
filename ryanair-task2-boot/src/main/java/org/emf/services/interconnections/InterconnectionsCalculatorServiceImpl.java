package org.emf.services.interconnections;

import lombok.extern.slf4j.Slf4j;
import org.emf.exceptions.InterconnectionsInternalException;
import org.emf.model.interconnections.Interconnection;
import org.emf.model.interconnections.Leg;
import org.emf.model.schedules.Flight;
import org.emf.model.schedules.Schedule;
import org.emf.services.schedules.SchedulesService;
import org.emf.services.utils.InterconnectsDateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InterconnectionsCalculatorServiceImpl implements InterconnectionsCalculatorService {

    private final SchedulesService schedulesService;
    private final InterconnectsDateFormatter dateFormatter;

    @Autowired
    public InterconnectionsCalculatorServiceImpl(SchedulesService schedulesService, InterconnectsDateFormatter dateFormatter) {
        this.schedulesService = schedulesService;
        this.dateFormatter = dateFormatter;
    }

    /**
     * Gets all direct flights from departure and arrival between the given dates
     *
     * @param departure Departure airport code
     * @param arrival Arrival airport code
     * @param departureDateTime Departure date time limit
     * @param arrivalDateTime Arrival date time limit
     *
     * @return A list of {@link Interconnection} with flights info
     *
     * @throws InterconnectionsInternalException
     */
    @Override
    public List<Interconnection> getDirectFlights(String departure,
                                                  String arrival,
                                                  LocalDateTime departureDateTime,
                                                  LocalDateTime arrivalDateTime) throws InterconnectionsInternalException {

        long startTime = System.currentTimeMillis();

        log.info("Getting direct flights from {} to {} between {} and {} ...", departure, arrival, departureDateTime, arrivalDateTime);

        List<Interconnection> interconnections = new ArrayList<>();

        LocalDateTime scheduleDateTime = departureDateTime;

        // Iterate between departureDateTime and arrivalDateTime by months to get all required schedules
        while (scheduleDateTime.getYear() < arrivalDateTime.getYear() ||
                (scheduleDateTime.getYear() == arrivalDateTime.getYear() &&
                        scheduleDateTime.getMonthValue() <= arrivalDateTime.getMonthValue())) {

            // Gets all flights between departure and arrival in the given iteration
            Schedule schedule = schedulesService.getSchedule(
                    departure, arrival, scheduleDateTime.getYear(), scheduleDateTime.getMonth().getValue());

            // If there are flights with the given parameters, gets flights info as Interconnection
            if(schedule != null) {
                schedule.getDays().stream()
                        .filter(day -> day.getDay() >= departureDateTime.getDayOfMonth() && day.getDay() <= arrivalDateTime.getDayOfMonth())
                        .forEach(day -> {

                            for (Flight flight : day.getFlights()) {

                                LocalDateTime flightDepartureDateTime = this.getLocalDateTimeFromFlightInfo(
                                        flight.getDepartureTime(), departureDateTime.getYear(), schedule.getMonth(), day.getDay());

                                LocalDateTime flightArrivalDateTime = this.getLocalDateTimeFromFlightInfo(
                                        flight.getArrivalTime(), departureDateTime.getYear(), schedule.getMonth(), day.getDay());

                                if (flightDepartureDateTime.compareTo(departureDateTime) >= 0 && flightArrivalDateTime.compareTo(arrivalDateTime) <= 0) {

                                    Leg leg = this.getLeg(
                                            departure,
                                            arrival,
                                            flightDepartureDateTime,
                                            flightArrivalDateTime);

                                    interconnections.add(this.getInterconnection(new Leg[]{leg}));
                                }
                            }
                        });

            }

            scheduleDateTime = scheduleDateTime.plusMonths(1L);
        }

        log.info("{} flights found in {} miliseconds", interconnections.size(), System.currentTimeMillis() - startTime);

        return interconnections;
    }

    @Override
    public List<Interconnection> getConnectedFlights(String departure,
                                                     String arrival,
                                                     String connection,
                                                     LocalDateTime departureDateTime,
                                                     LocalDateTime arrivalDateTime) throws InterconnectionsInternalException {

        long startTime = System.currentTimeMillis();

        log.info("Getting flights from {} to {} interconnected by {} between {} and {} ...",
                departure, arrival, connection, departureDateTime, arrivalDateTime);

        List<Interconnection> interconnections = new ArrayList<>();

        List<Leg> possibleFirstLegs = new ArrayList<>();

        LocalDateTime scheduleDateTime = departureDateTime;

        // Iterate between departureDateTime and arrivalDateTime by months to get all required schedules
        while (scheduleDateTime.getYear() < arrivalDateTime.getYear() ||
                (scheduleDateTime.getYear() == arrivalDateTime.getYear() &&
                        scheduleDateTime.getMonthValue() <= arrivalDateTime.getMonthValue())) {

            log.info("Getting flights from {} to {} between {} and {}", departure, connection, departureDateTime, arrivalDateTime);

            // Gets all flights between departure and arrival in the given iteration
            Schedule schedule = schedulesService.getSchedule(
                    departure, connection, scheduleDateTime.getYear(), scheduleDateTime.getMonth().getValue());

            // If there are flights with the given parameters, gets flights info as possible first legs for Interconnections
            if(schedule != null) {

                possibleFirstLegs.addAll(
                        this.getPossibleFirstLegsFromSchedule(schedule, departure, connection, departureDateTime, arrivalDateTime));

            }

            scheduleDateTime = scheduleDateTime.plusMonths(1L);
        }

        log.info("{} possible interconnection flights found", possibleFirstLegs.size());

        /*
         After calculating all possible first legs for connections,
         for each possible first leg gets all connections with arrival airport
         at least two hours later than first leg flight arrival
         and not later than the arrival date time and sets an Interconnection
          */
        for(Leg firstLeg: possibleFirstLegs) {

            // Gets second leg departure time limit from first leg flight arrival date time plus 2 hours
            LocalDateTime connectionDateTime = dateFormatter.parseDateTime(firstLeg.getArrivalDateTime()).plusHours(2L);

            log.info("Getting flights from {} to {} between {} and {} ...",
                    connection, arrival, connectionDateTime, arrivalDateTime);

            scheduleDateTime = connectionDateTime;

            // Iterate between departureDateTime and arrivalDateTime by months to get all required schedules
            while (scheduleDateTime.getYear() < arrivalDateTime.getYear() ||
                    (scheduleDateTime.getYear() == arrivalDateTime.getYear() &&
                    scheduleDateTime.getMonthValue() <= arrivalDateTime.getMonthValue())) {

                // Gets all flights between departure and arrival in the given iteration
                Schedule schedule =
                        schedulesService.getSchedule(
                                firstLeg.getArrivalAirport(),
                                arrival,
                                scheduleDateTime.getYear(),
                                scheduleDateTime.getMonth().getValue());

                // If there are flights with the given parameters, gets flights info as Interconnection
                if(schedule != null) {

                    schedule.getDays().stream()
                            .filter(day -> day.getDay() >= connectionDateTime.getDayOfMonth() &&
                                    day.getDay() <= arrivalDateTime.getDayOfMonth())
                            .forEach(day -> {

                                for (Flight flight : day.getFlights()) {

                                    LocalDateTime flightDepartureDateTime = this.getLocalDateTimeFromFlightInfo(
                                            flight.getDepartureTime(), departureDateTime.getYear(), schedule.getMonth(), day.getDay());

                                    LocalDateTime flightArrivalDateTime = this.getLocalDateTimeFromFlightInfo(
                                            flight.getArrivalTime(), departureDateTime.getYear(), schedule.getMonth(), day.getDay());

                                    if (flightDepartureDateTime.compareTo(connectionDateTime) >= 0 &&
                                            flightArrivalDateTime.compareTo(arrivalDateTime) <= 0) {

                                        Leg secondLeg = this.getLeg(
                                                connection,
                                                arrival,
                                                flightDepartureDateTime,
                                                flightArrivalDateTime);

                                        interconnections.add(this.getInterconnection(new Leg[]{firstLeg, secondLeg}));
                                    }
                                }
                            });
                }

                scheduleDateTime = scheduleDateTime.plusMonths(1L);
            };
        }

        log.info("{} interconnected flights found in {} miliseconds", interconnections.size(), System.currentTimeMillis() - startTime);

        return interconnections;
    }

    /**
     * Gets LocalDateTime from flight time info and date info
     *
     * @param stringFlightTime Flight time info
     * @param year Year number
     * @param month Month number
     * @param day Day of month number
     *
     * @return Parsed {@link LocalDateTime}
     */
    private LocalDateTime getLocalDateTimeFromFlightInfo(String stringFlightTime, int year, int month, int day){

        String[] flightTime = stringFlightTime.split(":");

        return LocalDateTime.of(year, month, day, Integer.valueOf(flightTime[0]), Integer.valueOf(flightTime[1]));

    }

    /**
     * Gets {@link Leg} from departure airport code and date time and arrival airport code and date time
     *
     * @param departure Departure airport code
     * @param arrival Arrival airport code
     * @param departureDateTime Departure date time
     * @param arrivalDateTime Arrival date time
     * @return
     */
    private Leg getLeg(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime){

        Leg leg = new Leg();
        leg.setDepartureAirport(departure);
        leg.setArrivalAirport(arrival);
        leg.setDepartureDateTime(dateFormatter.formatDateTime(departureDateTime));
        leg.setArrivalDateTime(dateFormatter.formatDateTime(arrivalDateTime));

        return leg;
    }

    /**
     * Gets {@link Interconnection} from the given {@link Leg} array
     *
     * @param legs {@link Leg} array of flights info
     *
     * @return Interconnection with the given flights info
     */
    private Interconnection getInterconnection(Leg[] legs){

        Interconnection interconnection = new Interconnection();
        interconnection.setStops(legs.length - 1);
        for(Leg leg:legs){
            interconnection.addLeg(leg);
        }

        return interconnection;
    }

    /**
     * Gets possible first legs for connections from {@link Schedule} with the given parameters
     *
     * @param schedule {@link Schedule} with flights info
     * @param departure Departure airport code
     * @param connection Connection airport code
     * @param departureDateTime Departure date time
     * @param arrivalDateTime Arrival date time
     *
     * @return A {@link List} of {@link Leg} with possible first legs info
     */
    private List<Leg> getPossibleFirstLegsFromSchedule(Schedule schedule, String departure, String connection, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime){

        List<Leg> legs = new ArrayList<>();

        schedule.getDays().stream()
                .filter(day -> day.getDay() >= departureDateTime.getDayOfMonth() &&
                        day.getDay() <= arrivalDateTime.getDayOfMonth())
                .forEach(day -> {

                    for (Flight flight : day.getFlights()) {

                        LocalDateTime flightDepartureDateTime = this.getLocalDateTimeFromFlightInfo(
                                flight.getDepartureTime(), departureDateTime.getYear(), schedule.getMonth(), day.getDay());

                        LocalDateTime flightArrivalDateTime = this.getLocalDateTimeFromFlightInfo(
                                flight.getArrivalTime(), departureDateTime.getYear(), schedule.getMonth(), day.getDay());

                        /*
                         Checks if the flight departure and arrival is between the given departure and arrival date time limits
                         and a connection could be established two hours later
                          */
                        if (flightDepartureDateTime.compareTo(departureDateTime) >= 0 &&
                                flightArrivalDateTime.compareTo(arrivalDateTime) <= 0 &&
                                flightArrivalDateTime.plusHours(2L).compareTo(arrivalDateTime) <= 0) {

                            Leg leg = this.getLeg(
                                    departure,
                                    connection,
                                    flightDepartureDateTime,
                                    flightArrivalDateTime);

                            legs.add(leg);
                        }
                    }
                });

        return legs;
    }
}
