package cz.cvut.omo.sem.scm.seafood.model.item;

import cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Material extends Item {

    private MaterialType materialType;

    public Material(String id, MaterialType materialType, double weightKg) {
        this.setItemId(id);
        this.materialType = materialType;
        this.setName(materialType.getPrettyName());
        this.setWeightKg(weightKg);
        this.setRequiredStorage(StorageTemperature.ROOM_TEMP);
    }
}