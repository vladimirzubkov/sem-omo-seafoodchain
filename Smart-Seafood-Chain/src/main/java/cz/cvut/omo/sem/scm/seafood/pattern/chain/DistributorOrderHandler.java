package cz.cvut.omo.sem.scm.seafood.pattern.chain;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.resource.Money;

public class DistributorOrderHandler extends OrderHandler {

    @Override
    public void handleOrder(Party buyer, String itemType, double amount, Money maxPrice) {
        // Logic: Distributors usually act as middlemen.
        // They might check their stock (StockHandler logic) or just route requests.

        System.out.println("[CHAIN] Distributor routing order for %s...".formatted(itemType));

        // Distributors usually take a cut (markup), so they pass a lower "maxPrice" upstream
        // to make profit, but here we keep it simple.
        passToNext(buyer, itemType, amount, maxPrice);
    }
}