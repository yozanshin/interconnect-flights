package org.emf.model.schedules;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Flight {

    private String number;
    private String departureTime;
    private String arrivalTime;
}
