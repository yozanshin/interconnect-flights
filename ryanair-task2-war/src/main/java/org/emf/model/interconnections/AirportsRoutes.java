package org.emf.model.interconnections;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class AirportsRoutes {

    private boolean directlyConnectable;
    private Set<String> connectionAirports;
}
