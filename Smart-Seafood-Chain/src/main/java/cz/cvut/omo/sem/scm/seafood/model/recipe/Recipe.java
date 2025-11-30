package cz.cvut.omo.sem.scm.seafood.model.recipe;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.monad.CookingProcess;

/**
 * Represents a production recipe.
 * Instead of a static list, it is a Function that builds a processing pipeline.
 */
public interface Recipe {

    String getName();

    /**
     * Defines the algorithmic steps to create the product.
     * @param rawInput The raw ingredient.
     * @param context The party executing the recipe (provides inventory for ingredients).
     * @return The process monad.
     */
    CookingProcess<? extends Item> prepare(Item rawInput, Party context);
}