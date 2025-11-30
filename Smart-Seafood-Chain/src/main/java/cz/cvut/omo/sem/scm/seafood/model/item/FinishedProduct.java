package cz.cvut.omo.sem.scm.seafood.model.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FinishedProduct extends Item {

    public FinishedProduct(String id, String recipeName, double weightKg) {
        this.setItemId(id);
        this.setName(recipeName); // Set name based on the recipe (e.g., "Sushi Box")
        this.setWeightKg(weightKg);
    }
}