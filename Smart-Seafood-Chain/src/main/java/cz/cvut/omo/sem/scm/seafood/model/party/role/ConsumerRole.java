package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Capability to consume goods from the inventory.
 * Represents the "End of Life" (Sink) for a product in the supply chain.
 * Used by: Customer, Restaurant Guests.
 */
public class ConsumerRole implements BusinessRole {

    /** * Consumption rate (e.g., how many kg or items consumed per tick).
     */
    private final double appetite;

    public ConsumerRole(double appetite) {
        this.appetite = appetite;
    }

    @Override
    public void performLogic(Party context) {
        // 1. Identify items in inventory available for consumption
        // Creating a copy of the list to avoid ConcurrentModificationException during removal
        List<Item> itemsToConsume = new ArrayList<>(context.getInventory());

        // TODO: Implement logic to filter items (e.g., consume only Food, not Materials)

        for (Item item : itemsToConsume) {
            // TODO: Check if appetite allows consuming this item now
            consume(item, context);
        }

        // 2. (Optional) Generate demand if inventory is empty (Trigger Pull Request)
        // TODO: Logic to trigger a "Request Goods" event if hungry
    }

    /**
     * Removes the item from the system (simulating eating/usage).
     */
    private void consume(Item item, Party context) {
        // Remove item from world/inventory
        context.getInventory().remove(item);

        System.out.println("[CONSUMER] " + context.getName() + " consumed item: " + item.getItemId());

        // TODO: Logic for satisfaction (e.g., if fish was fresh -> increase happiness)
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.CONSUMER;
    }
}