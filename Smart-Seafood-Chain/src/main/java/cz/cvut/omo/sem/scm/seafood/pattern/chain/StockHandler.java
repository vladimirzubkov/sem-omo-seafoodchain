package cz.cvut.omo.sem.scm.seafood.pattern.chain;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.resource.Money;

public class StockHandler extends OrderHandler {

    private final Party me;

    public StockHandler(Party me) {
        this.me = me;
    }

    @Override
    public void handleOrder(Party buyer, String itemType, double amount, Money maxPrice) {
        // 1. Looking for a product in stock (simplified logic)
        boolean inStock = me.getInventory().stream()
                .anyMatch(i -> i.getName().equalsIgnoreCase(itemType));

        if (inStock) {
            System.out.println("[CHAIN] %s has %s in stock! Order fulfilled immediately."
                    .formatted(me.getName(), itemType));
            // Here would be the transaction logic: buyer.pay(me), me.transfer(item)
        } else {
            System.out.println("[CHAIN] %s doesn't have %s. Checking suppliers...".formatted(me.getName(), itemType));
            passToNext(buyer, itemType, amount, maxPrice);
        }
    }
}