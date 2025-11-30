package cz.cvut.omo.sem.scm.seafood.pattern.state.item;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;

/**
 * State Interface for Item Lifecycle (Bonus Req 2).
 * Defines how an item behaves and reacts to environment in different stages.
 */
public interface ItemLifecycleState {

    /**
     * Reacts to temperature changes.
     * Logic: Fresh fish is more sensitive than Frozen fish.
     */
    void handleTemperature(Item context, double currentTemp);

    /**
     * Transitions the item to the next logical state.
     * @return The new state instance.
     */
    ItemLifecycleState nextState(Item context);

    String getStateName();
}