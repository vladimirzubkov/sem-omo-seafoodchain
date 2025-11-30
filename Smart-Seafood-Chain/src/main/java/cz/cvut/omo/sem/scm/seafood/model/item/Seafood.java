package cz.cvut.omo.sem.scm.seafood.model.item;

import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Seafood extends Item {
    private SeafoodType seafoodType;
    private boolean isLive;

    public Seafood(String id, SeafoodType seafoodType, double weightKg, boolean isLive) {
        this.setItemId(id);
        this.seafoodType = seafoodType;
        this.setWeightKg(weightKg);
        this.isLive = isLive;
        this.setName(seafoodType.name());
    }
}