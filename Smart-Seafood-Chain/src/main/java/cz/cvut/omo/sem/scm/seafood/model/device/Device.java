package cz.cvut.omo.sem.scm.seafood.model.device;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.device.sensor.IoTSensor;
import cz.cvut.omo.sem.scm.seafood.model.device.sensor.hysteresis.HysteresisCheck;
import cz.cvut.omo.sem.scm.seafood.model.device.sensor.hysteresis.SensorValidationStrategy;
import cz.cvut.omo.sem.scm.seafood.pattern.builder.EventBuilder;
import cz.cvut.omo.sem.scm.seafood.pattern.state.device.ActiveState;
import cz.cvut.omo.sem.scm.seafood.pattern.state.device.BrokenState;
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
    private double totalEnergyConsumed = 0.0;

    private ResourceType primaryResourceType;
    private double consumptionPerHour;
    private Money maintenanceCost;

    // EVENT BUS: Needed to report breakdowns/repairs
    private EventBus eventBus;

    private DeviceState state = new ActiveState(); // Initial state

    // COMPOSITION: Device has IoT Sensors
    private List<IoTSensor> sensors = new ArrayList<>();

    // Strategy for sensor validation (Composition)
    // Default hysteresis: 5.0 degrees deviation allowed, 1.0 degree buffer for reset
    private SensorValidationStrategy sensorStrategy = new HysteresisCheck(5.0, 1.0);

    public Device(String id, String name, ResourceType resType, double consumption, Money cost) {
        super(id, name);
        this.primaryResourceType = resType;
        this.consumptionPerHour = consumption;
        this.maintenanceCost = cost;
    }

    // Logic called by ActiveState
    public void consumeEnergy() {
        // In a complex sim, we would deduct money from Party here.
        // For now, we track usage statistics.
        this.totalEnergyConsumed += this.consumptionPerHour;

        // Optional: Log heavy consumers to console
        // System.out.println("Device %s consumed %.2f %s".formatted(
        //        getName(), consumptionPerHour, primaryResourceType.getUnit()));
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

    /**
     * Checks sensor data for anomalies using the injected Strategy.
     */
    protected void checkAnomaly(IoTSensor sensor, double targetValue) {
        // Delegate validation to the Strategy (Hysteresis)
        // We pass current value, target value, and the CURRENT alert status to handle the buffer logic
        boolean isAnomalyNow = sensorStrategy.isAnomaly(
                sensor.getCurrentValue(),
                targetValue,
                sensor.isAlertActive()
        );

        // State switching logic based on Strategy result
        if (isAnomalyNow && !sensor.isAlertActive()) {
            // Case: Anomaly just started
            sensor.setAlertActive(true);

            // 1. Notify listeners
            fireEvent(EventType.DEVICE_BREAKDOWN, "Sensor anomaly detected: %s (Val: %.2f)".formatted(
                    sensor.getType(), sensor.getCurrentValue()));

            // 2. CRITICAL: Force state transition to Broken to stop the machine immediately.
            this.setState(new BrokenState());

        } else if (!isAnomalyNow && sensor.isAlertActive()) {
            // Case: Anomaly ended (system returned to normal within hysteresis limits)
            sensor.setAlertActive(false);

            // Optional: We could fire a "Normalization" event here,
            // but the device usually remains in BrokenState until a technician fixes it manually.
        }
    }

    // --- HELPER FOR EVENTS ---
    // Automatically prepends the Device ID to the description, so reports are readable
    public void fireEvent(EventType type, String description) {
        if (eventBus != null) {

            String enhancedDescription = "[Device: %s] %s".formatted(this.getId(), description);

            Event event = new EventBuilder()
                    .type(type)
                    .sourceId(this.getId())
                    .description(enhancedDescription)
                    .build();
            eventBus.publish(event);
        }
    }

    // Abstract method to be implemented by concrete devices (Oven, Fridge, Truck)
    protected abstract double getBaseValueForSensor(SensorType type);
}