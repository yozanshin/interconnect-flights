package org.emf.model.interconnections;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Interconnection {

    private int stops;
    private List<Leg> legs;

    public void addLeg(Leg leg){
        if(this.legs == null){
            this.legs = new ArrayList<>();
        }
        this.legs.add(leg);
    }
}
