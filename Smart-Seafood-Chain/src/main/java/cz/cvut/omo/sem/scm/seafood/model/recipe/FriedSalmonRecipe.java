package cz.cvut.omo.sem.scm.seafood.model.recipe;

import cz.cvut.omo.sem.scm.seafood.model.item.FinishedProduct;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.monad.CookingProcess;
import cz.cvut.omo.sem.scm.seafood.type.device.RobotCapability;
import cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType;
import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;

public class FriedSalmonRecipe implements Recipe {

    @Override
    public String getName() {
        return "Fried Salmon with Potatoes and Sour Cream";
    }

    @Override
    public CookingProcess<? extends Item> prepare(Item rawInput, Party context) {
        if (!(rawInput instanceof Seafood)) {
            return CookingProcess.begin(rawInput).ensure(i -> false, "Input is not Seafood!");
        }

        Seafood rawFish = (Seafood) rawInput;

        return CookingProcess.begin(rawFish)
                // 1. Validation
                .ensure(fish -> fish.getSeafoodType() == SeafoodType.SALMON, "Must be Salmon")
                .ensure(fish -> fish.getQualityLevel() > 90.0, "Salmon must be premium quality")

                // 2. Processing (Filleting)
                .process(RobotCapability.FILLETING, item -> {
                    item.setWeightKg(item.getWeightKg() * 0.85); // 15% waste (skin/bones)
                    return item;
                })

                // 3. Side Dish Ingredients
                .requireIngredient(context, MaterialType.POTATOES, 0.300) // 300g potatoes
                .requireIngredient(context, MaterialType.SOUR_CREAM, 0.050) // 50g sour cream dip
                .requireIngredient(context, MaterialType.OIL, 0.010)      // For grilling

                // 4. Cooking (Grilling/Frying)
                .process(RobotCapability.COOKING, item -> {
                    // Combined weight of fish + potatoes + dip
                    item.setWeightKg(item.getWeightKg() + 0.350);
                    item.recordTemperature(65.0); // Served warm
                    return item;
                })

                // 5. Garnish (Optional, assumes generic herbs exist or skip)
                // .requireIngredient(context, MaterialType.HERBS, 0.002)

                // 6. Packaging into a meal box
                .requireIngredient(context, MaterialType.CARDBOARD_BOX, 1.0)

                // 7. Final Transformation
                .transform(item -> new FinishedProduct(
                        item.getItemId() + "-salmon-meal",
                        getName(),
                        item.getWeightKg()
                ), "Final Assembly");
    }
}