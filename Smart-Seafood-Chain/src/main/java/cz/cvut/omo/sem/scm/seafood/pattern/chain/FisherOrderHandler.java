package cz.cvut.omo.sem.scm.seafood.pattern.chain;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.builder.EventBuilder;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;

/**
 * The end of the chain. The Fisher receives the demand and catches fish.
 */
public class FisherOrderHandler extends OrderHandler {

    private final Party me;

    public FisherOrderHandler(Party me) {
        this.me = me;
    }

    @Override
    public void handleOrder(Party buyer, String itemType, double amount, Money maxPrice) {
        if (me.getType().equalsIgnoreCase("FISHER") || me.getName().toLowerCase().contains("fisher")) {
            System.out.println("[CHAIN] Fisher %s accepted order! Deploying nets for %s".formatted(
                    me.getName(), itemType));

            // Trigger a DEMAND_CREATED event.
            // In the next simulation tick, the ProducerRole could react to this event
            // and increase catch probability or target specific fish.
            if (me.getEventBus() != null) {
                me.getEventBus().publish(new EventBuilder()
                        .type(EventType.DEMAND_CREATED)
                        .sourceId(me.getId())
                        .description("Order received for %.2f kg of %s".formatted(amount, itemType))
                        .build());
            }
        } else {
            System.out.println("[CHAIN] End of chain reached. Order cannot be fulfilled.");
        }
    }
}