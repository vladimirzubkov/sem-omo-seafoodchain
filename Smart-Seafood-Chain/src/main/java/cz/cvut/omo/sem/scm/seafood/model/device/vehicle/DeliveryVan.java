package cz.cvut.omo.sem.scm.seafood.model.device.vehicle;

import cz.cvut.omo.sem.scm.seafood.model.device.vehicle.component.CombustionEngine;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.device.VehicleCategory;
import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;

public class DeliveryVan extends Vehicle {

    /**
     * @param fuelType - DIESEL or PETROL (passed from config)
     */
    public DeliveryVan(String id, double energyConsumption, Money cost, ResourceType fuelType) {
        // Создаем двигатель с переданным типом топлива
        super(id, "Delivery Van",
                new CombustionEngine(fuelType, 80.0), // 80L tank
                energyConsumption, cost, 110.0, 1500.0);

        this.attachSensor(SensorType.ELECTRONIC_LOCK);
        this.attachSensor(SensorType.VIBRATION);
    }

    @Override
    protected double getBaseValueForSensor(SensorType type) {
        if (type == SensorType.ELECTRONIC_LOCK) return getLockStatus();
        if (type == SensorType.VIBRATION) return isOperational() ? 1.5 : 0.0;
        return 0.0;
    }

    @Override
    public VehicleCategory getCategory() {
        return VehicleCategory.LIGHT_COMMERCIAL;
    }
}