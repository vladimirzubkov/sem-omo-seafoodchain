package cz.cvut.omo.sem.scm.seafood.pattern.state.item;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;

/**
 * State when item is in a fridge/freezer.
 */
public class StorageState implements ItemLifecycleState {

    @Override
    public void handleTemperature(Item context, double currentTemp) {
        // Standard check based on required storage temp
        if (!context.getRequiredStorage().isSafe(currentTemp)) {
            context.setQualityLevel(context.getQualityLevel() - 0.5);
        }
    }

    @Override
    public ItemLifecycleState nextState(Item context) {
        // Moves to Processing or Sale
        return new ProcessedState();
    }

    @Override
    public String getStateName() {
        return "STORED";
    }
}