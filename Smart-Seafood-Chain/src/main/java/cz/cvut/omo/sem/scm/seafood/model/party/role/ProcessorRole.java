package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.model.production.ProductionLine;
import cz.cvut.omo.sem.scm.seafood.model.recipe.Recipe;
import cz.cvut.omo.sem.scm.seafood.pattern.monad.CookingProcess;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ProcessorRole implements BusinessRole {

    private final List<Recipe> knownRecipes;

    @Getter
    private final List<ProductionLine> productionLines = new ArrayList<>();

    public ProcessorRole(List<Recipe> knownRecipes) {
        this.knownRecipes = knownRecipes;
    }

    public void addProductionLine(ProductionLine line) {
        this.productionLines.add(line);
    }

    @Override
    public void performLogic(Party context) {
        // Snapshot to avoid concurrent modification while transforming inventory
        List<Item> snapshot = new ArrayList<>(context.getInventory());

        for (Item item : snapshot) {
            if (item instanceof Seafood seafood) {
                tryProcess(seafood, context);
            }
        }
    }

    private void tryProcess(Seafood item, Party context) {
        for (Recipe recipe : knownRecipes) {
            // MONAD: Process the item through the recipe steps
            CookingProcess.CookingResult<?> result = recipe.prepare(item, context).complete();

            if (result.success()) {
                // 1. PHYSICAL TRANSFORMATION
                context.getInventory().remove(item);
                Item product = (Item) result.resultItem();
                context.getInventory().add(product);

                // 2. ATTRIBUTION: Find the name of the worker who did the job
                // We look for any employee in this facility with a Labor role
                String workerName = context.getEmployees().stream()
                        .filter(e -> e.getRoles().stream()
                                .anyMatch(role -> role.getRoleType().toString().contains("LABOR")))
                        .map(e -> e.getName())
                        .findAny()
                        .orElse("Automatic Line");

                // 3. DETAILED LOGGING: Combining WHO + WHAT + INTO WHAT
                String detailedDescription = "Worker %s processed %s into %s (Recipe: %s)".formatted(
                        workerName,
                        item.getName(),
                        product.getName(),
                        recipe.getName()
                );

                // 4. REPORTING: Send the event that ConsumptionReport will use
                EventBus.getInstance().publish(new Event(
                        null,                           // Timestamp
                        EventType.ITEM_PROCESSED,       // Successful processing type
                        context.getId(),                // Source (The Company)
                        item.getItemId(),               // Target (The specific fish)
                        detailedDescription,            // THE CONCRETE MESSAGE
                        product                         // Payload
                ));

                return; // Finished with this item
            }
        }
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.PROCESSOR;
    }
}