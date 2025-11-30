package cz.cvut.omo.sem.scm.seafood.model.device.vehicle.component;

import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;

/**
 * Strategy interface for vehicle power units.
 * Allows decoupling the vehicle logic from the specific fuel type (Diesel, Electric, Manual).
 */
public interface EnergySource {

    /**
     * Attempts to consume a specific amount of energy/fuel.
     * @param amount Units of energy (Liters, kWh, or Calories) required.
     * @return true if there was enough energy and it was consumed; false if empty.
     */
    boolean consume(double amount);

    /**
     * Returns the type of resource this engine consumes.
     */
    ResourceType getResourceType();

    /**
     * Human-readable description for reports (e.g. "V8 Diesel Engine").
     */
    String getDescription();

    /**
     * Refills the energy source to max capacity (Refuel/Recharge/Eat).
     */
    void replenish();
}