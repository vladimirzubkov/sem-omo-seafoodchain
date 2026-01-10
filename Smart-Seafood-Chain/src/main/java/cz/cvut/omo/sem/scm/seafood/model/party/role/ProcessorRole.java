package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.model.recipe.Recipe;
import cz.cvut.omo.sem.scm.seafood.pattern.monad.CookingProcess;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Role responsible for transforming raw materials into finished products using Recipes.
 */
public class ProcessorRole implements BusinessRole {

    private final List<Recipe> knownRecipes;

    public ProcessorRole(List<Recipe> knownRecipes) {
        this.knownRecipes = knownRecipes;
    }

    @Override
    public void performLogic(Party context) {
        // Create a snapshot of the inventory to avoid ConcurrentModificationException
        // because we might remove items (ingredients) and add items (products) inside the loop.
        List<Item> snapshot = new ArrayList<>(context.getInventory());

        for (Item item : snapshot) {
            // Processors currently only look for raw Seafood to process
            if (item instanceof Seafood seafood) {
                tryProcess(seafood, context);
            }
        }
    }

    private void tryProcess(Seafood item, Party context) {
        for (Recipe recipe : knownRecipes) {
            // Execute the Cooking Process Monad
            CookingProcess.CookingResult<?> result = recipe.prepare(item, context).complete();

            if (result.success()) {
                // 1. Remove the raw material (it was consumed/transformed)
                context.getInventory().remove(item);

                // 2. Add the finished product to inventory
                Item product = (Item) result.resultItem();
                context.getInventory().add(product);

                System.out.println("[PROCESSOR] %s transformed %s into %s (Recipe: %s)".formatted(
                        context.getName(), item.getName(), product.getName(), recipe.getName()));

                // Stop trying other recipes for this specific item (one item -> one product)
                return;
            } else {
                // If recipe failed (e.g., missing ingredients), silently continue to the next recipe
                // Optional: Log failure reason from result.logs()
            }
        }
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.PROCESSOR;
    }
}