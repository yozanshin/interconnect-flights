package org.emf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
//public class InterconnectFlightsApp  extends SpringBootServletInitializer{
public class InterconnectFlightsApp{

    public static void main(String[] args) {

        SpringApplication.run(InterconnectFlightsApp.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//
//        return builder.sources(InterconnectFlightsApp.class);
//    }
}
