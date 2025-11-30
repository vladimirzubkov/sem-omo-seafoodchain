package cz.cvut.omo.sem.scm.seafood.model.device;

import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.RobotCapability;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Universal machine for processing food.
 * Can perform: Cutting, Cooking, Mixing, Filleting.
 */
@Getter
@Setter
public class FoodProcessingRobot extends Device {

    // Capability flags - what this specific robot model can do
    private Set<RobotCapability> capabilities = new HashSet<>();

    public FoodProcessingRobot(String id, double energy, Money cost, Set<RobotCapability> capabilities) {
        super(id, "Food Robot", ResourceType.ELECTRICITY, energy, cost);
        this.capabilities = capabilities;

        // Robots need vibration monitoring for motor health
        this.attachSensor(SensorType.VIBRATION);

        // If it cooks, it might need temperature monitoring too
        if (can(RobotCapability.COOKING)) {
            this.attachSensor(SensorType.TEMPERATURE);
        }
    }

    /**
     * Checks if the robot supports a specific operation.
     */
    public boolean can(RobotCapability capability) {
        return capabilities.contains(capability);
    }

    @Override
    protected double getBaseValueForSensor(SensorType type) {
        if (type == SensorType.VIBRATION) {
            return this.isOperational() ? 4.5 : 0.0;
        }
        if (type == SensorType.TEMPERATURE && can(RobotCapability.COOKING)) {
            return this.isOperational() ? 180.0 : 20.0; // Cooking temp vs Room temp
        }
        return 0.0;
    }
}