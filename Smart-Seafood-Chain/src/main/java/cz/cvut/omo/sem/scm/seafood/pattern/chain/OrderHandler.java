package cz.cvut.omo.sem.scm.seafood.pattern.chain;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.resource.Money;

/**
 * Chain of Responsibility Pattern.
 * Handles order requests through the supply chain hierarchy.
 */
public abstract class OrderHandler {

    protected OrderHandler nextHandler;

    public OrderHandler setNext(OrderHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public abstract void handleOrder(Party buyer, String itemType, double amount, Money maxPrice);
}