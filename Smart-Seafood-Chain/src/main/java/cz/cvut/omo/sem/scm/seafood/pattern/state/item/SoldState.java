package cz.cvut.omo.sem.scm.seafood.pattern.state.item;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;

/**
 * Final State (Sink).
 * The item is with the customer.
 */
public class SoldState implements ItemLifecycleState {

    @Override
    public void handleTemperature(Item context, double currentTemp) {
        // We don't care anymore, it's sold.
    }

    @Override
    public ItemLifecycleState nextState(Item context) {
        // Terminal state - no transition
        return this;
    }

    @Override
    public String getStateName() {
        return "SOLD";
    }
}