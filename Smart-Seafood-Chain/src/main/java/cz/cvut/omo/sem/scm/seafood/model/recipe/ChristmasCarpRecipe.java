package cz.cvut.omo.sem.scm.seafood.model.recipe;

import cz.cvut.omo.sem.scm.seafood.model.item.FinishedProduct;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.monad.CookingProcess;
import cz.cvut.omo.sem.scm.seafood.type.device.RobotCapability;
import cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType;
import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;

public class ChristmasCarpRecipe implements Recipe {

    @Override
    public String getName() {
        return "Christmas Carp with Potato Salad";
    }

    @Override
    public CookingProcess<? extends Item> prepare(Item rawInput, Party context) {
        // Fail-fast check for type
        if (!(rawInput instanceof Seafood)) {
            return CookingProcess.begin(rawInput).ensure(i -> false, "Input is not Seafood!");
        }

        Seafood rawFish = (Seafood) rawInput;

        return CookingProcess.begin(rawFish)
                // 1. Validate Ingredient (Carp)
                // Assuming SeafoodType.CARP exists, otherwise remove this specific check
                // .ensure(fish -> fish.getSeafoodType() == SeafoodType.CARP, "Must be Carp")
                .ensure(fish -> fish.getQualityLevel() > 80.0, "Fish quality too low for Christmas dinner")

                // 2. Preparation (Scaling and Filleting)
                .process(RobotCapability.FILLETING, item -> {
                    item.setWeightKg(item.getWeightKg() * 0.7); // 30% waste (bones, head, scales)
                    return item;
                })

                // 3. Breading ingredients (Flour/Breadcrumbs)
                .requireIngredient(context, MaterialType.FLOUR, 0.050)

                // 4. Potato Salad Ingredients
                .requireIngredient(context, MaterialType.POTATOES, 0.250) // 250g potatoes
                .requireIngredient(context, MaterialType.ONION, 0.050)    // 50g onion
                .requireIngredient(context, MaterialType.MAYONNAISE, 0.050)
                .requireIngredient(context, MaterialType.OIL, 0.020)      // For frying

                // 5. Cooking Process (Frying the carp and boiling potatoes)
                .process(RobotCapability.COOKING, item -> {
                    // Add weight of side dish (approx 350g added total)
                    item.setWeightKg(item.getWeightKg() + 0.350);
                    item.recordTemperature(75.0); // Served hot
                    return item;
                })

                // 6. Packaging
                .requireIngredient(context, MaterialType.PLASTIC_TRAY, 1.0)

                // 7. Final Product Transformation
                .transform(item -> new FinishedProduct(
                        item.getItemId() + "-xmas-carp",
                        getName(),
                        item.getWeightKg()
                ), "Plating Christmas Dinner");
    }
}