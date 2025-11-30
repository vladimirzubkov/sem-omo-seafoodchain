package cz.cvut.omo.sem.scm.seafood.model.recipe;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.monad.CookingProcess;
import cz.cvut.omo.sem.scm.seafood.type.device.RobotCapability;
import cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType;
import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;

public class SushiBoxRecipe implements Recipe {

    @Override
    public String getName() {
        return "Premium Salmon Sushi Box";
    }

    @Override
    public CookingProcess<? extends Item> prepare(Item rawInput, Party context) {
        // We expect raw Seafood as input
        if (!(rawInput instanceof Seafood)) {
            return CookingProcess.begin(rawInput).ensure(i -> false, "Wrong Ingredient Type");
        }

        Seafood rawFish = (Seafood) rawInput;

        return CookingProcess.begin(rawFish)
                // 1. Initial Quality Control
                .ensure(fish -> fish.getSeafoodType() == SeafoodType.SALMON, "Must be Salmon")
                .ensure(fish -> fish.getQualityLevel() > 90.0, "Fish not fresh enough for Sushi")
                .ensure(fish -> fish.getCurrentTemperature() < 5.0, "Fish too warm!")

                // 2. Ingredients
                .requireIngredient(context, MaterialType.SUSHI_RICE, 0.200)

                // 3. Filleting (Machine Action)
                .process(RobotCapability.FILLETING, item -> {
                    item.setWeightKg(item.getWeightKg() * 0.6); // 40% waste
                    return item;
                })

                // 4. Cooking/Rice Assembly
                .process(RobotCapability.COOKING, item -> {
                    item.setWeightKg(item.getWeightKg() + 0.200); // Added rice
                    item.recordTemperature(20.0); // Warm rice
                    return item;
                })

                // 5. Cutting rolls
                .process(RobotCapability.CHOPPING, item -> item)

                // 6. Packaging Material usage
                .requireIngredient(context, MaterialType.CARDBOARD_BOX, 1.0)

                // 7. Packaging into container
                .packInto("Bento Box", 0.050);
    }
}