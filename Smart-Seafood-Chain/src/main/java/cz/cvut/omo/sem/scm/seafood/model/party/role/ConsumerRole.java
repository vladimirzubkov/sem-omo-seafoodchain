package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.item.FinishedProduct;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Capability to consume goods from the inventory.
 * Represents the "End of Life" (Sink) for a product in the supply chain.
 */
public class ConsumerRole implements BusinessRole {

    private final BusinessRoleType specificType; // RESTAURANT or CUSTOMER
    private final double appetite;

    public ConsumerRole(BusinessRoleType specificType, double appetite) {
        this.specificType = specificType;
        this.appetite = appetite;
    }

    @Override
    public void performLogic(Party context) {
        List<Item> itemsToConsume = new ArrayList<>(context.getInventory());
        double consumedThisTick = 0.0;

        for (Item item : itemsToConsume) {
            if (consumedThisTick >= appetite) break;

            if (isEdible(item)) {
                double weight = item.getWeightKg();
                consume(item, context);
                consumedThisTick += weight;
            }
        }
    }

    private boolean isEdible(Item item) {
        return item instanceof Seafood || item instanceof FinishedProduct;
    }

    private void consume(Item item, Party context) {
        // STATE PATTERN: Trigger transition
        // Item state moves from SoldState -> End (e.g., effectively removed from tracking)
        // or just ensure it reached the final logical state.
        item.nextState();

        context.getInventory().remove(item);

        System.out.println("[%s] %s consumed item: %s (Final State: %s)".formatted(
                specificType, // Dynamic log based on role type
                context.getName(),
                item.getItemId(),
                (item.getLifecycleState() != null ? item.getLifecycleState().getStateName() : "N/A")
        ));
    }

    @Override
    public BusinessRoleType getRoleType() {
        return specificType;
    }
}