package org.emf.services.routes;

import lombok.extern.slf4j.Slf4j;
import org.emf.exceptions.InterconnectionsInternalException;
import org.emf.model.routes.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class RoutesServiceAPIClient implements RoutesService {

    private final RestTemplate restTemplate;

    @Value("${ryanair.apis.routes.url}")
    private String apiUrl;

    @Autowired
    public RoutesServiceAPIClient(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Gets all routes by requesting RyanAir API
     *
     * @return List of {@link Route}
     */
    @Override
    public List<Route> getRoutes() {

        log.info("Getting routes from RyanAir Routes API ...");

        try {

            ResponseEntity<Route[]> response = this.restTemplate.getForEntity(this.apiUrl, Route[].class);

            List<Route> routes = Arrays.asList(response.getBody());

            log.info("{} routes got", routes.size());

            return routes;

        }catch (HttpClientErrorException ex){

            log.error("Unexpected error getting routes from RyanAir API", ex);
            throw new InterconnectionsInternalException("Unable to get routes due to integration error");
        }
    }
}
