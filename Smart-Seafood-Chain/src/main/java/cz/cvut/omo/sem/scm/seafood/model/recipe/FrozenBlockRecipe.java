package cz.cvut.omo.sem.scm.seafood.model.recipe;

import cz.cvut.omo.sem.scm.seafood.model.item.FinishedProduct;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.monad.CookingProcess;
import cz.cvut.omo.sem.scm.seafood.type.device.RobotCapability;
import cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType;
import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;

import java.util.Set;

public class FrozenBlockRecipe implements Recipe {

    // Set of fish types this recipe accepts
    private static final Set<SeafoodType> ACCEPTED_TYPES = Set.of(
            SeafoodType.HERRING,
            SeafoodType.SPRAT,
            SeafoodType.COD,
            SeafoodType.PERCH);

    @Override
    public String getName() {
        return "Frozen Fish Block";
    }

    @Override
    public CookingProcess<? extends Item> prepare(Item rawInput, Party context) {
        if (!(rawInput instanceof Seafood)) {
            return CookingProcess.begin(rawInput).ensure(i -> false, "Input is not Seafood!");
        }

        Seafood rawFish = (Seafood) rawInput;

        return CookingProcess.begin(rawFish)
                // 1. Validation
                .ensure(fish -> ACCEPTED_TYPES.contains(fish.getSeafoodType()),
                        "Fish type not suitable for Frozen Block")

                // 2. Processing (Cleaning/Filleting) - using FILLETING as closest capability
                .process(RobotCapability.FILLETING, item -> {
                    item.setWeightKg(item.getWeightKg() * 0.90); // 10% waste (cleaning)
                    return item;
                })

                // 3. Packaging (Simplified: No physical box item required)

                // 4. Final Transformation
                .transform(item -> {
                    // Simulating freezing by setting low temperature
                    item.recordTemperature(-18.0);

                    return new FinishedProduct(
                            item.getItemId() + "-frozen-block",
                            getName() + " (" + ((Seafood) item).getSeafoodType() + ")",
                            item.getWeightKg());
                }, "Freezing and Boxing");
    }
}