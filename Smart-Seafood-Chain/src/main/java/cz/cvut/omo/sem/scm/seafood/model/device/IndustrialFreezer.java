package cz.cvut.omo.sem.scm.seafood.model.device;

import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndustrialFreezer extends Device {

    private StorageTemperature storageMode;
    public IndustrialFreezer(String id, double energy, Money cost, StorageTemperature mode) {
        super(id, "Industrial IndustrialFreezer", ResourceType.ELECTRICITY, energy, cost);
        this.storageMode = mode;

        // Freezers always come with a Temp Sensor by default
        this.attachSensor(SensorType.TEMPERATURE);
    }

    /**
     * Helper to get the target temperature from the mode.
     */
    public double getTargetTemperature() {
        return storageMode.getOptimal();
    }

    @Override
    protected double getBaseValueForSensor(SensorType type) {
        if (type == SensorType.TEMPERATURE) {
            return getTargetTemperature(); // -18.0
        }
        return 0.0; // Fallback
    }
}