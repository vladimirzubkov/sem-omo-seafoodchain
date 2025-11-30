package cz.cvut.omo.sem.scm.seafood.model.device.vehicle;

import cz.cvut.omo.sem.scm.seafood.model.device.vehicle.component.CombustionEngine;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;

public class Truck extends Vehicle {

    private final StorageTemperature fridgeMode;

    /**
     * @param fuelType - Usually DIESEL, but allows PETROL if needed.
     */
    public Truck(String id, double energyConsumption, Money cost, ResourceType fuelType) {
        super(id, "Refrigerated Truck",
                new CombustionEngine(fuelType, 500.0), // 500L tank
                energyConsumption, cost, 90.0, 20000.0);

        this.fridgeMode = StorageTemperature.FROZEN;

        this.attachSensor(SensorType.TEMPERATURE);
        this.attachSensor(SensorType.ELECTRONIC_LOCK);
        this.attachSensor(SensorType.VIBRATION);
    }

    @Override
    protected double getBaseValueForSensor(SensorType type) {
        return switch (type) {
            case TEMPERATURE -> fridgeMode.getOptimal();
            case ELECTRONIC_LOCK -> getLockStatus();
            case VIBRATION -> isOperational() ? 2.0 : 0.0;
            default -> 0.0;
        };
    }
}