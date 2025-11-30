package cz.cvut.omo.sem.scm.seafood.model.item;

import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Seafood extends Item {
    private SeafoodType seafoodType;
    private double weightKg;
    private boolean isLive;

    public Seafood(String id, SeafoodType seafoodType, double weightKg, boolean isLive) {
        setItemId(id);
        this.seafoodType = seafoodType;
        this.weightKg = weightKg;
        this.isLive = isLive;
    }
}