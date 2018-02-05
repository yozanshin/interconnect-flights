package org.emf.model.schedules;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Day {

    private int day;
    private List<Flight> flights;
}
