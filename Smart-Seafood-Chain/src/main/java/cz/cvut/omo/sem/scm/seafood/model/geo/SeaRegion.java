package cz.cvut.omo.sem.scm.seafood.model.geo;

import cz.cvut.omo.sem.scm.seafood.type.domain.Country;
import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SeaRegion {
    private String name;
    private double seasonMultiplier;
    private Country country;
    private List<SeafoodType> seaFoodList = new ArrayList<>();
}