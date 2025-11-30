package cz.cvut.omo.sem.scm.seafood.model.recipe;

import cz.cvut.omo.sem.scm.seafood.model.item.FinishedProduct;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.monad.CookingProcess;
import cz.cvut.omo.sem.scm.seafood.type.device.RobotCapability;
import cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType;

public class NigiriRecipe implements Recipe {

    @Override
    public String getName() {
        return "Salmon Nigiri";
    }

    @Override
    public CookingProcess<? extends Item> prepare(Item rawInput, Party context) {

        if (!(rawInput instanceof Seafood)) {
            return CookingProcess.begin(rawInput).ensure(i -> false, "Input is not Seafood!");
        }

        return CookingProcess.begin((Seafood) rawInput)
                // 1. Quality Check
                .ensure(fish -> fish.getQualityLevel() > 85, "Fish quality too low")

                // 2. Ingredients from Inventory (Using Enums)
                .requireIngredient(context, MaterialType.SUSHI_RICE, 0.050)
                .requireIngredient(context, MaterialType.RICE_VINEGAR, 0.005)

                // 3. Cooking Process
                .process(RobotCapability.COOKING, item -> {
                    item.setWeightKg(item.getWeightKg() + 0.055);
                    return item;
                })

                // 4. Packaging Material
                .requireIngredient(context, MaterialType.PLASTIC_TRAY, 1.0) // 1 unit

                // 5. Final Assembly into Finished Product (not just a container)
                .transform(item -> new FinishedProduct(
                        item.getItemId() + "-nigiri",
                        getName(),
                        item.getWeightKg()
                ), "Final Assembly");
    }
}