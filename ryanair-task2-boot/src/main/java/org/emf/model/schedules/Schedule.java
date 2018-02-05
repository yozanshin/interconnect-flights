package org.emf.model.schedules;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Schedule {

    private int month;
    private List<Day> days;
}
