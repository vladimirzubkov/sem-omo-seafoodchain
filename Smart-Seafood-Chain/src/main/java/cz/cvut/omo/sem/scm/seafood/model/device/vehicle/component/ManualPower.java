package cz.cvut.omo.sem.scm.seafood.model.device.vehicle.component;

import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;

/**
 * Represents human effort (e.g., for Cargo Bicycles).
 * infinite capacity for simplicity, but tracks usage.
 */
public class ManualPower implements EnergySource {

    @Override
    public boolean consume(double amount) {
        // Humans are resilient; we assume they can always pedal during the shift.
        // In a complex sim, we could track fatigue here.
        return true;
    }

    @Override
    public void replenish() {
        // Resting/Eating happens automatically between shifts
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.CALORIES;
    }

    @Override
    public String getDescription() {
        return "Manual Power (Human)";
    }
}