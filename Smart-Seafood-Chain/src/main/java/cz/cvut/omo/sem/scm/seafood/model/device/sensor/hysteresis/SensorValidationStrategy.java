package cz.cvut.omo.sem.scm.seafood.model.device.sensor.hysteresis;

public interface SensorValidationStrategy {
    /**
     * Checks if the value triggers an alert (considering hysteresis).
     * @param currentValue The reading from the sensor.
     * @param targetValue The ideal value (e.g. -18.0).
     * @param isAlreadyAlerting State of the device (true if alert is currently active).
     * @return true if Alert should be ACTIVE (start or keep), false if SAFE.
     */
    boolean isAnomaly(double currentValue, double targetValue, boolean isAlreadyAlerting);
}