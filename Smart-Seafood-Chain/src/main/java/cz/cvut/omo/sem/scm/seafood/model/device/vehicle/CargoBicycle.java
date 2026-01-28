package cz.cvut.omo.sem.scm.seafood.model.device.vehicle;

import cz.cvut.omo.sem.scm.seafood.model.device.vehicle.component.ManualPower;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.device.VehicleCategory;

public class CargoBicycle extends Vehicle {

    public CargoBicycle(String id, Money cost) {
        // FIX: Inject ManualPower strategy.
        // Base consumption is 0.0 because ManualPower handles "fuel" differently (infinite or calories).
        super(id, "Cargo Bike", new ManualPower(), 0.0, cost, 20.0, 50.0);
    }

    @Override
    protected double getBaseValueForSensor(SensorType type) {
        // Cargo bikes usually don't have active sensors like Fridges or Vibrating motors
        return 0.0;
    }

    @Override
    public VehicleCategory getCategory() {
        return VehicleCategory.MANUAL_POWER;
    }
}