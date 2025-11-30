package cz.cvut.omo.sem.scm.seafood.model.device.vehicle;

import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.model.device.vehicle.component.EnergySource;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.operation.LockState;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract base class for all transportation assets (Trucks, Vans, Bikes).
 * Extends Device as vehicles consume resources, suffer wear, and require maintenance.
 */
@Getter
@Setter
public abstract class Vehicle extends Device {

    private double maxSpeedKmH;
    private double cargoCapacityKg;
    private LockState currentLockState = LockState.LOCKED;

    // COMPOSITION: The strategy for powering the vehicle
    private EnergySource powerUnit;

    /**
     * Constructor for Transportation Assets.
     *
     * @param powerUnit Strategy object handling fuel/energy logic.
     * @param baseConsumption Consumption rate passed to the Device base class.
     */
    public Vehicle(String id, String name, EnergySource powerUnit, double baseConsumption, Money cost, double speed, double capacity) {
        // Pass the resource type from the PowerUnit up to the Device base class
        super(id, name, powerUnit.getResourceType(), baseConsumption, cost);

        this.powerUnit = powerUnit;
        this.maxSpeedKmH = speed;
        this.cargoCapacityKg = capacity;

        // All vehicles must have GPS and an Electronic Lock for security/tracking (FRQ7)
        this.attachSensor(SensorType.GPS);
        this.attachSensor(SensorType.ELECTRONIC_LOCK);
    }

    /**
     * Provides the current lock status signal value (1.0, 0.0, or -1.0) to the sensor.
     */
    protected double getLockStatus() {
        return currentLockState.getSignalValue();
    }

    /**
     * Override handleTick to include fuel consumption check.
     */
    @Override
    public void handleTick() {
        if (isOperational()) {
            // Try to consume fuel for this hour
            // We use 'getEnergyConsumptionPerHour()' from the Device parent
            boolean hasFuel = powerUnit.consume(this.getConsumptionPerHour());

            if (!hasFuel) {
                System.out.println("Vehicle " + getName() + " ran out of " + powerUnit.getResourceType());
                this.setOperational(false); // Stop the vehicle
                // Ideally trigger an EVENT here
            } else {
                super.handleTick(); // Proceed with standard device ticking (wear, sensors)
            }
        }
    }
}