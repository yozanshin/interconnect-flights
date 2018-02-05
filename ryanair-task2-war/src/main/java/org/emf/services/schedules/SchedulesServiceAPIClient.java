package org.emf.services.schedules;

import lombok.extern.slf4j.Slf4j;
import org.emf.exceptions.InterconnectionsInternalException;
import org.emf.model.schedules.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SchedulesServiceAPIClient implements SchedulesService{

    private final RestTemplate restTemplate;

    @Value("${ryanair.apis.schedules.url}")
    private String apiUrl;

    @Autowired
    public SchedulesServiceAPIClient(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Gets all flights between departure and arrival in the givn year and month by requesting RyanAir API
     *
     * @param departure Departure airport code
     * @param arrival Arrival airport code
     * @param year Requested year
     * @param month Requested month
     *
     * @return Schedule with all flights per day
     *
     * @throws InterconnectionsInternalException
     */
    @Override
    public Schedule getSchedule(String departure, String arrival, int year, int month) throws InterconnectionsInternalException {

        log.info("Getting schedules from {} to {} for {}/{} from RyanAir Schedules API ...",
                departure, arrival, year, month);

        Map<String, Object> params = new HashMap<>();
        params.put("departure", departure);
        params.put("arrival", arrival);
        params.put("year", year);
        params.put("month", month);

        try {

            ResponseEntity<Schedule> response = this.restTemplate.getForEntity(this.apiUrl, Schedule.class, params);

            Schedule schedule = response.getBody();

            log.info("Schedules got");

            return schedule;

        }catch (HttpClientErrorException ex){

            // RyanAir API throws a 404 Http Status Code if there aren't flights with the given parameters
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND){

                log.info("No schedules found with requested params");
                return null;
            }

            log.error("Unexpected error getting schedules from RyanAir API", ex);
            throw new InterconnectionsInternalException("Unable to get schedules due to integration error");
        }
    }
}
