package cz.cvut.omo.sem.scm.seafood.model.device;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.device.sensor.IoTSensor;
import cz.cvut.omo.sem.scm.seafood.pattern.builder.EventBuilder;
import cz.cvut.omo.sem.scm.seafood.pattern.state.device.ActiveState;
import cz.cvut.omo.sem.scm.seafood.pattern.state.device.DeviceState;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.EntityVisitor;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.Visitable;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public abstract class Device extends SimulationEntity implements Visitable {

    private double wearLevel = 0.0; // 0.0 to 1.0 (100%)

    // REDUNDANCY REMOVED: 'isOperational' is now determined by the State object.

    private ResourceType primaryResourceType;
    private double consumptionPerHour;
    private Money maintenanceCost;

    // EVENT BUS: Needed to report breakdowns/repairs
    private EventBus eventBus;

    private DeviceState state = new ActiveState(); // Initial state

    // COMPOSITION: Device has IoT Sensors
    private List<IoTSensor> sensors = new ArrayList<>();

    public Device(String id, String name, ResourceType resType, double consumption, Money cost) {
        super(id, name);
        this.primaryResourceType = resType;
        this.consumptionPerHour = consumption;
        this.maintenanceCost = cost;
    }

    // --- VISITOR PATTERN ---
    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }

    // --- STATE PATTERN DELEGATION ---

    @Override
    public void handleTick() {
        // Delegate logic to the current state (Active, Broken, Repairing)
        state.onTick(this);
    }

    /**
     * Helper to check status without exposing the State object directly.
     */
    public boolean isOperational() {
        return state.isOperational();
    }

    // --- SENSOR LOGIC ---

    public void attachSensor(SensorType type) {
        String sensorId = "%s-sensor-%s".formatted(this.getId(), type.name());
        this.sensors.add(new IoTSensor(sensorId, type));
    }

    /**
     * Called by the State (e.g., inside ActiveState.onTick).
     */
    public void updateSensors() {
        for (IoTSensor sensor : sensors) {
            // Template Method: Get base value from subclass
            double baseValue = getBaseValueForSensor(sensor.getType());

            // 1. Measure new value (simulation physics)
            sensor.measure(baseValue);

            // 2. Check for anomalies
            checkAnomaly(sensor, baseValue);
        }
    }

    protected void checkAnomaly(IoTSensor sensor, double targetValue) {
        // TODO: Inject SensorValidationStrategy here (Hysteresis check)
        // For now, simple logic:
        double diff = Math.abs(sensor.getCurrentValue() - targetValue);
        if (diff > 5.0 && !sensor.isAlertActive()) {
            sensor.setAlertActive(true);
            fireEvent(EventType.DEVICE_BREAKDOWN, "Sensor anomaly detected: " + sensor.getType());
        }
    }

    // --- HELPER FOR EVENTS ---

    public void fireEvent(EventType type, String description) {
        if (eventBus != null) {
            Event event = new EventBuilder()
                    .type(type)
                    .sourceId(this.getId())
                    .description(description)
                    .build();
            eventBus.publish(event);
        }
    }

    // Abstract method to be implemented by concrete devices (Oven, Fridge, Truck)
    protected abstract double getBaseValueForSensor(SensorType type);
}