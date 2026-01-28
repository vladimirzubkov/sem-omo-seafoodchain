package cz.cvut.omo.sem.scm.seafood.pattern.chain;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.resource.Money;

// it is more convenient to use abstarct class in chain pattern than interface, for the reason:
// - define next, setNext, passToNext (delegate) - all in abstract class, DRY - don't repeat yourself.
public abstract class OrderHandler {

    protected OrderHandler nextHandler;

    public OrderHandler setNext(OrderHandler nextHandler) {
        this.nextHandler = nextHandler;
        // Important: return handler to build a chain
        // handler1.setNext(handler2).setNext(handler3)
        return nextHandler;
    }

    /**
     * Tries to handle the order. If logic fails here, it should delegate to next.
     */
    public abstract void handleOrder(Party buyer, String itemType, double amount, Money maxPrice);

    // Helper method to pass upstream
    protected void passToNext(Party buyer, String itemType, double amount, Money maxPrice) {
        if (nextHandler != null) {
            nextHandler.handleOrder(buyer, itemType, amount, maxPrice);
        } else {
            System.out.println("[CHAIN] Order failed: Chain ended, no provider for " + itemType);
        }
    }
}