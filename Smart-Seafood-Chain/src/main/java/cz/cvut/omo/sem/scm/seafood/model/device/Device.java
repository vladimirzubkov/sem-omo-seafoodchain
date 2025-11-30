package cz.cvut.omo.sem.scm.seafood.model.device;

import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.device.sensor.IoTSensor;
import cz.cvut.omo.sem.scm.seafood.pattern.state.device.ActiveState;
import cz.cvut.omo.sem.scm.seafood.pattern.state.device.DeviceState;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public abstract class Device extends SimulationEntity {
    private double wearLevel = 0.0;
    private boolean isOperational = true;
    private ResourceType primaryResourceType; // e.g. ELECTRICITY or DIESEL
    private double consumptionPerHour;
    private Money maintenanceCost;

    private DeviceState state = new ActiveState(); // default

    // COMPOSITION: Device has IoT Sensors
    private List<IoTSensor> sensors = new ArrayList<>();

    // Manual constructor to satisfy SimulationEntity's requirements
    public Device(String id, String name, ResourceType resType, double consumption, Money cost) {
        super(id, name);
        this.primaryResourceType = resType;
        this.consumptionPerHour = consumption;
        this.maintenanceCost = cost;
    }

    // Helper to attach sensor dynamically
    public void attachSensor(SensorType type) {
        String sensorId = "%s-sensor-%s".formatted(this.getId(), type.name());
        this.sensors.add(new IoTSensor(sensorId, type));
    }

    @Override
    public void handleTick() {
        if (isOperational) {
            // 1. Increase wear logic (FRQ14)
            // wearLevel += 0.05;

            // 2. Update sensors (FRQ7)
            updateSensors();
        }
    }

    private void updateSensors() {
        for (IoTSensor sensor : sensors) {
            // Ask subclasses for the "normal" value (Template Method)
            double baseValue = getBaseValueForSensor(sensor.getType());

            // 1. Measure new value (simulation physics)
            sensor.measure(baseValue);

            // 2. Check for anomalies (Strategy hook)
            checkAnomaly(sensor, baseValue);
        }
    }

    protected void checkAnomaly(IoTSensor sensor, double targetValue) {
        // TODO: Implement hysteresis or threshold check here.
    }

    // Do not use default value, as it varies for different devices (temperature for oven and fridge, vibration)
    // also, Fail Fast if not defined in some device
    protected abstract double getBaseValueForSensor(SensorType type);

}