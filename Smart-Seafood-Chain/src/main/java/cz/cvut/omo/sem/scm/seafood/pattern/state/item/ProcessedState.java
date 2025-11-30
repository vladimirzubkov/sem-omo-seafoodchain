package cz.cvut.omo.sem.scm.seafood.pattern.state.item;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;

/**
 * State after the fish has been filleted/cooked.
 */
public class ProcessedState implements ItemLifecycleState {
    // Implementation similiar to others...

    @Override
    public void handleTemperature(Item context, double currentTemp) {
        // Processed food might be more stable or strictly frozen
    }

    @Override
    public ItemLifecycleState nextState(Item context) {
        return new SoldState();
    }

    @Override
    public String getStateName() {
        return "PROCESSED";
    }
}