package cz.cvut.omo.sem.scm.seafood.model.device.vehicle.component;

import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CombustionEngine implements EnergySource {

    private final ResourceType fuelType; // DIESEL or PETROL
    private final double tankCapacityLiters;
    private double currentFuelLiters;

    public CombustionEngine(ResourceType fuelType, double tankCapacityLiters) {
        // Validate that we only put liquid fuels here
        if (fuelType != ResourceType.DIESEL && fuelType != ResourceType.PETROL) {
            throw new IllegalArgumentException("Combustion engine requires DIESEL or PETROL");
        }
        this.fuelType = fuelType;
        this.tankCapacityLiters = tankCapacityLiters;
        this.currentFuelLiters = tankCapacityLiters; // Start full
    }

    @Override
    public boolean consume(double amount) {
        if (currentFuelLiters >= amount) {
            currentFuelLiters -= amount;
            return true;
        }
        return false; // Tank empty
    }

    @Override
    public void replenish() {
        this.currentFuelLiters = tankCapacityLiters;
    }

    @Override
    public ResourceType getResourceType() {
        return fuelType;
    }

    @Override
    public String getDescription() {
        return "Combustion Engine (" + fuelType + ")";
    }
}