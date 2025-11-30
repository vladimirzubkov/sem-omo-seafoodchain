package cz.cvut.omo.sem.scm.seafood.model.device.sensor.hysteresis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HysteresisCheck implements SensorValidationStrategy {

    private final double maxDeviation; // Deviation threshold
    private final double hysteresisBuffer; // Hysteresis

    @Override
    public boolean isAnomaly(double current, double target, boolean isAlreadyAlerting) {
        double diff = Math.abs(current - target);

        if (isAlreadyAlerting) {
            // To turn off the alarm we need to return to 'safe zone' with a margin
            // Diff must be < (Limit - Buffer)
            return diff > (maxDeviation - hysteresisBuffer);
        } else {
            // To activate the alarm, we need to exceed the limit
            // Diff > Limit
            return diff > maxDeviation;
        }
    }
}