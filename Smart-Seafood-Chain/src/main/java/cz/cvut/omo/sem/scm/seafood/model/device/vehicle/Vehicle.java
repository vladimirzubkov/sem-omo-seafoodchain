package cz.cvut.omo.sem.scm.seafood.model.device.vehicle;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.model.device.vehicle.component.EnergySource;
import cz.cvut.omo.sem.scm.seafood.pattern.state.device.ActiveState;
import cz.cvut.omo.sem.scm.seafood.pattern.state.device.BrokenState;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.device.VehicleCategory;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.operation.LockState;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract base class for all transportation assets (Trucks, Vans, Bikes).
 * Integrates Energy Strategy with Device State Pattern.
 */
@Getter
@Setter
public abstract class Vehicle extends Device {

    private double maxSpeedKmH;
    private double cargoCapacityKg;
    private LockState currentLockState = LockState.LOCKED;

    // COMPOSITION: The strategy for powering the vehicle
    private EnergySource powerUnit;

    public Vehicle(String id, String name, EnergySource powerUnit, double baseConsumption, Money cost, double speed, double capacity) {
        super(id, name, powerUnit.getResourceType(), baseConsumption, cost);

        this.powerUnit = powerUnit;
        this.maxSpeedKmH = speed;
        this.cargoCapacityKg = capacity;

        // Security sensors
        this.attachSensor(SensorType.GPS);
        this.attachSensor(SensorType.ELECTRONIC_LOCK);
    }

    /**
     * Polymorphic method to get the vehicle class.
     * Forces subclasses to declare their category.
     */
    public abstract VehicleCategory getCategory();

    protected double getLockStatus() {
        return currentLockState.getSignalValue();
    }

    @Override
    public void consumeEnergy() {
        // Prevent double consumption.
        // Vehicles handle their own fuel logic via EnergySource strategy in handleTick().
        // We can just log specific Vehicle consumption here if needed,
        // or leave empty to rely on the powerUnit.consume() call.
    }

    /**
     * Override handleTick to inject Fuel/Energy logic BEFORE standard Device behavior.
     */
    @Override
    public void handleTick() {
        // 1. Check if the vehicle is currently in a state that consumes energy (Active)
        // We use the helper method from Device which delegates to the State
        if (isOperational()) {

            // 2. Try to consume fuel (Strategy Pattern)
            boolean hasFuel = powerUnit.consume(this.getConsumptionPerHour());

            if (!hasFuel) {
                System.out.println("Vehicle %s ran out of %s".formatted(getName(), powerUnit.getResourceType()));

                // 3. State Transition: Instead of setOperational(false), we set the State explicitly
                this.setState(new BrokenState());

                // 4. Fire Event
                this.fireEvent(EventType.DEVICE_BREAKDOWN,
                        "Vehicle stalled: Out of %s".formatted(powerUnit.getResourceType()));

                return; // Stop execution here, do not proceed to super.handleTick() (no wear added this tick)
            }
        }

        // 5. If we have fuel (or if we are already broken/repairing),
        // delegate to the Device's state logic (wear calculation, repair timer, etc.)
        super.handleTick();
    }
}