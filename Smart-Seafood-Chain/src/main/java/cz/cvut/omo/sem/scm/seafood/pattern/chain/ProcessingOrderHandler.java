package cz.cvut.omo.sem.scm.seafood.pattern.chain;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.resource.Money;

/**
 * Middleware in the chain. Checks if raw materials are needed.
 */
public class ProcessingOrderHandler extends OrderHandler {

    private final Party me;

    public ProcessingOrderHandler(Party me) {
        this.me = me;
    }

    @Override
    public void handleOrder(Party buyer, String itemType, double amount, Money maxPrice) {
        // Check if I am a Processor
        if (me.getType().equalsIgnoreCase("PROCESSOR")) {
            System.out.println("[CHAIN] %s (Processor) received order. Checking raw materials...".formatted(
                    me.getName()));

            // Logic: A processor needs raw fish to create products.
            // We simulate passing the order upstream to the Fisher.
            if (nextHandler != null) {
                // He asks for slightly more raw material to account for waste
                nextHandler.handleOrder(me, "Raw %s".formatted(itemType), amount * 1.2, maxPrice);
            } else {
                System.out.println("[CHAIN] Processor cannot fulfill order: No raw material provider linked.");
            }
        } else {
            // Not a processor, pass it along
            if (nextHandler != null) {
                nextHandler.handleOrder(buyer, itemType, amount, maxPrice);
            }
        }
    }
}