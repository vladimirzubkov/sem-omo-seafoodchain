package cz.cvut.omo.sem.scm.seafood.model.device.sensor;

import cz.cvut.omo.sem.scm.seafood.type.device.SensorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Random;

/**
 * Represents a physical IoT sensor attached to a device or vehicle.
 * Measures environmental data (Temperature, Vibration, GPS, etc.).
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class IoTSensor {

    private final String id;
    private final SensorType type;
    private double currentValue = 0.0; // Used in case of @NoArgsConstructor
    private boolean isAlertActive = false; // Tracks if this specific sensor is currently reporting an anomaly

    /**
     * Secondary constructor used for simple initialization when status is SAFE.
     * This constructor calls the main one, providing default values for optional fields.
     */
    public IoTSensor(String id, SensorType type) {
        this(id, type, 0.0, false);
    }

    /**
     * Simulates measuring a value based on the environment/device state.
     * Adds random noise to simulate real-world sensor fluctuation.
     *
     * @param baseValue The actual physical value (e.g., target freezer temperature).
     */
    public void measure(double baseValue) {
        Random random = new Random();
        // Simulating sensor noise: +/- 0.5 units
        double noise = (random.nextDouble() - 0.5) * 0.5;
        this.currentValue = baseValue + noise;
    }
}