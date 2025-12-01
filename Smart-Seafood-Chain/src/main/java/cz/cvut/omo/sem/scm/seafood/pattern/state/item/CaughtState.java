package cz.cvut.omo.sem.scm.seafood.pattern.state.item;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;

/**
 * Initial state: Fish just caught on the boat.
 * Very sensitive to spoilage.
 */
public class CaughtState implements ItemLifecycleState {

    @Override
    public void handleTemperature(Item context, double currentTemp) {
        // Strict check: if temp > 4C, quality drops fast
        if (currentTemp > 4.0) {
            double drop = (currentTemp - 4.0) * 2.0;
            context.setQualityLevel(context.getQualityLevel() - drop);
            System.out.println("Warning: Fresh catch is warming up!");
        }
    }

    @Override
    public ItemLifecycleState nextState(Item context) {
        // Usually moves to Storage or Transport
        return new StoredState();
    }

    @Override
    public String getStateName() {
        return "CAUGHT";
    }
}