package cz.cvut.omo.sem.scm.seafood.model.device;

import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConveyorBelt extends Device {

    private double speedMetersPerSec;

    public ConveyorBelt(String id, double energy, Money cost, double speed) {
        super(id, "Conveyor Belt", ResourceType.ELECTRICITY, energy, cost);
        this.speedMetersPerSec = speed;

        // Conveyors need Vibration sensors to detect motor issues
        this.attachSensor(SensorType.VIBRATION);
    }

    @Override
    protected double getBaseValueForSensor(SensorType type) {
        if (type == SensorType.VIBRATION) {
            // If operational, normal vibration is 5.0. If broken/off, 0.0.
            return this.isOperational() ? 5.0 : 0.0;
        }
        return 0.0;
    }
}