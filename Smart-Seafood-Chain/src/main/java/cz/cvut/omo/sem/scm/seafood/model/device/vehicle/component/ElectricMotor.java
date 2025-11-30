package cz.cvut.omo.sem.scm.seafood.model.device.vehicle.component;

import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElectricMotor implements EnergySource {

    // Electric motors always consume Electricity
    private final ResourceType fuelType = ResourceType.ELECTRICITY;

    private final double batteryCapacityKwh;
    private double currentChargeKwh;

    public ElectricMotor(double batteryCapacityKwh) {
        this.batteryCapacityKwh = batteryCapacityKwh;
        this.currentChargeKwh = batteryCapacityKwh; // Start fully charged
    }

    @Override
    public boolean consume(double amount) {
        if (currentChargeKwh >= amount) {
            currentChargeKwh -= amount;
            return true;
        }
        return false; // Battery dead
    }

    @Override
    public void replenish() {
        this.currentChargeKwh = batteryCapacityKwh;
    }

    @Override
    public ResourceType getResourceType() {
        return fuelType;
    }

    @Override
    public String getDescription() {
        return "Electric Motor (Li-Ion Battery)";
    }
}