package cz.cvut.omo.sem.scm.seafood.model.device;

import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackagingMachine extends Device {

    private int maxPackagesPerMinute;

    public PackagingMachine(String id, double energy, Money cost, int maxPackagesPerMinute) {
        super(id, "Packaging Machine", ResourceType.ELECTRICITY, energy, cost);
        this.maxPackagesPerMinute = maxPackagesPerMinute;

        // Mechanical device -> Vibration sensor for wear detection
        this.attachSensor(SensorType.VIBRATION);
    }

    @Override
    protected double getBaseValueForSensor(SensorType type) {
        if (type == SensorType.VIBRATION) {
            return this.isOperational() ? 3.0 : 0.0; // Normal vibration level
        }
        return 0.0;
    }
}