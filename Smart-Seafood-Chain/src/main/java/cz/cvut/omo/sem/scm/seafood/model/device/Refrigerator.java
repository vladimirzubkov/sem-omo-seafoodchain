package cz.cvut.omo.sem.scm.seafood.model.device;

import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Refrigerator extends Device {

    private StorageTemperature storageMode;

    public Refrigerator(String id, double energy, Money cost) {
        super(id, "Commercial Fridge", ResourceType.ELECTRICITY, energy, cost);
        this.storageMode = StorageTemperature.CHILLED;

        this.attachSensor(SensorType.TEMPERATURE);
    }

    public double getTargetTemperature() {
        return storageMode.getOptimal();
    }

    @Override
    protected double getBaseValueForSensor(SensorType type) {
        if (type == SensorType.TEMPERATURE) {
            return getTargetTemperature();
        }
        return 0.0;
    }
}