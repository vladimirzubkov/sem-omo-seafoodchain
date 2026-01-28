package cz.cvut.omo.sem.scm.seafood.pattern.chain;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.builder.EventBuilder;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;

public class FisherOrderHandler extends OrderHandler {

    private final Party me;

    public FisherOrderHandler(Party me) {
        this.me = me;
    }

    @Override
    public void handleOrder(Party buyer, String itemType, double amount, Money maxPrice) {
        // Fisher is the source. He doesn't pass to next.
        System.out.println("[CHAIN] Fisher %s accepted demand! Planning catch for %s".formatted(
                me.getName(), itemType));

        if (me.getEventBus() != null) {
            me.getEventBus().publish(new EventBuilder()
                    .type(EventType.DEMAND_CREATED)
                    .sourceId(me.getId())
                    .description("Order received for %.2f kg of %s".formatted(amount, itemType))
                    .build());
        }
    }
}