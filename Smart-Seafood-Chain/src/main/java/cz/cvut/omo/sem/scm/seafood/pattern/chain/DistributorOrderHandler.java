package cz.cvut.omo.sem.scm.seafood.pattern.chain;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.resource.Money;

public class DistributorOrderHandler extends OrderHandler {

    @Override
    public void handleOrder(Party buyer, String itemType, double amount, Money maxPrice) {
        // Logic: Check if distributor has stock (Mock implementation)
        boolean hasStock = false;

        if (hasStock) {
            System.out.println("[CHAIN] Distributor accepting order for %.2f kg of %s".formatted(
                    amount, itemType));
            // Trigger Sales Transaction here...
        } else if (nextHandler != null) {
            System.out.println("[CHAIN] Distributor passing order for %s up the chain...".formatted(itemType));
            nextHandler.handleOrder(buyer, itemType, amount, maxPrice);
        } else {
            System.out.println("[CHAIN] Order failed: No provider found for %s".formatted(itemType));
        }
    }
}